package com.tecode.house.liuhao.server.impl

import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/11/29.
  * 分析建筑结构类型
  */
class StructureType {

  /**
    * 读取hbase中数据
    * @param tablename
    * @param qualifiername
    * @return
    */
  def read(tablename: String, qualifiername: String): RDD[(Int)] = {
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = new SparkContext(con)
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "INFO")
    //读取hbase中数据并转换为rdd
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]).map(_._2)
    val rdd1 = hbaseRDD.map(x => {
      val rawCells: Array[Cell] = x.rawCells()
      var value = 0
      for (elem <- rawCells) {
        if (CellUtil.cloneQualifier(elem).toString.equals(qualifiername)) {
          value = CellUtil.cloneValue(elem).toString.toInt
        }
      }
      value
    })
  rdd1
  }

  /**
    * 分析建筑建构类型的数据
    * @param rdd 传入的数据
    * @return
    */
  def analyStructureType(rdd:RDD[Int]):RDD[(String,Int)] = {
      val value = rdd.map(x=>(x,1)).reduceByKey(_+_)
      val result = value.map(x=>{
        if(x._1 == 1){
          ("独栋建筑",x._2)
        }else if (x._1 ==2){
          ("2-4户建筑",x._2)
        }else if (x._1 ==3){
          ("5-19户建筑",x._2)
        }else if (x._1 ==4){
          ("20-49户建筑",x._2)
        }else if (x._1 ==5){
          ("50户以上建筑",x._2)
        } else{
          ("移动建筑",x._2)
        }

      })
  result
  }


}
