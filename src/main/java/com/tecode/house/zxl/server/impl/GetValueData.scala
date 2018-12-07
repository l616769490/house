package com.tecode.house.zxl.server.impl


import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}


class GetData {

  def getData():Map[String,Iterable[(String,Int,Int)]]={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE"))).toInt

    )).filter(_._3.toInt>0)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<500000){
        ("50万以下",(x._1,x._2,x._3))
      }else if(i<1000000){
        ("50万-100万",(x._1,x._2,x._3))
      }else if(i<1500000){
        ("100万-150万",(x._1,x._2,x._3))
      }else if(i<2000000){
        ("150万-200万",(x._1,x._2,x._3))
      }else if(i<2500000){
        ("200万-250万",(x._1,x._2,x._3))
      }else{
        ("250万以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }


  def getData(str:String):Map[String,Iterable[(String,Int,Int)]]={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE"))).toInt

    )).filter(_._3.toInt>0)

    var map:Map[String,Iterable[(String,Int,Int)]]=Map();
    val s2=s.map(x=>{
      val i=x._3
      if(i<500000){
        ("50万以下",(x._1,x._2,x._3))
      }else if(i<1000000){
        ("50万-100万",(x._1,x._2,x._3))
      }else if(i<1500000){
        ("100万-150万",(x._1,x._2,x._3))
      }else if(i<2000000){
        ("150万-200万",(x._1,x._2,x._3))
      }else if(i<2500000){
        ("200万-250万",(x._1,x._2,x._3))
      }else{
        ("250万以上",(x._1,x._2,x._3))
      }
    }).groupByKey()
    s2.filter(_._1.equals(str)).collect().foreach(map+=((_)))

    sc.stop()

    println("获取数据完毕")
    return map

  }

}

object t extends App{
  val a=new GetData();

  a.getData().foreach(x=>{
    var it=x._2.iterator
    while(it.hasNext){
      println(x._1+"=="+it.next())
    }

  })
}
