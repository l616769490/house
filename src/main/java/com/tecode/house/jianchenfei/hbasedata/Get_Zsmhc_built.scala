package com.tecode.house.jianchenfei.hbasedata

import java.sql.{Connection, PreparedStatement, ResultSet}

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
class Get_Zsmhc_built extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("getzsmhc_built").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hbaseconf = HBaseConfiguration.create

    hbaseconf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val RateRDD = hBaseRDD.map { case (_, result) => {
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val built = Bytes.toString(result.getValue("info".getBytes, "BUILT".getBytes))
      val zsmhc = Bytes.toString(result.getValue("info".getBytes, "ZSMHC".getBytes))
      (built, zsmhc)
      //转换成年份区间
    }
    }
    val Rdd1 = RateRDD.map(x => {
      if (Integer.parseInt(x._1) < 2000 && Integer.parseInt(x._1) > 1900) {
        ("1900-2000", x._2)
      } else if (2000 < Integer.parseInt(x._1) && Integer.parseInt(x._1) < 2010) {
        ("2000-2010", x._2)
      } else {
        ("2010-2018", x._2)
      }
    })
    // (年份,(0-500， 500-1000， 1000-1500， 1500-2000， 2000-2500， 2500-3000， 3000-3500， 3500+))
    val RDD2 = Rdd1.map(
      x => {
        val ZSMHC = Integer.parseInt(x._2)
        if (ZSMHC < 500) {
          ( x._1+"_0-500",1)
        } else if (ZSMHC < 1000) {
          (x._1+"_500-1000", 1)
        } else if (ZSMHC < 1500) {
          (x._1+"_1000-1500", 1)
        } else if (ZSMHC < 2000) {
          (x._1+"_1500-2000", 1)
        } else if (ZSMHC < 2500) {
          (x._1+"_2000-2500", 1)
        } else if (ZSMHC < 3000) {
          (x._1+"_2500-3000", 1)
        } else if (ZSMHC < 3500) {
          (x._1+"_3000-3500", 1)
        } else {
          (x._1+"_3500+", 1)
        }
      }
    )
    val RDD3 = RDD2.reduceByKey(_+_)
    val RDDMap = RDD3.collect().toMap

    packageDate(tableName, RDDMap)

    true
  }

  def packageDate(tableName: String, values: Map[String,Int]): Unit = {
    var conn: Connection = null;
    conn = ConnSource.getConnection
    conn.setAutoCommit(false)

    // 插入报表表
    val reportDao: MysqlDao[Report] = new ReportImpl()
    val report: Report = new Report()
    report.setName("房产税统计")
    report.setCreate(System.currentTimeMillis())
    report.setYear(Integer.valueOf(tableName.split(":")(1)))
    report.setGroup("年份统计")
    report.setStatus(1)
    report.setUrl("http://166.166.0.13//zsmhc_built")
    val reportId: Int = reportDao.insert(report)


    // 插入图表表
    val daoDiagram: MysqlDao[Diagram] = new DiagramImpl()
    val diagram: Diagram = new Diagram()
    diagram.setName("房产税分布图")
    diagram.setReportid(reportId)
    diagram.setSubtext("统计房产税")
    diagram.setType(1)
    val DiagramId: Int = daoDiagram.insert(diagram)


    // 插入X轴表
    val daoXAxis: MysqlDao[XAxis] = new XAxisImpl()
    val xasis: XAxis = new XAxis()
    xasis.setName("年份")
    xasis.setDiagramid(DiagramId)
    xasis.setDimgroupname("年")
    val XAxisId: Int = daoXAxis.insert(xasis)


    // 插入Y轴表
    val daoYAxis: MysqlDao[YAxis] = new YAxisImpl()
    val yaxis: YAxis = new YAxis()
    yaxis.setName("数量")
    yaxis.setDiagramid(DiagramId)
    val YAxisId: Int = daoYAxis.insert(yaxis)


    // 插入图例表
    val daoLegend: MysqlDao[Legend] = new LegendImpl()
    var LegendId: Int=0;
    val legend: Legend = new Legend()
    for (elem<-values){


        legend.setDiagramid(DiagramId)
        legend.setDimgroupname(elem._1.split("_")(1))
        legend.setName("房产税")
      LegendId= daoLegend.insert(legend)
      }



    // 插入数据表
    // (年份,(0-500， 500-1000， 1000-1500， 1500-2000， 2000-2500， 2500-3000， 3000-3500， 3500+))
    val daoData: MysqlDao[Data] = new DataImpl()
    val data: Data = new Data()
    for (elem <-values){

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

object Get_Zsmhc_built {
  def main(args: Array[String]): Unit = {
    val b = new Get_Zsmhc_built
    b.analysis("thads:2013")

  }

}
