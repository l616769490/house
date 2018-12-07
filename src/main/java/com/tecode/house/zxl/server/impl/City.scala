package com.tecode.house.zxl.server.impl

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class City {
  def getvalue():Map[String,Int] ={


    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,"2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map( x =>(
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._2.toInt>0).groupByKey()

    var map:Map[String,Int]=Map();

    val s2=s.flatMap(x => {
      val a=x._2.iterator
      var list:List[(String,Int)]=List()
      while(a.hasNext){
        list:+=(x._1,a.next())
      }
      list
    })

    val s3=s2.map(x=>{
      var i=x._2.toInt
      if(i<50000){
        (x._1+"_10万以下",1)
      }else if(i<300000){
        (x._1+"_10万-30万",1)
      }else if(i<500000){
        (x._1+"_30万-50万",1)
      }else{
        (x._1+"_50万以上",1)
      }
    }
    )


    val s4=s3.reduceByKey(_+_)
//    s.collect().foreach(println)
//    println("--------------------------------------")
    s2.map(x=>(x._1,x._2.toInt)).reduceByKey(math.max(_,_)).collect().foreach(x=>map+=((x._1+"_max",x._2)))

//    println("--------------------------------------")

    s2.map(x=>(x._1,x._2.toInt)).reduceByKey(math.min(_,_)).collect().foreach(x=>map+=((x._1+"_min",x._2)))

//    println("--------------------------------------")

    s.map(x=>(x._1,x._2.toList.sum/x._2.toList.size)).collect().foreach(x=>map+=((x._1+"_avg",x._2.toInt)))



//    println("--------------------------------------")

    //  s2.collect().foreach(println)

    s4.collect().foreach(map+=((_)))
    sc.stop()
    println("读取数据完毕")
    return map;
  }
}
