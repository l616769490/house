package com.tecode.house.liuhao.dao.impl

import scala.collection.JavaConverters._
import java.util

import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.house.liuhao.dao.ReadHbaseDao
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/6.
  */
class ReadHbaseDaoImpl extends  ReadHbaseDao{
  /**
    * 无条件查询
    * @param tablename 表名
    * @param page 查询页数
    * @param arr  查看的列的集合
    * @return
    */
  override def readData(tablename:String,page:Int,arr:String):(Integer,util.List[util.ArrayList[String]])= {
//    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //读取hbase中数据并转换为rdd
    val listrow:RDD[util.ArrayList[String]] = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]).map(x=>{
      val list = new util.ArrayList[String]()

      val value = (Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("STRUCTURETYPE"))),
        (Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3")))),
          Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))))
      list.add(value._1)
      list.add(value._2)
      list.add(value._3)

      list
    })


    //将rdd转换成scala的list
    val scalalist: List[util.ArrayList[String]] = listrow.collect().toList
    //将scala的list转换成Javalist
    val javalist: util.List[util.ArrayList[String]] = scalalist.asJava
    var showdata:util.List[util.ArrayList[String]] = null
    val size = javalist.size()

    if(page*10>size){
       showdata =javalist.subList((page-1)*10,size)
    }else{
      showdata = javalist.subList((page-1)*10,page*10)
    }
    (size,showdata)

  }

  /**
    * 按各线城市划分查询
    * @param tablename 表名
    * @param page 查看的页数
    * @param city 城市等级
    * @return
    */
  override def readCityTaxData(tablename:String,page:Int,city:String):(Integer,util.List[util.ArrayList[String]])= {
    print(city)
    var citys =
      if(city.equals("1")){
      1
    }else if(city.equals("2")){
      2
    }else if(city.equals("3")) {
      3
    }else if(city.equals("4")) {
      4
    }else {
      5
    }
    val con = new SparkConf().setAppName("hbase").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tablename)
    conf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //读取hbase中数据并转换为rdd

    val listrow = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]).map(x =>{
      val list = new util.ArrayList[String]()
      val value = (Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))),
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))),
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("UTILITY"))),
        Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("OTHERCOST"))
        ))
      //if(value._1.equals(citys.toString)){
        list.add(value._1)
        list.add(value._2)
        list.add(value._3)
        list.add(value._4)
     // }

      list
    })

    //将rdd转换成scala的list
    val scalalist: List[util.ArrayList[String]] = listrow.collect().toList
    //将scala的list转换成Javalist
    val javalist: util.List[util.ArrayList[String]] = scalalist.asJava

    var showdata:util.List[util.ArrayList[String]] = null
    val size = javalist.size()



    if(page*10>size){
      showdata =javalist.subList((page-1)*10,size)
    }else{
      showdata = javalist.subList((page-1)*10,page*10)
    }

    (size,showdata)

  }


}
