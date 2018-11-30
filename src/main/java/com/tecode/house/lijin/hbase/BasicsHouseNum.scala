package com.tecode.house.lijin.hbase

import com.tecode.house.d01.bean.HBaseBean
import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.util.Bytes

/**
  * 基础-房间数
  */
class BasicsHouseNum extends Analysis {
  private val conf = new SparkConf().setAppName("BasicsHouseNum").setMaster("local[*]")
  private val sc = new SparkContext(conf)
  private val hBaseConf = HBaseConfiguration.create

  /**
    * 读取HBase数据
    *
    * @param tableName HBase的表名
    * @return HBaseRDD
    */
  def readHBase(tableName: String): RDD[(ImmutableBytesWritable, Result)] = {
    hBaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
    val scan = new Scan()
    scan.setCacheBlocks(false)
    // 设置读取的列族
    scan.addFamily(Bytes.toBytes("info"))
    // 卧室数
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BEDRMS"))
    // 房间数
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ROOMS"))
    // 将scan类转化成string类型  
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    // 读取数据
    val hbaseRDD = readHBase(tableName).map(
      row => {
        var BEDRMS, ROOMS = 0

        for (cell <- row._2.rawCells()) {
          // 列族
          // val family = Bytes.toString(CellUtil.cloneFamily(cell))
          // 列名
          val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
          // 值
          val value = Bytes.toString(CellUtil.cloneValue(cell))
          if ("BEDRMS".equals(qualifier)) {
            BEDRMS = Integer.parseInt(value)
          } else if ("ROOMS".equals(qualifier)) {
            ROOMS = Integer.parseInt(value)
          }
        }
        (BEDRMS, ROOMS)
      }

    )
    hbaseRDD.foreach(s => println(s._1.getClass))

    // 卧室数
    val bedrmsRDD = hbaseRDD.map(_._1)

    // 房间数
    val roomsRDD = hbaseRDD.map(_._2)

    bedrmsRDD.map((_, 1)).reduceByKey(_ + _).glom().foreach(s => s.foreach(println))
    println("-----------------------")
    roomsRDD.map((_, 1)).reduceByKey(_ + _).glom().foreach(s => s.foreach(println))

    true
  }


}

object BasicsHouseNum {
  def main(args: Array[String]): Unit = {
    val basicsHouseNum = new BasicsHouseNum()
    basicsHouseNum.analysis("thads:2013")
  }
}
