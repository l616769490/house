package com.tecode.house.chenyong.dao

import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.util

import com.tecode.house.chenyong.bean._
import com.tecode.house.chenyong.dao.impl.CYMySQLDaoImpl
import com.tecode.house.chenyong.utils.MySQLUtil
import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.utils.SparkUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.collection.mutable


class AgeAnalysis extends Analysis {

  def readData(tableName: String, sc: SparkContext): RDD[Int] = {

    //配置HBase链接
    val conf = HBaseConfiguration.create();
    //读取的表名
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    //读取的列族
    conf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //获取数据
    val values: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val value: RDD[Result] = values.map(x => x._2)
    //取出Result结果中年龄列的值
    val ageRDD: RDD[Int] = value.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var age: Int = 0
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("AGE1")) age = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      age
    })
    ageRDD
  }

  def packageAgeData(age: Age, tableName: String) = {
    //创建链接
    var conn: Connection = null
    val ps: PreparedStatement = null
    val rs: ResultSet = null
    val msDao: CYMySQLDao = new CYMySQLDaoImpl()
    //获取链接
    try {
      conn = MySQLUtil.getConn()
      //开启事物
      conn.setAutoCommit(false)
      //插入报表表中
      val report: Report = new Report()
      report.setName("户主年龄分布")
      report.setCreate(System.currentTimeMillis())
      report.setYear(Integer.valueOf(tableName.split(":")(1)))
      report.setGroup("基础分析")
      report.setStatus(1)
      report.setUrl("/ageAnalysis")
      val reportId: Int = msDao.putInTableReport(conn, report)
      //插入图表表,饼图
      val pieDiagram: Diagram = new Diagram()
      pieDiagram.setName("年龄分布图")
      pieDiagram.setReportId(reportId)
      pieDiagram.setType(2)
      pieDiagram.setSubtext("统计户主年龄分布情况")
      val pieDiagramId: Int = msDao.putInTableDiagram(conn, pieDiagram)
      //插入x轴
      val pieXAxis: XAxis = new XAxis()
      pieXAxis.setName("年龄区间")
      pieXAxis.setDiagramId(pieDiagramId)
      pieXAxis.setDimGroupName("户主年龄")

      val pieXAxisId: Int = msDao.putInTableXaxis(conn, pieXAxis)
      //插入Y轴
      val pieYAxis = new YAxis()
      pieYAxis.setName("年龄")
      pieYAxis.setDiagramId(pieDiagramId)

      val pieYAxisId: Int = msDao.putInTableYaxis(conn, pieYAxis)

      //插入数据集
      val pieLegend = new Legend()
      pieLegend.setName("年龄区间")
      pieLegend.setDiagramId(pieDiagramId)
      pieLegend.setDimGroupName("户主年龄")
      val pieLegendId = msDao.putInTableLegend(conn, pieLegend)

      //插入数据集
      val list: mutable.Buffer[(String, Integer)] = age.getList().asScala
      for (elem <- list) {
        val pieData = new Data(elem._2.toString, pieXAxisId, pieLegendId, elem._1, "分布统计")
        msDao.putInTableData(conn, pieData)
      }

//      val search = new Search("年龄区间搜索", "年龄", reportId)
      ////      msDao.putInTableSearch(conn, search)

      /*val columnarReport: Report = new Report()
      columnarReport.setName("户主年龄极值分布")
      columnarReport.setCreate(System.currentTimeMillis())
      columnarReport.setYear(Integer.valueOf(tableName.split(":")(1)))
      columnarReport.setGroup("基础分析")
      columnarReport.setStatus(1)
      columnarReport.setUrl("/ageAnalysis")
      val columnarReportId: Int = msDao.putInTableReport(conn, columnarReport)*/


      //插入数据表，柱状图
      val columnarDiagram: Diagram = new Diagram()
      columnarDiagram.setName("年龄最大最小值")
      columnarDiagram.setType(1)
      columnarDiagram.setReportId(reportId)
      columnarDiagram.setSubtext("年龄最大最小平均值统计")

      val columnarDiagramId: Int = msDao.putInTableDiagram(conn, columnarDiagram)

      //插入x轴表
      val columnarXAxis: XAxis = new XAxis()

      columnarXAxis.setName("年龄")
      columnarXAxis.setDiagramId(columnarDiagramId)
      columnarXAxis.setDimGroupName("年龄区间")
      val columnarXAxisId: Int = msDao.putInTableXaxis(conn, columnarXAxis)

      //插入Y轴表
      val columnarYAxis: YAxis = new YAxis()

      columnarYAxis.setName("年龄")
      columnarYAxis.setDiagramId(columnarDiagramId)

      val columnarYAxisId: Int = msDao.putInTableYaxis(conn, columnarYAxis)

      //插入数据集
      val columnarMaxLegen = new Legend()
      columnarMaxLegen.setName("极值")
      columnarMaxLegen.setDiagramId(columnarDiagramId)
      columnarMaxLegen.setDimGroupName("最大年龄")
      val columnarMaxLegenId = msDao.putInTableLegend(conn, columnarMaxLegen)
      //插入数据表
      val columnarMaxData = new Data(age.getMaxAge.toString, columnarXAxisId, columnarMaxLegenId, "maxAge", "maxAge")
      msDao.putInTableData(conn, columnarMaxData)

      val columnarMinLegen = new Legend()
      columnarMinLegen.setName("极值")
      columnarMinLegen.setDiagramId(columnarDiagramId)
      columnarMinLegen.setDimGroupName("最小年龄")
      val columnarMinLegenId = msDao.putInTableLegend(conn, columnarMinLegen)

      val columnarMinData = new Data(age.getMinAge.toString, columnarXAxisId, columnarMinLegenId, "minAge", "minAge")
      msDao.putInTableData(conn, columnarMinData)

      val columnarAvgLegen = new Legend()
      columnarAvgLegen.setName("极值")
      columnarAvgLegen.setDiagramId(columnarDiagramId)
      columnarAvgLegen.setDimGroupName("平均年龄")
      val columnarAvgLegenId = msDao.putInTableLegend(conn, columnarAvgLegen)

      val columnarAvgData = new Data(age.getAvgAge.toString, columnarXAxisId, columnarAvgLegenId, "avgAge", "avgAge")
      msDao.putInTableData(conn, columnarAvgData)

      val columnarSearch = new Search("年龄区间搜索", "年龄极值", reportId)
      msDao.putInTableSearch(conn, columnarSearch)
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
    }
    finally {
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
//      val conf = new SparkConf().setAppName("AgeAnalysis").setMaster("local[*]")

      val sc = SparkUtil.getSparkContext

      val ageRDD: RDD[Int] = readData(tableName, sc).filter(x => x >= 0)

      //将年龄转换为在各年龄区间并进行计数
      val ageZone: RDD[(String, Int)] = ageRDD.map(x => {
        if (x < 18 && x >= 0) {
          ("18以下", 1)
        } else if (x >= 18 && x <= 40) {
          ("18-40", 1)
        } else if (x >= 41 && x < 65) {
          ("40-65", 1)
        } else {
          ("65以上", 1)
        }
      })

      //统计各年龄区间的总数
      val value: RDD[(String, Int)] = ageZone.reduceByKey(_ + _)
      //求最大年龄
      val maxAge: Int = ageRDD.max()
      //求最小年龄
      val minAge: Int = ageRDD.min()
      //统计总人数
      val count: Long = ageRDD.count()
      //统计总年龄
      val sumAge: Double = ageRDD.sum()
      val avgAge: Double = sumAge / count

      val tuples = value.map(x => (x._1, {
        Integer.valueOf(x._2)
      })).collect().toList.asJava
      //封装进Age对象
      val age: Age = new Age(maxAge, minAge, avgAge, tuples);
      //调用封装方法
      packageAgeData(age, tableName)
      return true
    }
}

