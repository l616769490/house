package com.tecode.house.zxl.test

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class Value {

  def getvalue():Map[String,Int] ={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")
    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

    var map:Map[String,Int]=Map();

    val s = hbaseRDD.map( x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE")))

    ).map(_.toInt).filter(_>1)

    val s2=s.map(i=>{

      if(i<500000){
        ("0-50万",1)
      }else if(i<1000000){
        ("50万-100万",1)
      }else if(i<1500000){
        ("100万-150万",1)
      }else if(i<2000000){
        ("150万-200万",1)
      }else if(i<2500000){
        ("200万-250万",1)
      }else{
        ("250万以上",1)
      }
    }).reduceByKey(_+_)

    val max=s.max()
    val min=s.min()

    val sum=s.sum()
    val count=s.count()
    val avg=sum/count

    s2.collect().foreach(x=>map+=((x._1,x._2)))

    map+=(("max",max))
    map+=(("min",min))
    map+=(("avg",avg.toInt))
    sc.stop()
    return map

  }

}
