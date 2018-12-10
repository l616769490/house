package com.tecode.house.zouchao.serivce.impl

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.util

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.house.zouchao.bean._
import com.tecode.house.zouchao.dao.MySQLDao
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl
import com.tecode.house.zouchao.util.MySQLUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

class RoomsByBuildAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {


    val sc = SparkUtil.getSparkContext
    //    调用读取数据的方法
    val roomsRDD: RDD[(Int, Int)] = read(tableName, "ROOMS", sc)
    val bedrmsRDD: RDD[(Int, Int)] = read(tableName, "BEDRMS", sc)
    //    调用分析方法，求各区间的均值
    //    总房间数
    val rooms: RDD[(String, Int)] = calculation(roomsRDD)

    //    println("总房屋===========")
    //    rooms.collect().foreach(println)
    //    println("卧室数==================")
    //    bedrms.collect().foreach(println)

    val roomsList: List[(String, Int)] = rooms.collect().toList


    //    卧室数
    val bedrms: RDD[(String, Int)] = calculation(bedrmsRDD)


    val bedrmList: List[(String, Int)] = bedrms.collect().toList

    packageDate(tableName, roomsList, bedrmList)
//    sc.stop()
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
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的租金列的值
    val rentsRDD: RDD[(Int, Int)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0
      var year: Int = 0
      for (elem <- cells) {
        //        获取传入的列的值
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        //        获取建成年份
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          year = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      (year, value)
    })
    //    去除无效数据（value小于0的数据）
    val value: RDD[(Int, Int)] = rentsRDD.filter(_._2 > 0)
    //    value.collect().foreach(println)
    value
  }

  /**
    * 分析数据
    *
    * @param rdd 包含（建成年份，房间数）的RDD
    * @return 包含建成年份区间及其房屋数的RDD
    */
  def calculation(rdd: RDD[(Int, Int)]): RDD[(String, Int)] = {
    //    val d = rdd.map(_._2).sum()
    //    println("总数：  " + d)
    //    将具体的建成年份转换成建成年份区间
    val result: RDD[(String, Int)] = rdd.map(x => {
      if (x._1 < 1940) {
        ("1900-1940", x._2)
      } else if (x._1 < 1960) {
        ("1940-1960", x._2)
      } else if (x._1 < 1980) {
        ("1960-1980", x._2)
      } else if (x._1 < 2000) {
        ("1980-2000", x._2)
      } else {
        ("2000+", x._2)
      }
    })
    //统计各建成年份区间的和
    val value: RDD[(String, Int)] = result.reduceByKey(_ + _)
    value
  }


  /**
    * 将分析结果导入MySQL数据库
    *
    * @param tableName 表名
    * @param roomsList 房屋数分析结果
    * @param bedrmList 卧室数分析结果
    */
  def packageDate(tableName: String, roomsList: List[(String, Int)], bedrmList: List[(String, Int)]) = {
    var conn: Connection = null
    val dao: MySQLDao = new MySQLDaoImpl()
    try {
      conn = MySQLUtil.getConn
      //事务控制，开启事务
      conn.setAutoCommit(false)
      //      插入报表表
      val report: Report = new Report()
      report.setName("房间数统计")
      report.setCreate(System.currentTimeMillis())
      report.setYear(Integer.valueOf(tableName.split(":")(1)))
      report.setGroup("年份统计")
      report.setStatus(1)
      report.setUrl("/roomsByBuild")
      val reportId: Int = dao.putInTableReport(conn, report)



      //折线图

      //房间数折线图

      val lineDiagram: Diagram = new Diagram()
      lineDiagram.setName("各建成年份区间房间数")
      lineDiagram.setType(0)
      lineDiagram.setReportId(reportId)
      lineDiagram.setSubtext("统计各建成年份区间房间数")

      val lineDiagramId: Int = dao.putInTableDiagram(conn, lineDiagram)


      //    插入x轴表
      val lineXaxis: Xaxis = new Xaxis()
      lineXaxis.setName("年份区间")
      lineXaxis.setDiagramId(lineDiagramId)
      lineXaxis.setDimGroupName("建筑年份")

      val lineXaxisId: Int = dao.putInTableXaxis(conn, lineXaxis)

      //    插入y轴表
      val lineYaxis = new Yaxis()
      lineYaxis.setName("间")
      lineYaxis.setDiagramId(lineDiagramId)

      val lineYaxisId: Int = dao.putInTableYaxis(conn, lineYaxis)
      //    插入数据集表
      val lineLegend = new Legend()
      lineLegend.setName("房间数统计")
      lineLegend.setDiagramId(lineDiagramId)
      lineLegend.setDimGroupName("房间数统计")

      val lineLegendId = dao.putInTableLegend(conn, lineLegend)



      //    插入数据表
      //      房间数
      for (elem <- roomsList) {
        /*
        private var value: String = null
    private var xId: Int = 0
    private var legendId: Int = 0
    private var x: String = null
    private var legend: String = null
         */
        val data = new Data(elem._2.toString, lineXaxisId, lineLegendId, elem._1, "总房间数")
        dao.putInTableData(conn, data)
      }

      //      卧室数
      for (elem <- bedrmList) {
        val data = new Data(elem._2.toString, lineXaxisId, lineLegendId, elem._1, "总卧室数")
        dao.putInTableData(conn, data)
      }

      //    插入搜索表
      //建成年份区间搜索
      val yearYearch = new Search("建成年份区间搜索", "建筑年份", reportId)
      dao.putInTableSearch(conn, yearYearch)
      //     城市规模搜索
      val cityYearch = new Search("城市规模搜索", "城市规模", reportId)
      dao.putInTableSearch(conn, cityYearch)
      conn.commit()
    } catch {
      case e: Exception => {
        //回滚事务
        try {
          conn.rollback()
        } catch {
          case e1: SQLException => e1.printStackTrace()
        }
        e.printStackTrace()
      }
    } finally {
      MySQLUtil.close(conn)
    }
  }

}

