package com.tecode.house.jianchenfei.hbasedata


import com.tecode.house.jianchenfei.utils.{ConfigUtil, HBaseUtil}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark._
import org.apache.hadoop.io._
/**
  * Created by Administrator on 2018/11/29.
  */
object BasicPeople {
  def main(args: Array[String]): Unit = {


  val conf = new SparkConf().setAppName("People").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val hbaseconf = HBaseConfiguration.create()
  val tablename = "thads:2013"
//  hbaseconf.set("hbase.zookeeper.quorum","hadoop002,hadoop003,hadoop004")
//  hbaseconf.set("hbase.zookeeper.property.clientPort","2181")
  hbaseconf.set(TableInputFormat.INPUT_TABLE,tablename)



  //读取数据并转化成rdd
  val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
    classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
    classOf[org.apache.hadoop.hbase.client.Result])
  val count = hBaseRDD.count()
  println(count)
  hBaseRDD.foreach{case (_,result) =>{
    //获取行键
    val key = Bytes.toString(result.getRow)


    println("Row key:"+key)
  }}
  sc.stop()



} }
