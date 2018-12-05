package com.tecode.house.liuhao.server.impl

import com.tecode.house.liuhao.bean._
import com.tecode.house.liuhao.dao.MysqlDao
import com.tecode.house.liuhao.dao.impl.PutMysqlDaoImpl
import com.tecode.house.liuhao.utils.MySQLUtil
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/11/29.
  * 分析建筑结构类型
  *
  */
/*object StructureType {
  def main(args: Array[String]): Unit = {
    println("读取数据中...")
    val stru = new StructureType()
    val read = stru.read("2013", "STRUCTURETYPE")
    println("数据读取完成，开始分析...")
    val analy = stru.analyStructureType(read)

    println("分析完成，开始导入...")
    stru.packageBean("2013", analy)
    println("导入完成")
  }
}*/


class StructureType {

  /**
    * 读取hbase中数据
    *
    * @param tablename
    * @param qualifiername
    * @return
    */
  def read(tablename: String, qualifiername: String): RDD[(Int)] = {
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = new SparkContext(con)
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "INFO")
    //读取hbase中数据并转换为rdd
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]).map(_._2)

    val rdd1 = hbaseRDD.map(x => {
      val rawCells: Array[Cell] = x.rawCells()
      var value = 0
      for (elem <- rawCells) {

        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername)) {
          println(Bytes.toString(CellUtil.cloneValue(elem)).toInt)
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        }
      }
      value
    })
    rdd1
  }

  /**
    * 分析建筑建构类型的数据
    *
    * @param rdd 传入的数据
    * @return
    */
  def analyStructureType(rdd: RDD[Int]): Array[(String, Int)] = {
    val value = rdd.map(x => (x, 1))
    val result = value.map(x => {
      //println(x._1)
      if (x._1 == 1) {
        ("独栋建筑", x._2)
      } else if (x._1 == 2) {
        ("2-4户建筑", x._2)
      } else if (x._1 == 3) {
        ("5-19户建筑", x._2)
      } else if (x._1 == 4) {
        ("20-49户建筑", x._2)
      } else if (x._1 == 5) {
        ("50户以上建筑", x._2)
      } else {
        ("移动建筑", x._2)
      }

    }).reduceByKey(_ + _)
    val collect = result.collect()
    collect
  }

  /**
    * 封装数据到bean
    *
    * @param tablename hbase表名
    */
  def packageBean(tablename: String, value: Array[(String, Int)]) = {


    val dao: MysqlDao = new PutMysqlDaoImpl()
    val conn = MySQLUtil.getConn()
    //   //开启事务
    //   conn.setAutoCommit(false)
    //封装到report的bean
    val report = new Report()
    report.setName("类型")
    report.setCreate(System.currentTimeMillis())

    report.setYear(Integer.valueOf(tablename))

    report.setGroup("基础分析")
    report.setStatus(1)
    report.setUrl("http://166.166.5.101:8080/basics_structuretype_num")
    val reportId = dao.putToTableReport(conn, report)
    println(reportId)
    //柱状图封装bean
    val digram = new Diagram()
    digram.setName("建筑结构类型分布柱状图")
    digram.setType(1)
    digram.setReportId(reportId)
    digram.setSubtext("统计建筑结构类型的分布情况统计")
    val diagramId = dao.putToTableDiagram(conn, digram)
    //封装x轴
    val x = new Xaxis()
    x.setName("结构类型")
    x.setDiagramId(diagramId)
    x.setDimGroupName("建筑结构类型")
    val XaxisId = dao.putToTablexAxis(conn, x)
    //封装y轴
    val y = new Yaxis()
    y.setName("数量")
    y.setDiagramId(diagramId)
    val yaxisId = dao.putToTableYaxis(conn, y)

    //封装legendBean
    val legend = new Legend()
    legend.setName("建筑结构类型统计")
    legend.setDimGroupName("建筑结构类型统计")
    legend.setDiagramId(diagramId)
    val legendId = dao.putToTableLegend(conn, legend)

    //封装到databean
    val xvalue = for (elem <- value) {
      val xvalue = elem._1
      val x1 = elem._2.toString
      val data = new Data()
      data.setValue(x1)
      data.setLegendId(legendId)
      data.setX(xvalue)
      data.setxId(XaxisId)
      data.setLegend("建筑结构类型")

      dao.putToTableData(conn, data)
    }
    //封装searchBean
    val search = new Search()
    search.setName("建筑结构类型统计")
    search.setDimGroupName("建筑结构类型")
    search.setReportId(reportId)

    dao.putToTablesearch(conn, search)


  }

}
