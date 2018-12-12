package com.tecode.house.azxl.test

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class Person {

  def getvalue():Map[String,Int] ={

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER")))

    ).map(_.toInt).filter(_>0)

    var map:Map[String,Int]=Map();

    val s2=s.map(i=>{


      if(i<2){
        ("1人",1)
      }else if(i<3){
        ("2人",1)
      }else if(i<4){
        ("3人",1)
      }else if(i<5){
        ("4人",1)
      }else if(i<6){
        ("5人",1)
      }else if(i<7){
        ("6人",1)
      }else{
        ("6人以上",1)
      }
    }).reduceByKey(_+_)

    val max=s.max()
    val min=s.min()

    val sum=s.sum()
    val count=s.count()
    val avg=sum/count
    println("最多人数"+max)
    println("最少人数"+min)
    println("总家庭数"+sum)
    println("计数"+count)
    println("平均人数"+avg)

    s2.collect().foreach(map+=((_)))

    map+=(("max",max))
    map+=(("min",min))
    map+=(("avg",avg.toInt))

    sc.stop()
    println("获得数据成功")
    return map

  }

}
