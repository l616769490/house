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
  //val count = hBaseRDD.count()
  //println(count)

    /**
      * 获取家庭人数数据
      */
   val PERRDD = hBaseRDD.map{case (_,result) =>{
    //获取行键
    //val key = Bytes.toString(result.getRow)
    //通过列族和列名获取列
     Bytes.toString(result.getValue("info".getBytes,"PER".getBytes))
   }}.map(x=>(x,1)).reduceByKey(_+_).groupBy(_._1).foreach(println)

    /**
      * 获取房产税数据，按建成年份划分
      */
    val RateRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      Bytes.toString(result.getValue("info".getBytes,"ZSMHC".getBytes))
    }


    /**
      * 获取独栋比例数据，按照区域划分
      */
    val SingleRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      Bytes.toString(result.getValue("info".getBytes,"NUNITS".getBytes))
    }




  sc.stop()



} }
