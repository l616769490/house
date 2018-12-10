package com.tecode.house.chenyong.dao

import java.sql.{PreparedStatement, ResultSet, SQLException}
import java.{sql, util}

import com.tecode.house.chenyong.bean._
import com.tecode.house.chenyong.dao.impl.CYMySQLDaoImpl
import com.tecode.house.chenyong.utils.MySQLUtil
import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.utils.SparkUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Connection, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Repository

class RoomsAnalysisByAge extends Analysis{
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {


//    val conf = new SparkConf().setAppName("RoomsAnalysisByAge").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    调用读取数据的方法
    val roomsRDD: RDD[(Int, Int)] = read(tableName, "ROOMS", sc)
    val bedrmsRDD: RDD[(Int, Int)] = read(tableName, "BEDRMS", sc)
    //    调用分析方法，求各区间的均值
    //    总房间数
    val room: RDD[(String, Int)] = calculation(roomsRDD)

    val rooms: List[(String, Int)] = room.collect().toList

    //    卧室数
    val bedrms: RDD[(String, Int)] = calculation(bedrmsRDD)

    val bedrooms: List[(String, Int)] = bedrms.collect().toList

    packageData(tableName,rooms,bedrooms)
    true
  }

  def read(tableName: String, qualifiername: String, sc: SparkContext): RDD[(Int, Int)] = {
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val values: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = values.map(x => x._2)
    //    取出Result结果中的年龄列的值
    val ageRDD: RDD[(Int, Int)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0
      var age: Int = 0
      for (elem <- cells) {
        //获取传入的列的值
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        //获取户主年龄
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("AGE1"))
          age = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      (age, value)
      // 去除无效数据（age小于0，value小于0的数据）
    }).filter(x => x._1 >= 0 && x._2 >= 0)
    ageRDD
  }

  /**
    * 分析数据
    *
    * @param rdd
    * @return 包含年龄区间及其平均值的RDD
    */
  def calculation(rdd: RDD[(Int, Int)]): RDD[(String, Int)] = {
    //    将具体的年龄转换成年龄区间
    val result: RDD[(String, Int)] = rdd.map(x => {
      if (x._1 < 18 ) {
        ("18以下", x._2)
      } else if (x._1 < 41 && x._1 >= 18) {
        ("18-40", x._2)
      } else if(x._1 < 65 && x._1 >= 41){
        ("41-65",x._2)
      }else {
        ("65以上", x._2)
      }
    })
    //按年龄区间分组
    val value: RDD[(String, Int)] = result.reduceByKey(_ + _)
    value
  }


  def packageData(tableName: String,rooms:List[(String, Int)],bedrooms:List[(String, Int)]) = {
    var conn: sql.Connection = null;
    val ps: PreparedStatement = null;
    val rs: ResultSet = null;
    val msDao: CYMySQLDao = new CYMySQLDaoImpl()
    try {
      conn = MySQLUtil.getConn();
      //事务控制，开启事务
      conn.setAutoCommit(false);
      //房间数统计
      //插入报表表
      val roomReport: Report = new Report()
      roomReport.setName("房间数统计")
      roomReport.setCreate(System.currentTimeMillis())
      roomReport.setYear(Integer.valueOf(tableName.split(":")(1)))
      roomReport.setGroup("房间统计")
      roomReport.setStatus(1)
      roomReport.setUrl("/roomsAnalysisByAge")
      val roomReportId: Int = msDao.putInTableReport(conn, roomReport)

      //房间数饼图
      val roomDiagram: Diagram = new Diagram()
      roomDiagram.setName("各年龄区间房间数")
      roomDiagram.setType(2)
      roomDiagram.setReportId(roomReportId)
      roomDiagram.setSubtext("统计各年龄区间房间数")

      val roomDiagramId: Int = msDao.putInTableDiagram(conn, roomDiagram)

      //    插入x轴表
      val roomXaxis: XAxis = new XAxis()
      roomXaxis.setName("年龄区间")
      roomXaxis.setDiagramId(roomDiagramId)
      roomXaxis.setDimGroupName("户主年龄段")

      val roomXaxisId: Int = msDao.putInTableXaxis(conn, roomXaxis)

      //    插入y轴表
      val roomYaxis = new YAxis()
      roomYaxis.setName("间")
      roomYaxis.setDiagramId(roomDiagramId)

      val roomYaxisId: Int = msDao.putInTableYaxis(conn, roomYaxis)
      //    插入数据集表
      val roomLegend = new Legend()
      roomLegend.setName("房间数统计")
      roomLegend.setDiagramId(roomDiagramId)
      roomLegend.setDimGroupName("房间数统计")

      val roomLegendId = msDao.putInTableLegend(conn, roomLegend)
      for (elem <- rooms) {
        val roomsData = new Data(elem._2.toString,roomXaxisId,roomLegendId,elem._1,"房间分布")
        msDao.putInTableData(conn,roomsData)
      }

      //卧室数统计
      val bedroomsReport: Report = new Report()
      bedroomsReport.setName("卧室数统计")
      bedroomsReport.setCreate(System.currentTimeMillis())
      bedroomsReport.setYear(Integer.valueOf(tableName.split(":")(1)))
      bedroomsReport.setGroup("卧室统计")
      bedroomsReport.setStatus(1)
      val reportId: Int = msDao.putInTableReport(conn, bedroomsReport)

      //房间数饼图
      val bedroomsDiagram: Diagram = new Diagram()
      bedroomsDiagram.setName("各年龄区间卧室数")
      bedroomsDiagram.setType(2)
      bedroomsDiagram.setReportId(reportId)
      bedroomsDiagram.setSubtext("统计各年龄区间卧室数")

      val bedroomsDiagramId: Int = msDao.putInTableDiagram(conn, bedroomsDiagram)

      //    插入x轴表
      val bedroomsXaxis: XAxis = new XAxis()
      bedroomsXaxis.setName("年龄区间")
      bedroomsXaxis.setDiagramId(bedroomsDiagramId)
      bedroomsXaxis.setDimGroupName("户主年龄段")

      val bedroomsXaxisId: Int = msDao.putInTableXaxis(conn, bedroomsXaxis)

      //    插入y轴表
      val bedroomsYaxis = new YAxis()
      bedroomsYaxis.setName("间")
      bedroomsYaxis.setDiagramId(bedroomsDiagramId)

      val bedroomsYaxisId: Int = msDao.putInTableYaxis(conn, bedroomsYaxis)
      //    插入数据集表
      val bedroomsLegend = new Legend()
      bedroomsLegend.setName("卧室数统计")
      bedroomsLegend.setDiagramId(bedroomsDiagramId)
      bedroomsLegend.setDimGroupName("卧室数统计")

      val bedroomsLegendId = msDao.putInTableLegend(conn, bedroomsLegend)
      for (elem <- bedrooms) {
        val bedroomsData = new Data(elem._2.toString,bedroomsXaxisId,bedroomsLegendId,elem._1,"卧室分布")
        msDao.putInTableData(conn,bedroomsData)
      }

      val search = new Search("年龄区间搜索", "年龄", reportId)
      msDao.putInTableSearch(conn, search)

      conn.commit()
    } catch {
      case e: Exception => {
        //回滚事务
        try {
          conn.rollback();
        } catch {
          case e1: SQLException => e1.printStackTrace()
        }
        e.printStackTrace();
      }
    } finally {
      MySQLUtil.close(conn);
    }
  }
}
