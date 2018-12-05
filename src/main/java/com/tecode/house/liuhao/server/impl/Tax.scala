package com.tecode.house.liuhao.server.impl


import com.tecode.house.liuhao.bean._
import com.tecode.house.liuhao.dao.MysqlDao
import com.tecode.house.liuhao.dao.impl.PutMysqlDaoImpl
import com.tecode.house.liuhao.utils.MySQLUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

object Tax {
  def main(args: Array[String]): Unit = {
    val tax = new Tax()
    println("开始读取数据...")
    val read: RDD[(Int, Double, Double, Double)] = tax.readTax("2013")
    //    read.collect().foreach(println)
    println("读取数据完成，开始分析...")
    val analy: ArrayBuffer[(String, Int)] = tax.analyTax(read)
    //    for (elem <- analy) {
    //      println("aaaaa"+elem)
    //    }
    println("分析完成，开始插入...")
    tax.packageTaxBean("2013", analy)
    println("插入完成！")
  }
}

class Tax {
  /**
    * 读取和base中的数据
    *
    * @param tablename
    * @return
    */
  def readTax(tablename: String): RDD[(Int, Double, Double, Double)] = {
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = new SparkContext(con)
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "INFO")
    //读取hbase中数据并转换为rdd

    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],

      classOf[Result]).map(x =>

      (Bytes.toString(x._2.getValue(Bytes.toBytes("INFO"), Bytes.toBytes("METRO3"))).toInt,
        Bytes.toString(x._2.getValue(Bytes.toBytes("INFO"), Bytes.toBytes("ZSMHC"))).toDouble,
        Bytes.toString(x._2.getValue(Bytes.toBytes("INFO"), Bytes.toBytes("UTILITY"))).toDouble,
        Bytes.toString(x._2.getValue(Bytes.toBytes("INFO"), Bytes.toBytes("OTHERCOST"))
        ).toDouble))
    //    hbaseRDD.collect().foreach(println)
    hbaseRDD
  }

  /**
    * 数据分析
    *
    * @param hbaseRDD
    * @return
    */
  def analyTax(hbaseRDD: RDD[(Int, Double, Double, Double)]): ArrayBuffer[(String, Int)] = {

    //个规模城市的税务详情平均值
    val home = hbaseRDD.map(x => (x._1, x._2)).groupByKey().map(x => (x._1, (x._2.toList.sum / x._2.toList.size).toInt))


    val Utilityfee = hbaseRDD.map(x => (x._1, x._3)).map(x => (x._1, x._2)).groupByKey().map(x => (x._1, (x._2.toList.sum / x._2.toList.size).toInt))
    val other = hbaseRDD.map(x => (x._1, x._4)).map(x => (x._1, x._2)).groupByKey().map(x => (x._1, (x._2.toList.sum / x._2.toList.size).toInt))


    //val homes = home.combineByKey(v => (v, 1), (c: (Double, Int), v1) => (c._1 + v1, c._2 + 1), (c1: (Double, Int), c2: (Double, Int)) => (c1._1 + c2._1, c1._2 + c2._2))
    //各个城市规模下的家庭收入的平均值
    //val result1 = homes.map(x => (x._1, x._2._1 / x._2._2))

    //result1.collect.foreach(println)
    val array: ArrayBuffer[(String, Int)] = new ArrayBuffer[(String, Int)]()

    val map: RDD[(String, Int)] = home.map(x => {
      if (x._1 == 1) {
        ("一线城市房屋费用平均值", x._2);
      } else if (x._1 == 2) {
        ("二线城市房屋费用平均值", x._2);
      } else if (x._1 == 3) {
        ("三线城市房屋费用平均值", x._2);
      } else if (x._1 == 4) {
        ("四线城市房屋费用平均值", x._2);
      } else {
        ("五线城市房屋费用平均值", x._2);
      }
    })
    val collect: Array[(String, Int)] = map.collect()
//    for (elem <- collect) {
//      array +=elem
//
//    }
    array.foreach(println)
    val map1: RDD[(String, Int)] = Utilityfee.map(x => {
      if (x._1 == 1) {
        ("一线城市水电费用平均值", x._2);
      } else if (x._1 == 2) {
        ("二线城市水电费用平均值", x._2);
      } else if (x._1 == 3) {
        ("三线城市水电费用平均值", x._2);
      } else if (x._1 == 4) {
        ("四线城市水电费用平均值", x._2);
      } else {
        ("五线城市水电费用平均值", x._2);
      }
    })
    val collect1: Array[(String, Int)] = map1.collect()
    for (elem <- collect1) {
      array +=elem

    }
    /*//各个城市规模下的其他费用的总和
    val others = home.combineByKey(v => (v, 1), (c: (Double, Int), v1) => (c._1 + v1, c._2 + 1), (c1: (Double, Int), c2: (Double, Int)) => (c1._1 + c2._1, c1._2 + c2._2))
    //各个城市规模下的其他费用的平均值
    val result3 = others.map(x => (x._1 + ",other", x._2._1 / x._2._2)).collect()*/

    val map2 = other.map(x => {
      if (x._1 == 1) {
        (("一线城市其他费用平均值", x._2))
      } else if (x._1 == 2) {
       (("二线城市其他费用平均值", x._2))
      } else if (x._1 == 3) {
        (("三线城市其他费用平均值", x._2))
      } else if (x._1 == 4) {
        (("四线城市其他费用平均值", x._2))
      } else {
        (("五线城市其他费用平均值", x._2))
      }
    })
    val collect2: Array[(String, Int)] = map2.collect()
    for (elem <- collect2) {
      array +=elem
    }

    for (elem <- array) {
      println(elem)
    }
    array
  }

  def packageTaxBean(tablename: String, array: ArrayBuffer[(String, Int)]) = {
    val dao: MysqlDao = new PutMysqlDaoImpl()
    val conn = MySQLUtil.getConn()
    //   //开启事务
    //   conn.setAutoCommit(false)
    //封装到report的bean
    val report = new Report()
    report.setName("各个城市规模税务统计")
    report.setCreate(System.currentTimeMillis())
    report.setYear(Integer.valueOf(tablename))
    report.setGroup("城市规模")
    report.setStatus(1)
    report.setUrl("http://166.166.5.101:8080/CitySize_Tax_Avg")
    val reportId = dao.putToTableReport(conn, report)
    //柱状图封装bean
    val digram = new Diagram()
    digram.setName("各个城市规模税务统计图")
    digram.setType(1)
    digram.setReportId(reportId)
    digram.setSubtext("各个规模下城市的税务统计")
    val diagramId = dao.putToTableDiagram(conn, digram)
    //封装x轴
    val x = new Xaxis()
    x.setName("城市规模")
    x.setDiagramId(diagramId)
    x.setDimGroupName("城市规模")
    val XaxisId = dao.putToTablexAxis(conn, x)
    //封装y轴
    val y = new Yaxis()
    y.setName("美元")
    y.setDiagramId(diagramId)
    val yaxisId = dao.putToTableYaxis(conn, y)

    //封装legendBean
    val legend = new Legend()
    legend.setName("各个城市规模下的税务统计")
    legend.setDimGroupName("城市规模")
    legend.setDiagramId(diagramId)
    val legendId = dao.putToTableLegend(conn, legend)

    //封装到databean

    for (elem <- array) {
      var x1 = elem._1
      var xvalue = elem._2.toString
      val data = new Data()
      data.setValue(x1)
      data.setLegendId(legendId)
      data.setX(xvalue)
      data.setxId(XaxisId)
      data.setLegend("城市规模")
//      println(data.getX + ":" + data.getValue)
      dao.putToTableData(conn, data)
    }

    //封装searchBean
    val search = new Search()
    search.setName("各个规模城市税务统计")
    search.setDimGroupName("城市规模")
    search.setReportId(reportId)

    dao.putToTablesearch(conn, search)

  }


}

