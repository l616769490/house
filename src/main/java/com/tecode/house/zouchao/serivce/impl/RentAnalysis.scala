package com.tecode.house.zouchao.serivce.impl


import java.sql.{Connection, PreparedStatement, ResultSet, SQLException}
import java.util

import com.tecode.house.d01.service.Analysis
import com.tecode.house.zouchao.bean._
import com.tecode.house.zouchao.dao.MySQLDao
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl
import com.tecode.house.zouchao.util.MySQLUtil
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


class RentAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("rentAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    调用读取数据的方法
    val rentsRDD: RDD[Int] = read(tableName, sc)
    //将具体的租金转换为租金区间并计数
    val rents: RDD[(String, Int)] = rentsRDD.map(x => {
      if (x < 1000) {
        ("(0,1000)", 1)
      } else if (x < 1500) {
        ("(1000,1500)", 1)
      } else if (x < 2000) {
        ("(1500,2000)", 1)
      } else if (x < 2500) {
        ("(2000,2500)", 1)
      } else if (x < 3000) {
        ("(2500,3000)", 1)
      } else {
        ("(3000,)", 1)
      }
    })
    //    统计各租金区间的总数
    val value: RDD[(String, Int)] = rents.reduceByKey(_ + _)
    //    求最大租金
    val max: Int = rentsRDD.max()
    //    求最小租金
    val min: Int = rentsRDD.min()
    //    求平均租金
    val count: Long = rentsRDD.count()
    val sum: Double = rentsRDD.sum()
    val avg: Double = sum / count
    //    println("max: " + max)
    //    println("min: " + min)
    //    println("avg: " + avg)
    //    value.collect().foreach(println)
    //    将int类型封装为Integer类型，并将RDD变成Buffer
    val buffer: mutable.Buffer[(String, Integer)] = value.map(x => (x._1, {
      Integer.valueOf(x._2)
    })).collect().toBuffer
    //    将Buffer类型转换为java的List类型
    val list: util.List[(String, Integer)] = scala.collection.JavaConversions.bufferAsJavaList(buffer)
    //    封装对象
    val rent: Rent = new Rent(max, min, avg, list)
    //    调用封装方法
    packageDate(rent, tableName)
    sc.stop()
    true
  }

  /**
    * 从HBase读取数据
    *
    * @param tableName 表名
    * @param sc        SparkContext
    * @return 租金的RDD
    */
  def read(tableName: String, sc: SparkContext): RDD[Int] = {
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
    val rentsRDD: RDD[Int] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0;
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      value
    })
    rentsRDD
  }


  def packageDate(rent: Rent, tableName: String) = {
    var conn: Connection = null;
    val ps: PreparedStatement = null;
    val rs: ResultSet = null;
    val dao: MySQLDao = new MySQLDaoImpl()
    try {
      conn = MySQLUtil.getConn();
      //事务控制，开启事务
      conn.setAutoCommit(false);
      //      插入报表表

      val report: Report = new Report()
      report.setName("公平市场租金")
      report.setCreate(System.currentTimeMillis())
      report.setYear(Integer.valueOf(tableName.split(":")(1)))
      report.setGroup("基础分析")
      report.setStatus(1)

      val reportId: Int = dao.putInTableReport(conn, report)
      //饼图
      //    插入图表表
      //      租金分布饼图
      val pieDiagram: Diagram = new Diagram()
      pieDiagram.setName("租金分布图")
      pieDiagram.setType(2)
      pieDiagram.setReportId(reportId)
      pieDiagram.setSubtext("统计租金分布情况")

      val pieDiagramId: Int = dao.putInTableDiagram(conn, pieDiagram)

      //    插入x轴表
      val pieXaxis: Xaxis = new Xaxis()
      pieXaxis.setName("美元")
      pieXaxis.setDiagramId(pieDiagramId)
      pieXaxis.setDimGroupName("租金")

      val pieXaxisId: Int = dao.putInTableXaxis(conn, pieXaxis)
      //    插入y轴表
      val pieYaxis = new Yaxis()
      pieYaxis.setName("户")
      pieYaxis.setDiagramId(pieDiagramId)

      val pieYaxisId: Int = dao.putInTableYaxis(conn, pieYaxis)

      //    插入数据集表
      val pieLegend = new Legend()
      pieLegend.setName("租金区间")
      pieLegend.setDiagramId(pieDiagramId)
      pieLegend.setDimGroupName("租金统计")

      val pieLegendId = dao.putInTableLegend(conn, pieLegend)

      //    插入数据表
      //    插入搜索表


      //折线图

      //租金极值折线图

      val lineDiagram: Diagram = new Diagram()
      lineDiagram.setName("租金最大最小值")
      lineDiagram.setType(0)
      lineDiagram.setReportId(reportId)
      lineDiagram.setSubtext("统计租金的最大最小平均值")

      val lineDiagramId: Int = dao.putInTableDiagram(conn, lineDiagram)


      //    插入x轴表
      val lineXaxis: Xaxis = new Xaxis()
      lineXaxis.setName("美元")
      lineXaxis.setDiagramId(lineDiagramId)
      lineXaxis.setDimGroupName()

      val lineXaxisId: Int = dao.putInTableXaxis(conn, lineXaxis)

      //    插入y轴表
      val lineYaxis = new Yaxis()
      lineYaxis.setName("美元")
      lineYaxis.setDiagramId(lineDiagramId)

      val lineYaxisId: Int = dao.putInTableYaxis(conn, lineYaxis)
      //    插入数据集表
      val lineMaxLegend = new Legend()
      lineMaxLegend.setName("极值")
      lineMaxLegend.setDiagramId(lineDiagramId)
      lineMaxLegend.setDimGroupName("最大值")

      val lineMaxLegendId = dao.putInTableLegend(conn, lineMaxLegend)

      val lineMinLegend = new Legend()
      lineMinLegend.setName("极值")
      lineMinLegend.setDiagramId(lineDiagramId)
      lineMinLegend.setDimGroupName("最小值")
      val lineMinLegendId = dao.putInTableLegend(conn, lineMinLegend)

      val lineAvgLegend = new Legend()
      lineAvgLegend.setName("极值")
      lineAvgLegend.setDiagramId(lineDiagramId)
      lineAvgLegend.setDimGroupName("平均值")
      val lineAvgLegendId = dao.putInTableLegend(conn, lineAvgLegend)



      //    插入数据表



      //    插入搜索表


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