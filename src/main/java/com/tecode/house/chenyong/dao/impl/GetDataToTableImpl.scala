package com.tecode.house.chenyong.dao.impl

import java.util

import com.tecode.house.chenyong.dao.GetDataToTable
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.springframework.web.bind.annotation.ResponseBody

import scala.collection.JavaConverters._

/**
  * Created by Administrator on 2018/12/7.
  */
@ResponseBody
class GetDataToTableImpl extends GetDataToTable{
  /**
    *
    * @param tableName   表名
    * @param ageInterval 查询的年龄区间
    * @param page        查询页数
    * @return 查询的总数，查询的数据
    */
  override def getDataToAge(tableName: String, ageInterval: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val res: RDD[Result] = param(tableName,ageInterval,page)
    //取出需要展示在表格中的各列的值
    val rows:RDD[util.ArrayList[String]] = res.map(x => {
      val rawCells: Array[Cell] = x.rawCells()
      var CONTROL: String = null
      var TYPE: String = null
      var ZINC2: String = null
      var ZSMHC: String = null
      var AGE1: String = null
      val arrList = new util.ArrayList[String]()
      for (elem <- rawCells) {
        val col:String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val rs: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (col.equals("CONTROL")) {
          CONTROL = rs
        }
        if (col.equals("TYPE")) {
          TYPE = rs
        }
        if (col.equals("ZINC2")) {
          ZINC2 = rs
        }
        if (col.equals("ZSMHC")) {
          ZSMHC = rs
        }
        if (col.equals("AGE1")) {
          AGE1 = rs
        }
      }
      arrList.add(CONTROL)
      arrList.add(TYPE)
      arrList.add(ZINC2)
      arrList.add(ZSMHC)
      arrList.add(AGE1)
      arrList
    })
    //将获得的数组集合转换为list集合
    val slist:List[util.ArrayList[String]] = rows.collect().toList
    //转换为java型的list
    val jlist:util.List[util.ArrayList[String]] = slist.asJava

    var fin:util.List[util.ArrayList[String]] = null
    //获取数据的总数量
    val sumCount = jlist.size()
    //获取需要查询的页码的数据
    if(page*20 > sumCount){
      fin = jlist.subList((page-1)*20, sumCount)
    }else{
      fin = jlist.subList((page-1)*20, page*20)
    }
    (sumCount,fin)
  }

  /**
    *
    * @param tableName   表名
    * @param ageInterval 查询的年龄区间
    * @param page        查询页数
    * @return 查询的总数，查询的数据
    */
  override def getDataToRooms(tableName: String, ageInterval: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val res: RDD[Result] = param(tableName,ageInterval,page)
    //取出需要展示在表格中的各列的值
    val rows:RDD[util.ArrayList[String]] = res.map(x => {
      val rawCells: Array[Cell] = x.rawCells()
      var CONTROL: String = null
      var AGE1: String = null
      var VALUE: String = null
      var ROOMS: String = null
      var BEDRMS: String = null
      var PER: String = null
      val arrList = new util.ArrayList[String]()
      for (elem <- rawCells) {
        val col:String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val rs: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (col.equals("CONTROL")) {
          CONTROL = rs
        }
        if (col.equals("VALUE")) {
          VALUE = rs
        }
        if (col.equals("ROOMS")) {
          ROOMS = rs
        }
        if (col.equals("BEDRMS")) {
          BEDRMS = rs
        }
        if (col.equals("AGE1")) {
          AGE1 = rs
        }
        if (col.equals("PER")) {
          PER = rs
        }
      }
      arrList.add(CONTROL)
      arrList.add(AGE1)
      arrList.add(VALUE)
      arrList.add(ROOMS)
      arrList.add(BEDRMS)
      arrList.add(PER)
      arrList
    })
    //将获得的数组集合转换为list集合
    val slist:List[util.ArrayList[String]] = rows.collect().toList
    //转换为java型的list
    val jlist:util.List[util.ArrayList[String]] = slist.asJava

    var fin:util.List[util.ArrayList[String]] = null
    //获取数据的总数量
    val sumCount = jlist.size()
    //获取需要查询的页码的数据
    if(page*20 > sumCount){
      fin = jlist.subList((page-1)*20, sumCount)
    }else{
      fin = jlist.subList((page-1)*20, page*20)
    }
    (sumCount,fin)
  }
  /**
    *
    * @param tableName   表名
    * @param ageInterval 查询的年龄区间
    * @param page        查询页数
    * @return 查询的总数，查询的数据
    */
  override def getDataToUtility(tableName: String, ageInterval: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val res: RDD[Result] = param(tableName,ageInterval,page)
    //取出需要展示在表格中的各列的值
    val rows:RDD[util.ArrayList[String]] = res.map(x => {
      val rawCells: Array[Cell] = x.rawCells()
      var CONTROL: String = null
      var AGE1: String = null
      var UTILITY: String = null
      var PER: String = null

      val arrList = new util.ArrayList[String]()
      for (elem <- rawCells) {
        val col:String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val rs: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (col.equals("CONTROL")) {
          CONTROL = rs
        }
        if (col.equals("AGE1")) {
          AGE1 = rs
        }
        if (col.equals("UTILITY")) {
          UTILITY = rs
        }
        if (col.equals("PER")) {
          PER = rs
        }
      }
      arrList.add(CONTROL)
      arrList.add(AGE1)
      arrList.add(UTILITY)
      arrList.add(PER)
      arrList
    })
    //将获得的数组集合转换为list集合
    val slist:List[util.ArrayList[String]] = rows.collect().toList
    //转换为java型的list
    val jlist:util.List[util.ArrayList[String]] = slist.asJava
    System.out.println(jlist.size()+"数据长度")
    var fin:util.List[util.ArrayList[String]] = null
    //获取数据的总数量
    val sumCount = jlist.size()
    //获取需要查询的页码的数据
    if(page*10 > sumCount){
      fin = jlist.subList((page-1)*20, sumCount)
    }else{
      fin = jlist.subList((page-1)*20, page*20)
    }
    (sumCount,fin)
  }
  def param(tableName: String, ageInterval: String, page: Int):RDD[Result] = {
    val conf = new SparkConf().setAppName("getDataToTable").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //配置HBase参数
    val hconf = HBaseConfiguration.create()
    //设置表名
    hconf.set(TableInputFormat.INPUT_TABLE,tableName)
    //设置列族
    hconf.set(TableInputFormat.SCAN_COLUMNS,"info")
    //获取数据
    val datas:RDD[(ImmutableBytesWritable,Result)] = sc.newAPIHadoopRDD(hconf,classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
    val data:RDD[Result] = datas.map(x => x._2)
    //根据结果将其转换成(年龄区间，Result)
    val result:RDD[(String,Result)] = data.map(x => {

      val ageCells: Array[Cell] = x.rawCells()

      //声明获取年龄值得变量
      var age:Int = 0

      for (elem <- ageCells) {
        //取出各列
        val column:String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value:String = Bytes.toString(CellUtil.cloneValue(elem))
        if (column.equals("AGE1")){
          age = Integer.parseInt(value)
        }
      }
      //将具体得到的数据封装成元组(年龄区间，Result)
      if(age < 18){
        ("18以下",x)
      }else if (age >= 18 && age <= 40){
        ("18-40",x)
      }else if(age >= 41 && age < 65){
        ("41-65",x)
      }else{
        ("65以上",x)
      }
    })
    //筛选满足查询条件的数据
    val res:RDD[Result] = result.filter({
      case (x:String, y:Result) => {
        x.equals(ageInterval)
      }
    }).map(_._2)
    res
  }
}
