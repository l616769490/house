package com.tecode.house.zxl.server.impl

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class GetPerson {
  def getData():Map[String,Iterable[(String,Int,Int)]]= {

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt>0)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<2){
        ("1人",(x._1,x._2,x._3))
      }else if(i<3){
        ("2人",(x._1,x._2,x._3))
      }else if(i<4){
        ("3人",(x._1,x._2,x._3))
      }else if(i<5){
        ("4人",(x._1,x._2,x._3))
      }else if(i<6){
        ("5人",(x._1,x._2,x._3))
      }else if(i<7){
        ("6人",(x._1,x._2,x._3))
      }else{
        ("6人以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }


  def getData(person:String):Map[String,Iterable[(String,Int,Int)]]= {

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt>0)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<2){
        ("1人",(x._1,x._2,x._3))
      }else if(i<3){
        ("2人",(x._1,x._2,x._3))
      }else if(i<4){
        ("3人",(x._1,x._2,x._3))
      }else if(i<5){
        ("4人",(x._1,x._2,x._3))
      }else if(i<6){
        ("5人",(x._1,x._2,x._3))
      }else if(i<7){
        ("6人",(x._1,x._2,x._3))
      }else{
        ("6人以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.filter(_._1.equals(person)).collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

  def getData(c:Int):Map[String,Iterable[(String,Int,Int)]]= {

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt>0).filter(c==_._2)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<2){
        ("1人",(x._1,x._2,x._3))
      }else if(i<3){
        ("2人",(x._1,x._2,x._3))
      }else if(i<4){
        ("3人",(x._1,x._2,x._3))
      }else if(i<5){
        ("4人",(x._1,x._2,x._3))
      }else if(i<6){
        ("5人",(x._1,x._2,x._3))
      }else if(i<7){
        ("6人",(x._1,x._2,x._3))
      }else{
        ("6人以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

  def getData(city:Int,person:String):Map[String,Iterable[(String,Int,Int)]]= {

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt>0).filter(_._2==city)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<2){
        ("1人",(x._1,x._2,x._3))
      }else if(i<3){
        ("2人",(x._1,x._2,x._3))
      }else if(i<4){
        ("3人",(x._1,x._2,x._3))
      }else if(i<5){
        ("4人",(x._1,x._2,x._3))
      }else if(i<6){
        ("5人",(x._1,x._2,x._3))
      }else if(i<7){
        ("6人",(x._1,x._2,x._3))
      }else{
        ("6人以上",(x._1,x._2,x._3))
      }

    }).groupByKey()
    s2.filter(_._1.equals(person)).collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

}
