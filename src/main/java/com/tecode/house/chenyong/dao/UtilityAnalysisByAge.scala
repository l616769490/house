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

import scala.collection.mutable

class UtilityAnalysisByAge extends Analysis{

  def read(tableName: String, qualifiername: String, sc: SparkContext): RDD[(Int, Double)] = {
    //配置链接
    val hconf = HBaseConfiguration.create();
    //配置读取表名
    hconf.set(TableInputFormat.INPUT_TABLE,tableName)
    //读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS,"info")
    //获取数据
    val values:RDD[(ImmutableBytesWritable,Result)] = sc.newAPIHadoopRDD(hconf,classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
    val value: RDD[Result] = values.map(x => x._2)
    //取出result结果中的水电费列的值
    val utility: RDD[(Int,Double)] = value.map(x => {
      //遍历每个value
      val cells: Array[Cell] = x.rawCells()
      var util: Double = 0
      var age: Int = 0
      for (elem <- cells) {
        //获取传入的值
        if(Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername)){
          util = Bytes.toString(CellUtil.cloneValue(elem)).toDouble
        }
        if(Bytes.toString(CellUtil.cloneQualifier(elem)).equals("AGE1")){
          age = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        }
      }
      (age,util)
      //去除无效数据
    }).filter(x => x._1 >= 0 && x._2 >= 0)
    utility
  }

  def calculation(utilityRDD: RDD[(Int, Double)]): RDD[(String, Double)] = {
    //将具体户主年龄转换成年龄区间
    val ageZone: RDD[(String,Double)] = utilityRDD.map(x => {
      if(x._1 < 18){
        ("18以下",x._2)
      }else if(x._1 >= 18 && x._1 < 41){
        ("18-40",x._2)
      }else if(x._1 >= 41 && x._1 < 65){
        ("41-65",x._2)
      }else{
        ("65以上",x._2)
      }
    })
    //按年龄区间进行分组求和
    val result: RDD[(String,Double)] = ageZone.reduceByKey(_ + _)
    result
  }

  def packageData(tableName: String,utilityList:List[(String,Double)]) = {
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
      val utilityReport: Report = new Report()
      utilityReport.setName("水电费统计")
      utilityReport.setCreate(System.currentTimeMillis())
      utilityReport.setYear(Integer.valueOf(tableName.split(":")(1)))
      utilityReport.setGroup("水电费统计")
      utilityReport.setStatus(1)
      utilityReport.setUrl("/utilityAnalysisByAge")
      val roomReportId: Int = msDao.putInTableReport(conn, utilityReport)

      //房间数饼图
      val utilityDiagram: Diagram = new Diagram()
      utilityDiagram.setName("各年龄区间水电费")
      utilityDiagram.setType(2)
      utilityDiagram.setReportId(roomReportId)
      utilityDiagram.setSubtext("统计各年龄区间水电费")

      val utilityDiagramId: Int = msDao.putInTableDiagram(conn, utilityDiagram)

      //    插入x轴表
      val utilityXaxis: XAxis = new XAxis()
      utilityXaxis.setName("年龄区间")
      utilityXaxis.setDiagramId(utilityDiagramId)
      utilityXaxis.setDimGroupName("户主年龄段")

      val utilityXaxisId: Int = msDao.putInTableXaxis(conn, utilityXaxis)

      //    插入y轴表
      val utilityYaxis = new YAxis()
      utilityYaxis.setName("间")
      utilityYaxis.setDiagramId(utilityDiagramId)

      val utilityYaxisId: Int = msDao.putInTableYaxis(conn, utilityYaxis)
      //    插入数据集表
      val utilityLegend = new Legend()
      utilityLegend.setName("水电费统计")
      utilityLegend.setDiagramId(utilityDiagramId)
      utilityLegend.setDimGroupName("水电费统计")

      val utilityLegendId = msDao.putInTableLegend(conn, utilityLegend)
      for (elem <- utilityList) {
        val utilityData = new Data(elem._2.toString,utilityXaxisId,utilityLegendId,elem._1,"水电费统计")
        msDao.putInTableData(conn,utilityData)
      }

      val search = new Search("年龄区间搜索", "年龄", roomReportId)
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

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
//    val conf = new SparkConf().setAppName("UtilityAnalysisByAge").setMaster("local[*]")
    val sc =SparkUtil.getSparkContext
    //    调用读取数据的方法
    val utilityRDD: RDD[(Int, Double)] = read(tableName, "UTILITY", sc)
    //    调用分析方法
    //    总水电费
    val utility: RDD[(String, Double)] = calculation(utilityRDD)
    val utilityList: List[(String, Double)] = utility.collect().toList
    //封装
    packageData(tableName,utilityList)
    return true
  }
}
