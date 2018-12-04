package com.tecode.zxl.init

import com.tecode.zxl.init.t.{conf, hbaseRDD, sc}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration, TableName}
import org.apache.spark.{SparkConf, SparkContext}
import org.hibernate.validator.internal.util.CollectionHelper.Partitioner

object Test extends App {


  val conf = HBaseConfiguration.create();
  conf.set(TableInputFormat.INPUT_TABLE,"2013")
  conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")
  val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
  val sc = new SparkContext(spconf)

//  val spsql = SparkSession.builder().config(spconf).getOrCreate()

//  import spsql.implicits._

  val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

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
    }else if(i<3000000){
      ("250万-300万",1)
    }else{
      ("300万以上",1)
    }
  }).reduceByKey(_+_)

  val max=s.max()
  val min=s.min()

  val sum=s.sum()
  val count=s.count()
  val avg=sum/count
  println("最高房价"+max)
  println("最低房价"+min)
  println("总价"+sum)
  println("计数"+count)
  println("平均"+avg)

  s2.collect().foreach(println)

  sc.stop()


}


object t extends App {

  val conf = HBaseConfiguration.create();
  conf.set(TableInputFormat.INPUT_TABLE,"2011")
  conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")

  val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
  val sc = new SparkContext(spconf)

  //  val spsql = SparkSession.builder().config(spconf).getOrCreate()

  //  import spsql.implicits._

  val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

  val s = hbaseRDD.map( x =>

    Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE")))

  ).map(_.toInt).filter(_>5)

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
    }else if(i<3000000){
      ("250万-300万",1)
    }else{
      ("300万以上",1)
    }
  }).reduceByKey(_+_)

  val max=s.max()
  val min=s.min()

  val sum=s.sum()
  val count=s.count()
  val avg=sum/count
  println("最高房价"+max)
  println("最低房价"+min)
  println("总价"+sum)
  println("计数"+count)
  println("平均"+avg)

  s2.collect().foreach(println)

  sc.stop()

}

object person extends App{
  val conf = HBaseConfiguration.create();
  conf.set(TableInputFormat.INPUT_TABLE,"2013")
  conf.set(TableInputFormat.SCAN_COLUMN_FAMILY,"info")

  val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
  val sc = new SparkContext(spconf)
  val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  val s = hbaseRDD.map( x =>

    Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER")))

  ).map(_.toInt).filter(_>0)

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

  s2.collect().foreach(println)
  sc.stop()
}

object city extends App{

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
      (x._1+"_5万以下",1)
    }else if(i<100000){
      (x._1+"_5万-10万",1)
    }else if(i<200000){
      (x._1+"_10万-20万",1)
    }else if(i<300000){
      (x._1+"_20万-30万",1)
    }else if(i<400000){
      (x._1+"_30万-40万",1)
    }else if(i<500000){
      (x._1+"_40万-50万",1)
    }else{
      (x._1+"_50万以上",1)
    }
  }
  )


  val s4=s3.reduceByKey(_+_)
  s.collect().foreach(println)
  println("--------------------------------------")
  s2.map(x=>(x._1,x._2.toInt)).reduceByKey(math.max(_,_)).collect().foreach(println)

  println("--------------------------------------")

  s2.map(x=>(x._1,x._2.toInt)).reduceByKey(math.min(_,_)).collect().foreach(println)

  println("--------------------------------------")

  s.map(x=>(x._1,x._2.toList.sum/x._2.toList.size)).collect().foreach(println)



println("--------------------------------------")

//  s2.collect().foreach(println)

  s4.collect().foreach(println)
  sc.stop()

}