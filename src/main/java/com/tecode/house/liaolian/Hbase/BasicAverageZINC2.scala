package com.tecode.house.liaolian.hbasedata

import java.sql.Connection

import com.tecode.house.d01.service.Analysis
import com.tecode.house.liaolian.bean._
import com.tecode.house.liaolian.dao.MysqlDao
import com.tecode.house.liaolian.dao.impl._
import com.tecode.house.liaolian.utils.ConnSource
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/3.
  */
class BasicAverageZINC2 extends Analysis {

  val conf = new SparkConf().setAppName("BasicAverageZINC2").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val hbaseconf = HBaseConfiguration.create



  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    hbaseconf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])


    /**
      * 获取平均收入数据
      */
    var map:Map[String,Int] = Map()

    val HBaseRDD = hBaseRDD.map { case (_, result) => {
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val p = Bytes.toDouble(result.getValue("info".getBytes, "PER".getBytes))
      val z = Bytes.toDouble(result.getValue("info".getBytes, "ZINC2".getBytes))
      (p,z)
    }
    }.map(x=>(x._2/x._1))

    val per: RDD[(String, Int)] = HBaseRDD.map(x => {
      if (x.equals("1")) {
        ("0-4999", 1)
      } else if (x.equals("2")) {
        ("5000-9999", 1)
      } else if (x.equals("3")) {
        ("10000-14999", 1)
      } else if (x.equals("4")) {
        ("15000-19999", 1)
      } else {
        ("20000+", 1)
      }
    })

    val value: RDD[(String, Int)] = per.reduceByKey(_ + _)
    val valueList = value.collect().toMap
//    //println(valueList)


//
    packageDate(tableName,valueList)
    true
  }

  def packageDate(tableName: String, values: Map[String, Int]) = {

    var conn: Connection = null;
    conn = ConnSource.getConnection
    conn.setAutoCommit(false)
    //val ps: PreparedStatement = null;
    // val rs: ResultSet = null;

    val dao: ReportImpl = new ReportImpl()
    //      插入报表表
    val report: Report = new Report()
    report.setName("家庭平均家庭收入统计")
    report.setCreate(System.currentTimeMillis())
    report.setYear(Integer.valueOf(tableName.split(":")(1)))
    report.setGroup("基础分析")
    report.setStatus(1)
    report.setUrl("http://166.166.0.13//per")
    val reportId: Int = dao.insert(report)


    // 插入图表表
    val daoDiagram: MysqlDao[Diagram] = new DiagramImpl()
    val diagram: Diagram = new Diagram()
    diagram.setName("家庭收入分布图")
    diagram.setReportid(reportId)
    diagram.setSubtext("统计家庭收入比例")
    diagram.setType(2)
    val DiagramId: Int = daoDiagram.insert(diagram)

    // 插入x轴表
    val daoXAxis: MysqlDao[XAxis] = new XAxisImpl()
    val xasis: XAxis = new XAxis()
    xasis.setName("收入")
    xasis.setDiagramid(DiagramId)
    xasis.setDimgroupname("平均收入")
    val XAxisId: Int = daoXAxis.insert(xasis)

    // 插入y轴表
    val daoYAxis: MysqlDao[YAxis] = new YAxisImpl()
    val yaxis: YAxis = new YAxis()
    yaxis.setName("百分比")
    yaxis.setDiagramid(DiagramId)
    val YAxisId: Int = daoYAxis.insert(yaxis)

    // 插入图例表
    val daoLegend: MysqlDao[Legend] = new LegendImpl()
    val legend: Legend = new Legend()
    legend.setDiagramid(DiagramId)
    legend.setDimgroupname("空维度")
    legend.setName("平均收入")
    val LegendId: Int = daoLegend.insert(legend)

    // 插入数据表
    val daoData: MysqlDao[Data] = new DataImpl()
    val data: Data = new Data()
    for (elem <- values) {
      data.setValue(elem._2.toString)
      data.setLegend("平均收入")
      data.setLegendid(LegendId)
      data.setXid(XAxisId)
      data.setX(elem._1)
      daoData.insert(data)
    }


    conn.commit()
    conn.rollback()
  }

}

object BasicAverageZINC2 {
  def main(args: Array[String]): Unit = {
    val b = new BasicAverageZINC2()
    b.analysis("thads:2013")

  }
}