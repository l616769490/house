package com.tecode.house.zouchao.serivce.impl



import com.tecode.house.d01.service.Analysis

import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


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
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    val rentsRDD: RDD[Int] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0;
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
//      println(value)
      value




    })
    sc.makeRDD(List(1,2,3));
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
    val value: RDD[(String, Int)] = rents.reduceByKey(_ + _)
    val max: Int = rentsRDD.max()
    val min: Int = rentsRDD.min()
    val count: Long = rentsRDD.count()
    val sum: Double = rentsRDD.sum()
    val avg: Double = sum / count
    println("max: " + max)
    println("min: " + min)
    println("avg: " + avg)
    value.collect().foreach(println)
    true
  }
}
