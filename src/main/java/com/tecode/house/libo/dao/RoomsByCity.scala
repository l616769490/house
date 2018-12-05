package com.tecode.house.libo.dao

import java.util

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

object RoomsByCity extends Analysis{

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("PriceByBuildAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val value: RDD[(Int, Int)] = read("2013","ROOMS",sc)
    calculation(value)
  }
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("PriceByBuildAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val roomsRDD: RDD[(Int, Int)] = read(tableName,"ROOMS",sc)
    val bedrmSRDD: RDD[(Int, Int)] = read(tableName,"BEDRMS",sc)
    //调用方法   求值
    //总房间数
    val rooms: RDD[(String, Double)] = calculation(roomsRDD)
    println("z总房间数=====")
    rooms.collect().foreach(println)

    val roomsbuffer: mutable.Buffer[(String, Double)] = rooms.collect().toBuffer
    val tuples: util.List[(String, Double)] = scala.collection.JavaConversions.bufferAsJavaList(roomsbuffer)


    //    卧室数
    val bedrms: RDD[(String, Double)] = calculation(bedrmSRDD)


    val bedrmsbuffer: mutable.Buffer[(String, Double)] = bedrms.collect().toBuffer
    val bedrmslist: util.List[(String, Double)] = scala.collection.JavaConversions.bufferAsJavaList(bedrmsbuffer)


    sc.stop()
    true
  }

  /**
    * 按照城市规模统计房间数，卧室数
    */
  def read(tableName:String,qualifiername:String,sc:SparkContext):RDD[(Int,Int)]={
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v = valuess.map(x =>x._2)
    val rentsRDD: RDD[(Int, Int)] = v.map(x=>{
      val cells = x.rawCells()
      var value: Int = 0
      var city: Int = 0
      //        获取传入的列的值
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        //        获城市规模
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          city = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      (city,value)
    })
    //    去除无效数据（value小于0的数据）
    val value: RDD[(Int, Int)] = rentsRDD.filter(_._2 > 0)
    //    value.collect().foreach(println)
    value

  }

  /**
    * 分析
    */
  def calculation(rdd: RDD[(Int, Int)]): RDD[(String, Double)]  ={
    val result: RDD[(String, Double)] =rdd.map(x=>{
      if(x._1==1){
        ("（一级城市）",x._2)
      }else if(x._1==2){
        ("（二级城市）",x._2)
      } else if(x._1==3){
        ("（三级城市）",x._2)
      }else if(x._1==4){
        ("（四级城市）",x._2)
      }else {
        ("（五级城市）", x._2)
      }
    })
    val value: RDD[(String, Double)] = result.reduceByKey(_+_)
    value
  }










}
