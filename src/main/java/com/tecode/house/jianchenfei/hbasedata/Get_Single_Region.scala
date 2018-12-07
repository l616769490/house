package com.tecode.house.jianchenfei.hbasedata

import java.sql.Connection

import com.tecode.house.d01.service.Analysis
import com.tecode.house.jianchenfei.jdbc.bean._
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao
import com.tecode.house.jianchenfei.jdbc.dao.impl._
import com.tecode.house.jianchenfei.utils.ConnSource
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/5.
  */
class Get_Single_Region extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("getSingle_region").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hbaseconf = HBaseConfiguration.create

    hbaseconf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    val SingleRDD = hBaseRDD.map { case (_, result) => {
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val region = Bytes.toString(result.getValue("info".getBytes, "REGION".getBytes))
      val nunits = Bytes.toString(result.getValue("info".getBytes, "NUNITS".getBytes))
      (region, nunits)
    }
    }
    var RDD1 = SingleRDD.map(x => {
      if (Integer.parseInt(x._1) == 1) {
        (1, x._2)
      } else if (Integer.parseInt(x._1) == 2) {
        (2, x._2)
      } else if (Integer.parseInt(x._1) == 3) {
        (3, x._2)
      } else {
        (4, x._2)
      }
    })
    val RDD2 = RDD1.map(x => {
      val nunits = Integer.parseInt(x._2)
      if (nunits == 1) {
        (x._1 + "_独栋", 1)
      } else {
        (x._1 + "_非独栋", 1)
      }
    })
    val RDD3 = RDD2.reduceByKey(_ + _)
    val RDDMap = RDD3.collect().toMap

    packageDate(tableName, RDDMap)

    true
  }

  def packageDate(tableName: String, values: Map[String, Int]): Unit = {
    var conn: Connection = null;
    conn = ConnSource.getConnection
    conn.setAutoCommit(false)

    // 插入报表表
    val reportDao: MysqlDao[Report] = new ReportImpl()
    val report: Report = new Report()
    report.setName("独栋比例统计")
    report.setCreate(System.currentTimeMillis())
    report.setYear(Integer.valueOf(tableName.split(":")(1)))
    report.setGroup("按区域统计")
    report.setStatus(1)
    report.setUrl("http://166.166.0.13//single_region")
    val reportId: Int = reportDao.insert(report)


    // 插入图表表
    val daoDiagram: MysqlDao[Diagram] = new DiagramImpl()
    val diagram: Diagram = new Diagram()
    diagram.setName("独栋比例分布图")
    diagram.setReportid(reportId)
    diagram.setSubtext("统计独栋比例")
    diagram.setType(1)
    val DiagramId: Int = daoDiagram.insert(diagram)


    // 插入X轴表
    val daoXAxis: MysqlDao[XAxis] = new XAxisImpl()
    val xasis: XAxis = new XAxis()
    xasis.setName("区域")
    xasis.setDiagramid(DiagramId)
    xasis.setDimgroupname("区域")
    val XAxisId: Int = daoXAxis.insert(xasis)


    // 插入Y轴表
    val daoYAxis: MysqlDao[YAxis] = new YAxisImpl()
    val yaxis: YAxis = new YAxis()
    yaxis.setName("数量")
    yaxis.setDiagramid(DiagramId)
    val YAxisId: Int = daoYAxis.insert(yaxis)


    // 插入图例表
    val daoLegend: MysqlDao[Legend] = new LegendImpl()
    var LegendId: Int = 0;
    val legend: Legend = new Legend()
    for (elem <- values) {


      legend.setDiagramid(DiagramId)
      legend.setDimgroupname(elem._1.split("_")(1))
      legend.setName("独栋比例")
      LegendId = daoLegend.insert(legend)
    }


    // 插入数据表

    val daoData: MysqlDao[Data] = new DataImpl()
    val data: Data = new Data()
    for (elem <- values) {

      data.setX(elem._1.split("_")(0))
      data.setValue(elem._2.toString)
      data.setLegend(elem._1.split("_")(1))
      data.setLegendid(LegendId)
      data.setXid(XAxisId)
      daoData.insert(data)
    }


    conn.commit()
    conn.rollback()
  }
}
object Get_Single_Region{
  def main(args: Array[String]): Unit = {
    val s = new Get_Single_Region
    s.analysis("thads:2013")
  }
}
