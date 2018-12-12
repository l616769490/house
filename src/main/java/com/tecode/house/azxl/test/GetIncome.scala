package com.tecode.house.azxl.test

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class GetIncome {
  def getIncome(): Map[String,Iterable[(String,Int,Int)]] ={

      val conf = HBaseConfiguration.create();
      conf.set(TableInputFormat.INPUT_TABLE,"2013")
      conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


      val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
      val sc = new SparkContext(spconf)
      val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
      val s = hbaseRDD.map( x =>(
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

      )).filter(_._3.toInt>0)

      var map:Map[String,Iterable[(String,Int,Int)]]=Map();
      val s2=s.map(x=>{
        val i=x._3
        if(i<50000){
          ("5万以下",(x._1,x._2,x._3))
        }else if(i<100000){
          ("5万-10万",(x._1,x._2,x._3))
        }else if(i<150000){
          ("10万-15万",(x._1,x._2,x._3))
        }else if(i<200000){
          ("15万-20万",(x._1,x._2,x._3))
        }else if(i<250000){
          ("20万-25万",(x._1,x._2,x._3))
        }else{
          ("25万以上",(x._1,x._2,x._3))
        }
      }).groupByKey()
      s2.take(10).foreach(map+=((_)))

      sc.stop()

      println("获取数据完毕")
      return map

  }

  def getIncome(income:String): Map[String,Iterable[(String,Int,Int)]] ={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt>0)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<50000){
        ("5万以下",(x._1,x._2,x._3))
      }else if(i<100000){
        ("5万-10万",(x._1,x._2,x._3))
      }else if(i<150000){
        ("10万-15万",(x._1,x._2,x._3))
      }else if(i<200000){
        ("15万-20万",(x._1,x._2,x._3))
      }else if(i<250000){
        ("20万-25万",(x._1,x._2,x._3))
      }else{
        ("25万以上",(x._1,x._2,x._3))
      }
    }).filter(_._1.equals(income)).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

  def getIncome(city:Int): Map[String,Iterable[(String,Int,Int)]] ={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt>0).filter(_._2==city)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<50000){
        ("5万以下",(x._1,x._2,x._3))
      }else if(i<100000){
        ("5万-10万",(x._1,x._2,x._3))
      }else if(i<150000){
        ("10万-15万",(x._1,x._2,x._3))
      }else if(i<200000){
        ("15万-20万",(x._1,x._2,x._3))
      }else if(i<250000){
        ("20万-25万",(x._1,x._2,x._3))
      }else{
        ("25万以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

  def getIncome(income:String,city:Int): Map[String,Iterable[(String,Int,Int)]] ={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt>0).filter(_._2==city)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<50000){
        ("5万以下",(x._1,x._2,x._3))
      }else if(i<100000){
        ("5万-10万",(x._1,x._2,x._3))
      }else if(i<150000){
        ("10万-15万",(x._1,x._2,x._3))
      }else if(i<200000){
        ("15万-20万",(x._1,x._2,x._3))
      }else if(i<250000){
        ("20万-25万",(x._1,x._2,x._3))
      }else{
        ("25万以上",(x._1,x._2,x._3))
      }
    }).filter(_._1.equals(income)).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

}
