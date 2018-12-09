package com.tecode.house.lijun.serivce.impl

import java.sql.{Connection, SQLException}

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.house.lijun.dao.MySQLDao
import com.tecode.house.lijun.dao.impl.MySQLDaoImpl
import com.tecode.house.lijun.util.MySQLUtil
import com.tecode.house.lijun.bean._
import com.tecode.house.lijun.dao.MySQLDao
import com.tecode.house.lijun.dao.impl.MySQLDaoImpl
import com.tecode.house.lijun.util.MySQLUtil
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


class UTILITYAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
//    val conf = new SparkConf().setAppName("UTILITYAnalysis").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    调用读取数据的方法
    val rentsRDD: RDD[Double] = read(tableName, sc)
    //将具体的水电转换为水电区间并计数
    val utility: RDD[(String, Int)] = rentsRDD.map(x => {
      if (x < 100) {
        ("0-100", 1)
      } else if (x < 200) {
        ("100-200", 1)
      } else if (x < 300) {
        ("200-300", 1)
      } else if (x < 400) {
        ("300-400", 1)
      } else if (x < 500) {
        ("400-500", 1)
      } else {
        ("500+", 1)
      }
    })
    //    统计水电费的总数
    val value: RDD[(String, Int)] = utility.reduceByKey(_ + _)
    val buffer = value.map(x => (x._1, {
      Integer.valueOf(x._2)
    })).collect().toList
    //    封装对象
    val rent: Rent = new Rent(buffer)
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
  def read(tableName: String, sc: SparkContext): RDD[Double] = {
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的水电费列的值
    val utilityRDD: RDD[Double] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var value: Double = 0;
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("UTILITY"))
          value = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
      }
      value
    })

    utilityRDD
  }

  /**
    *
    * @param rent      分析结果
    * @param tableName HBase表名
    */
  def packageDate(rent: Rent, tableName: String) = {
    var conn: Connection = null
    val dao: MySQLDao = new MySQLDaoImpl()
    try {
      conn = MySQLUtil.getConn
      //事务控制，开启事务
      conn.setAutoCommit(false)
      //      插入报表表

      val report: Report = new Report()
      report.setName("水电费用")
      report.setCreate(System.currentTimeMillis())
      report.setYear(Integer.valueOf(tableName))
      report.setGroup("基础分析")
      report.setStatus(1)
      report.setUrl("http://166.166.166.166/cost")

      val reportId: Int = dao.putInTableReport(conn, report)
      //饼图
      //    插入图表表
      //      租金分布饼图
      val pieDiagram: Diagram = new Diagram()
      pieDiagram.setName("水电费用分布图")
      pieDiagram.setType(2)
      pieDiagram.setReportId(reportId)
      pieDiagram.setSubtext("统计水电费用")

      val pieDiagramId: Int = dao.putInTableDiagram(conn, pieDiagram)

      //    插入x轴表
      val pieXaxis: Xaxis = new Xaxis()
      pieXaxis.setName("美元")
      pieXaxis.setDiagramId(pieDiagramId)
      pieXaxis.setDimGroupName("费用")

      val pieXaxisId: Int = dao.putInTableXaxis(conn, pieXaxis)

      //    插入y轴表
      val pieYaxis = new Yaxis()
      pieYaxis.setName("费用")
      pieYaxis.setDiagramId(pieDiagramId)

      val pieYaxisId: Int = dao.putInTableYaxis(conn, pieYaxis)
      //    插入数据集表
      val pieLegend = new Legend()
      pieLegend.setName("费用区间")
      pieLegend.setDiagramId(pieDiagramId)
      pieLegend.setDimGroupName("空维度")

      val pieLegendId = dao.putInTableLegend(conn, pieLegend)

      //    插入数据表
      val list: List[(String, Integer)] = rent.getList()
      for (elem <- list) {
        val pieData = new Data(elem._2.toString, pieXaxisId, pieLegendId, elem._1, "水电费用")
        dao.putInTableData(conn, pieData)
      }

      conn.commit

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
