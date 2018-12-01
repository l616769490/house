package com.tecode.house.liuhao.server

import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/11/29.
  */
object HbaseData {
  def main(args: Array[String]): Unit = {
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = new SparkContext(con)
    val tablename = "2011"
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE,tablename)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"FMR")
    conf.set(TableInputFormat.SCAN_COLUMNS,"INFO")
    /**
      * 读取hbase中数据并转换为rdd
      */
    val hbaseRDD = sc.newAPIHadoopRDD(conf,classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]).map(_._2)
    hbaseRDD.foreach(x=>{
      val rawCells: Array[Cell] = x.rawCells()
      for (elem <- rawCells) {
        val name = Bytes.toString(CellUtil.cloneQualifier(elem))
        val string = Bytes.toString(CellUtil.cloneValue(elem))
        println("Lie:"+name +"    "+"value"+string)
      }
    })







  }

}
