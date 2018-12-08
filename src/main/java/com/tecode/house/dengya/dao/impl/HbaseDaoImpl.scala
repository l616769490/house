package com.tecode.house.dengya.dao.impl

import java.util

import com.tecode.house.dengya.dao.HbaseDao
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import scala.collection.JavaConverters._
import org.springframework.stereotype.Repository
@Repository
class HbaseDaoImpl extends HbaseDao {
  /**
    * 查询建筑单元数报表的所有数据（没有过滤条件）
    *
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getAllForUnits(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val value: RDD[Result] = valuess.map(_._2)
    val valueRDD:RDD[util.ArrayList[String]] = value.map(x =>{
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //建成年份
      var BUILT:String = null
      //建筑单元书
      var NUNITS:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("NUNITS"))
          NUNITS = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(BUILT)
      list.add(NUNITS)
      list
    })
    //将RDD装换成scala的list
    val list: List[util.ArrayList[String]] = valueRDD.take(page * 100).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size();
    var rows: util.List[util.ArrayList[String]] = java.subList(count - 10,count);
    sc.stop()
    (count,rows)
  }

  /**
    * 根据过滤条件查询建筑单元数报表的数据（按建筑单元数过滤）
    *
    * @param tableName 表名
    * @param units     过滤条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getForUnits(tableName: String, units: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForUnits").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var NUNITS:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("NUNITS")) {
          NUNITS = Integer.parseInt(vals);
        }
      }
      if(NUNITS == 1 ){
        ("singleFamliy",x)
      }else if(NUNITS < 200){
        ("2-200",x)
      }else if(NUNITS <400){
        ("200-400",x)
      }else if(NUNITS < 600){
        ("400-600",x)
      }else if(NUNITS < 800){
        ("600-800",x)
      }else{
        ("800+",x)
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,y:Result) => x.equals(units)
    }).map(_._2)
    //va.foreach(println)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
     val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //建成年份
      var BUILT:String = null
      //建筑单元书
      var NUNITS:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val row: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = row.split("_")
        CONTROL = rowKey(1)
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
          if(str.equals("METRO3")){
            METRO3=value
          }
        if(str.equals("BUILT")){
          BUILT = value
        }
        if(str.equals("NUNITS")){
          NUNITS = value
        }
      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(BUILT)
      list.add(NUNITS)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
   println(list.size)
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)

  }

  /**
    * 按城市规模统计价格查询所有数据(不过滤条件）
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getAllForValue(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForValue").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val value: RDD[Result] = valuess.map(_._2)
    val valueRDD:RDD[util.ArrayList[String]] = value.map(x =>{
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list
    })
    //将RDD装换成scala的list
    val list: List[util.ArrayList[String]] = valueRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size();
    var rows: util.List[util.ArrayList[String]] = null;
    //判断是否为最后一页
    if(page * 10 > count){
      rows = java.subList((page -1) * 10,count)
    } else{
      rows = java.subList((page - 1)*10,page * 10)
    }
    sc.stop()
    (count,rows)
  }

  /**
    * 按城市规模统计价格过滤查询数据（三个条件都过滤）
    *
    * @param tableName 表名
    * @param rent      租金区间条件
    * @param city      城市规模条件
    * @param price     售价
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getForValue(tableName: String, city: String, rent: String, price: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValue").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    //根据城市等级，租金，价格将result转换成为（城市等级，租金，价格，Result）的RDD
    val vvv: RDD[(String, String,String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var city:Int = 0;
      var rent:Int = 0;
      var price:Int = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if(name.equals("METRO3")){
          city = Integer.parseInt(vals);
        }
        if(name.equals("FMR")){
          rent = Integer.parseInt(vals)
        }
        if(name.equals("VALUE")){
          price = Integer.valueOf(vals)
        }
      }
      //将具体的城市规模、租金及价格数据替换为（城市规模，租金，价格，Result）的元祖
      if(city == 1){
        if(rent < 1000){
          if(price < 50){
            ("一线城市","0-1000","0-50",x)
          }else if(price < 100){
            ("一线城市","0-1000","50-100",x)
          }else if(price < 150){
            ("一线城市","0-1000","100-150",x)
          }else if(price < 200){
            ("一线城市","0-1000","150-200",x)
          }else if(price < 250){
            ("一线城市","0-1000","200-250",x)
          }else {
            ("一线城市","0-1000","250-300",x)
          }
        }else if(rent < 1500){
          if(price < 50){
            ("一线城市","1000-1500","0-50",x)
          }else if(price < 100){
            ("一线城市","1000-1500","50-100",x)
          }else if(price < 150){
            ("一线城市","1000-1500","100-150",x)
          }else if(price < 200){
            ("一线城市","1000-1500","150-200",x)
          }else if(price < 250){
            ("一线城市","1000-1500","200-250",x)
          }else {
            ("一线城市","1000-1500","250-300",x)
          }
        }else if(rent < 2000){
          if(price < 50){
            ("一线城市","1500-2000","0-50",x)
          }else if(price < 100){
            ("一线城市","1500-2000","50-100",x)
          }else if(price < 150){
            ("一线城市","1500-2000","100-150",x)
          }else if(price < 200){
            ("一线城市","1500-2000","150-200",x)
          }else if(price < 250){
            ("一线城市","1500-2000","200-250",x)
          }else{
            ("一线城市","1500-2000","250-300",x)
          }
        }else if(rent < 2500){
          if(price < 50){
            ("一线城市","2000-2500","0-50",x)
          }else if(price < 100){
            ("一线城市","2000-2500","50-100",x)
          }else if(price < 150){
            ("一线城市","2000-2500","100-150",x)
          }else if(price < 200){
            ("一线城市","2000-2500","150-200",x)
          }else if(price < 250){
            ("一线城市","2000-2500","200-250",x)
          }else {
            ("一线城市","2000-2500","250-300",x)
          }
        }else if(rent < 3000){
          if(price < 50){
            ("一线城市","2500-3000","0-50",x)
          }else if(price < 100){
            ("一线城市","2500-3000","50-100",x)
          }else if(price < 150){
            ("一线城市","2500-3000","100-150",x)
          }else if(price < 200){
            ("一线城市","2500-3000","150-200",x)
          }else if(price < 250){
            ("一线城市","2500-3000","200-250",x)
          }else {
            ("一线城市","2500-3000","250-300",x)
          }
        }else {
          if(price < 50){
            ("一线城市","3000+","0-50",x)
          }else if(price < 100){
            ("一线城市","3000+","50-100",x)
          }else if(price < 150){
            ("一线城市","3000+","100-150",x)
          }else if(price < 200){
            ("一线城市","3000+","150-200",x)
          }else if(price < 250){
            ("一线城市","3000+","200-250",x)
          }else {
            ("一线城市","3000+","250-300",x)
          }
        }
      }else if(city == 2){
        if(rent < 1000){
          if(price < 50){
            ("二线城市","0-1000","0-50",x)
          }else if(price < 100){
            ("二线城市","0-1000","50-100",x)
          }else if(price < 150){
            ("二线城市","0-1000","100-150",x)
          }else if(price < 200){
            ("二线城市","0-1000","150-200",x)
          }else if(price < 250){
            ("二线城市","0-1000","200-250",x)
          }else {
            ("二线城市","0-1000","250-300",x)
          }
        }else if(rent < 1500){
          if(price < 50){
            ("二线城市","1000-1500","0-50",x)
          }else if(price < 100){
            ("二线城市","1000-1500","50-100",x)
          }else if(price < 150){
            ("二线城市","1000-1500","100-150",x)
          }else if(price < 200){
            ("二线城市","1000-1500","150-200",x)
          }else if(price < 250){
            ("二线城市","1000-1500","200-250",x)
          }else {
            ("二线城市","1000-1500","250-300",x)
          }
        }else if(rent < 2000){
          if(price < 50){
            ("二线城市","1500-2000","0-50",x)
          }else if(price < 100){
            ("二线城市","1500-2000","50-100",x)
          }else if(price < 150){
            ("二线城市","1500-2000","100-150",x)
          }else if(price < 200){
            ("二线城市","1500-2000","150-200",x)
          }else if(price < 250){
            ("二线城市","1500-2000","200-250",x)
          }else {
            ("二线城市","1500-2000","250-300",x)
          }
        }else if(rent < 2500){
          if(price < 50){
            ("二线城市","2000-2500","0-50",x)
          }else if(price < 100){
            ("二线城市","2000-2500","50-100",x)
          }else if(price < 150){
            ("二线城市","2000-2500","100-150",x)
          }else if(price < 200){
            ("二线城市","2000-2500","150-200",x)
          }else if(price < 250){
            ("二线城市","2000-2500","200-250",x)
          }else {
            ("二线城市","2000-2500","250-300",x)
          }
        }else if(rent < 3000){
          if(price < 50){
            ("二线城市","2500-3000","0-50",x)
          }else if(price < 100){
            ("二线城市","2500-3000","50-100",x)
          }else if(price < 150){
            ("二线城市","2500-3000","100-150",x)
          }else if(price < 200){
            ("二线城市","2500-3000","150-200",x)
          }else if(price < 250){
            ("二线城市","2500-3000","200-250",x)
          }else {
            ("二线城市","2500-3000","250-300",x)
          }
        }else {
          if(price < 50){
            ("二线城市","3000+","0-50",x)
          }else if(price < 100){
            ("二线城市","3000+","50-100",x)
          }else if(price < 150){
            ("二线城市","3000+","100-150",x)
          }else if(price < 200){
            ("二线城市","3000+","150-200",x)
          }else if(price < 250){
            ("二线城市","3000+","200-250",x)
          }else {
            ("二线城市","3000+","250-300",x)
          }
        }
      }else if(city == 3){
        if(rent < 1000){
          if(price < 50){
            ("三线城市","0-1000","0-50",x)
          }else if(price < 100){
            ("三线城市","0-1000","50-100",x)
          }else if(price < 150){
            ("三线城市","0-1000","100-150",x)
          }else if(price < 200){
            ("三线城市","0-1000","150-200",x)
          }else if(price < 250){
            ("三线城市","0-1000","200-250",x)
          }else {
            ("三线城市","0-1000","250-300",x)
          }
        }else if(rent < 1500){
          if(price < 50){
            ("三线城市","1000-1500","0-50",x)
          }else if(price < 100){
            ("三线城市","1000-1500","50-100",x)
          }else if(price < 150){
            ("三线城市","1000-1500","100-150",x)
          }else if(price < 200){
            ("三线城市","1000-1500","150-200",x)
          }else if(price < 250){
            ("三线城市","1000-1500","200-250",x)
          }else {
            ("三线城市","1000-1500","250-300",x)
          }
        }else if(rent < 2000){
          if(price < 50){
            ("三线城市","1500-2000","0-50",x)
          }else if(price < 100){
            ("三线城市","1500-2000","50-100",x)
          }else if(price < 150){
            ("三线城市","1500-2000","100-150",x)
          }else if(price < 200){
            ("三线城市","1500-2000","150-200",x)
          }else if(price < 250){
            ("三线城市","1500-2000","200-250",x)
          }else {
            ("三线城市","1500-2000","250-300",x)
          }
        }else if(rent < 2500){
          if(price < 50){
            ("三线城市","2000-2500","0-50",x)
          }else if(price < 100){
            ("三线城市","2000-2500","50-100",x)
          }else if(price < 150){
            ("三线城市","2000-2500","100-150",x)
          }else if(price < 200){
            ("三线城市","2000-2500","150-200",x)
          }else if(price < 250){
            ("三线城市","2000-2500","200-250",x)
          }else{
            ("三线城市","2000-2500","250-300",x)
          }
        }else if(rent < 3000){
          if(price < 50){
            ("三线城市","2500-3000","0-50",x)
          }else if(price < 100){
            ("三线城市","2500-3000","50-100",x)
          }else if(price < 150){
            ("三线城市","2500-3000","100-150",x)
          }else if(price < 200){
            ("三线城市","2500-3000","150-200",x)
          }else if(price < 250){
            ("三线城市","2500-3000","200-250",x)
          }else {
            ("三线城市","2500-3000","250-300",x)
          }
        }else {
          if(price < 50){
            ("三线城市","3000+","0-50",x)
          }else if(price < 100){
            ("三线城市","3000+","50-100",x)
          }else if(price < 150){
            ("三线城市","3000+","100-150",x)
          }else if(price < 200){
            ("三线城市","3000+","150-200",x)
          }else if(price < 250){
            ("三线城市","3000+","200-250",x)
          }else {
            ("三线城市","3000+","250-300",x)
          }
        }
      }else if(city == 4){
        if(rent < 1000){
          if(price < 50){
            ("四线城市","0-1000","0-50",x)
          }else if(price < 100){
            ("四线城市","0-1000","50-100",x)
          }else if(price < 150){
            ("四线城市","0-1000","100-150",x)
          }else if(price < 200){
            ("四线城市","0-1000","150-200",x)
          }else if(price < 250){
            ("四线城市","0-1000","200-250",x)
          }else {
            ("四线城市","0-1000","250-300",x)
          }
        }else if(rent < 1500){
          if(price < 50){
            ("四线城市","1000-1500","0-50",x)
          }else if(price < 100){
            ("四线城市","1000-1500","50-100",x)
          }else if(price < 150){
            ("四线城市","1000-1500","100-150",x)
          }else if(price < 200){
            ("四线城市","1000-1500","150-200",x)
          }else if(price < 250){
            ("四线城市","1000-1500","200-250",x)
          }else{
            ("四线城市","1000-1500","250-300",x)
          }
        }else if(rent < 2000){
          if(price < 50){
            ("四线城市","1500-2000","0-50",x)
          }else if(price < 100){
            ("四线城市","1500-2000","50-100",x)
          }else if(price < 150){
            ("四线城市","1500-2000","100-150",x)
          }else if(price < 200){
            ("四线城市","1500-2000","150-200",x)
          }else if(price < 250){
            ("四线城市","1500-2000","200-250",x)
          }else {
            ("四线城市","1500-2000","250-300",x)
          }
        }else if(rent < 2500){
          if(price < 50){
            ("四线城市","2000-2500","0-50",x)
          }else if(price < 100){
            ("四线城市","2000-2500","50-100",x)
          }else if(price < 150){
            ("四线城市","2000-2500","100-150",x)
          }else if(price < 200){
            ("四线城市","2000-2500","150-200",x)
          }else if(price < 250){
            ("四线城市","2000-2500","200-250",x)
          }else {
            ("四线城市","2000-2500","250-300",x)
          }
        }else if(rent < 3000){
          if(price < 50){
            ("四线城市","2500-3000","0-50",x)
          }else if(price < 100){
            ("四线城市","2500-3000","50-100",x)
          }else if(price < 150){
            ("四线城市","2500-3000","100-150",x)
          }else if(price < 200){
            ("四线城市","2500-3000","150-200",x)
          }else if(price < 250){
            ("四线城市","2500-3000","200-250",x)
          }else {
            ("四线城市","2500-3000","250-300",x)
          }
        }else {
          if(price < 50){
            ("四线城市","3000+","0-50",x)
          }else if(price < 100){
            ("四线城市","3000+","50-100",x)
          }else if(price < 150){
            ("四线城市","3000+","100-150",x)
          }else if(price < 200){
            ("四线城市","3000+","150-200",x)
          }else if(price < 250){
            ("四线城市","3000+","200-250",x)
          }else {
            ("四线城市","3000+","250-300",x)
          }
        }
      }else {
        if(rent < 1000){
          if(price < 50){
            ("五线城市","0-1000","0-50",x)
          }else if(price < 100){
            ("五线城市","0-1000","50-100",x)
          }else if(price < 150){
            ("五线城市","0-1000","100-150",x)
          }else if(price < 200){
            ("五线城市","0-1000","150-200",x)
          }else if(price < 250){
            ("五线城市","0-1000","200-250",x)
          }else {
            ("五线城市","0-1000","250-300",x)
          }
        }else if(rent < 1500){
          if(price < 50){
            ("五线城市","1000-1500","0-50",x)
          }else if(price < 100){
            ("五线城市","1000-1500","50-100",x)
          }else if(price < 150){
            ("五线城市","1000-1500","100-150",x)
          }else if(price < 200){
            ("五线城市","1000-1500","150-200",x)
          }else if(price < 250){
            ("五线城市","1000-1500","200-250",x)
          }else{
            ("五线城市","1000-1500","250-300",x)
          }
        }else if(rent < 2000){
          if(price < 50){
            ("五线城市","1500-2000","0-50",x)
          }else if(price < 100){
            ("五线城市","1500-2000","50-100",x)
          }else if(price < 150){
            ("五线城市","1500-2000","100-150",x)
          }else if(price < 200){
            ("五线城市","1500-2000","150-200",x)
          }else if(price < 250){
            ("五线城市","1500-2000","200-250",x)
          }else {
            ("五线城市","1500-2000","250-300",x)
          }
        }else if(rent < 2500){
          if(price < 50){
            ("五线城市","2000-2500","0-50",x)
          }else if(price < 100){
            ("五线城市","2000-2500","50-100",x)
          }else if(price < 150){
            ("五线城市","2000-2500","100-150",x)
          }else if(price < 200){
            ("五线城市","2000-2500","150-200",x)
          }else if(price < 250){
            ("五线城市","2000-2500","200-250",x)
          }else {
            ("五线城市","2000-2500","250-300",x)
          }
        }else if(rent < 3000){
          if(price < 50){
            ("五线城市","2500-3000","0-50",x)
          }else if(price < 100){
            ("五线城市","2500-3000","50-100",x)
          }else if(price < 150){
            ("五线城市","2500-3000","100-150",x)
          }else if(price < 200){
            ("五线城市","2500-3000","150-200",x)
          }else if(price < 250){
            ("五线城市","2500-3000","200-250",x)
          }else {
            ("五线城市","2500-3000","250-300",x)
          }
        }else{
          if(price < 50){
            ("五线城市","3000+","0-50",x)
          }else if(price < 100){
            ("五线城市","3000+","50-100",x)
          }else if(price < 150){
            ("五线城市","3000+","100-150",x)
          }else if(price < 200){
            ("五线城市","3000+","150-200",x)
          }else if(price < 250){
            ("五线城市","3000+","200-250",x)
          }else{
            ("五线城市","3000+","250-300",x)
          }
        }
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = vvv.filter({
      case(x:String,y:String,z:String,e:Result) =>
        x.equals(city)&& y.equals(rent) && z.equals(price)
    }).map(_._4)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(只过滤租金区间)
    *
    * @param tableName 表名
    * @param build     租金区间条件
    * @param page      查询页码
    * @return
    */
  override def getForValueByRent(tableName: String, rent: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var FMR:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR")) {
          FMR = Integer.parseInt(vals);
        }
      }
      if(FMR <1000){
        ("0-1000",x)
      }else if(FMR < 1500){
        ("1000-1500",x)
      }else if(FMR <2000){
        ("1500-2000",x)
      }else if(FMR < 2500){
        ("2500-3000",x)
      }else if(FMR < 3000){
        ("3000-3500",x)
      }else{
        ("3000+",x)
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,y:Result) => x.equals(rent)
    }).map(_._2)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(只过滤城市规模)
    *
    * @param tableName 表名
    * @param city      城市规模条件
    * @param page      查询页码
    * @return
    */
  override def getForValueByCity(tableName: String, citys: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var city:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3")) {
          city = Integer.parseInt(vals);
        }
      }
      if(city ==1 ){
        ("一线城市",x)
      }else if(city ==1){
        ("二线城市",x)
      }else if(city ==1){
        ("三线城市",x)
      }else if(city ==1){
        ("四线城市",x)
      }else{
        ("五线城市",x)
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,y:Result) => x.equals(citys)
    }).map(_._2)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(只过滤价格区间规模)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param page      查询页码
    * @return
    */
  override def getForValueByPrice(tableName: String, prices: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var price:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE")) {
          price = Integer.parseInt(vals);
        }
      }
      if(price < 50 ){
        ("0-50",x)
      }else if(price < 100 ){
        ("50-100",x)
      }else if(price < 150 ){
        ("100-150",x)
      }else if(price < 200 ){
        ("150-200",x)
      }else if(price < 250){
        ("200-250",x)
      }else{
        ("300+",x)
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,y:Result) => x.equals(prices)
    }).map(_._2)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(过滤租金和城市规模）
    *
    * @param tableName 表名
    * @param rent      租金区间条件
    * @param city      城市规模
    * @param page      查询页码
    * @return
    */
  override def getForValueByCityAndRent(tableName: String, city: String, rent: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    //valuess.foreach(println)
    val v: RDD[Result] = valuess.map(_._2)
    //v.foreach(println)
    val value:RDD[(String,String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var citys:Int = 0;
      var rents:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3")) {
          citys = Integer.parseInt(vals)
        }
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR")) {
          rents = Integer.parseInt(vals);
        }
      }
        if(citys == 1){
          if(rents < 1000){
            ("一线城市","0-1000",x)
          }else if(rents < 1500){
            ("一线城市","1000-1500",x)
          }else if(rents < 2000){
            ("一线城市","1500-2000",x)
          }else if(rents < 2500){
            ("一线城市","2000-2500",x)
          }else if(rents < 3000){
            ("一线城市","2500-3000",x)
          }else{
            ("一线城市","3000+",x)
          }
        }else if(citys == 2){
          if(rents < 1000){
            ("二线城市","0-1000",x)
          }else if(rents < 1500){
            ("二线城市","1000-1500",x)
          }else if(rents < 2000){
            ("二线城市","1500-2000",x)
          }else if(rents < 2500){
            ("二线城市","2000-2500",x)
          }else if(rents < 3000){
            ("二线城市","2500-3000",x)
          }else{
            ("二线城市","3000+",x)
          }
        }else if(citys == 3){
          if(rents < 1000){
            ("三线城市","0-1000",x)
          }else if(rents < 1500){
            ("三线城市","1000-1500",x)
          }else if(rents < 2000){
            ("三线城市","1500-2000",x)
          }else if(rents < 2500){
            ("三线城市","2000-2500",x)
          }else if(rents < 3000){
            ("三线城市","2500-3000",x)
          }else{
            ("三线城市","3000+",x)
          }
        }else if(citys == 4){
          if(rents < 1000){
            ("四线城市","0-1000",x)
          }else if(rents < 1500){
            ("四线城市","1000-1500",x)
          }else if(rents < 2000){
            ("四线城市","1500-2000",x)
          }else if(rents < 2500){
            ("四线城市","2000-2500",x)
          }else if(rents < 3000){
            ("四线城市","2500-3000",x)
          }else{
            ("四线城市","3000+",x)
          }
        }else{
          if(rents < 1000){
            ("五线城市","0-1000",x)
          }else if(rents < 1500){
            ("五线城市","1000-1500",x)
          }else if(rents < 2000){
            ("五线城市","1500-2000",x)
          }else if(rents < 2500){
            ("五线城市","2000-2500",x)
          }else if(rents < 3000){
            ("五线城市","2500-3000",x)
          }else{
            ("五线城市","3000+",x)
          }
        }
    })
  // value.foreach(println)
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,z:String,y:Result) =>
        x.equals(city) && z.equals(rent)
    }).map(_._3)
    va.foreach(println)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
       rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(过滤城市规模和价格)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param city      城市规模
    * @param page      查询页码
    * @return
    */
  override def getForValueByCityAndPrice(tableName: String, city: String, price: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var citys:Int = 0;
      var prices:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3")) {
          citys = Integer.parseInt(vals);
        }
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE")) {
          prices = Integer.parseInt(vals);
        }
      }
      if(citys == 1){
        if(prices < 50){
          ("一线城市","0-50",x)
        }else if(prices < 100){
          ("一线城市","50-100",x)
        }else if(prices < 150){
          ("一线城市","100-150",x)
        }else if(prices < 200){
          ("一线城市","150-200",x)
        }else if(prices < 250){
          ("一线城市","200-250",x)
        }else{
          ("一线城市","250+",x)
        }
      }else if(citys == 2){
        if(prices < 50){
          ("二线城市","0-50",x)
        }else if(prices < 100){
          ("二线城市","50-100",x)
        }else if(prices < 150){
          ("二线城市","100-150",x)
        }else if(prices < 200){
          ("二线城市","150-200",x)
        }else if(prices < 250){
          ("二线城市","200-250",x)
        }else{
          ("二线城市","250+",x)
        }
      }else if(citys == 3){
        if(prices < 50){
          ("三线城市","0-50",x)
        }else if(prices < 100){
          ("三线城市","50-100",x)
        }else if(prices < 150){
          ("三线城市","100-150",x)
        }else if(prices < 200){
          ("三线城市","150-200",x)
        }else if(prices < 250){
          ("三线城市","200-250",x)
        }else{
          ("三线城市","250+",x)
        }
      }else if(citys == 4){
        if(prices < 50){
          ("四线城市","0-50",x)
        }else if(prices < 100){
          ("四线城市","50-100",x)
        }else if(prices < 150){
          ("四线城市","100-150",x)
        }else if(prices < 200){
          ("四线城市","150-200",x)
        }else if(prices < 250){
          ("四线城市","200-250",x)
        }else{
          ("四线城市","250+",x)
        }
      }else{
        if(prices < 50){
          ("五线城市","0-50",x)
        }else if(prices < 100){
          ("五线城市","50-100",x)
        }else if(prices < 150){
          ("五线城市","100-150",x)
        }else if(prices < 200){
          ("五线城市","150-200",x)
        }else if(prices < 250){
          ("五线城市","200-250",x)
        }else{
          ("五线城市","250+",x)
        }
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,z:String,y:Result) =>
        x.equals(city) && z.equals(price)
    }).map(_._3)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  /**
    * 按建成年份统计价格过滤查询数据(过滤价格和租金)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param rent      租金条件
    * @param page      查询页码
    * @return
    */
  override def getForValueByRentAndPrice(tableName: String, rent: String, price: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValueByRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)
    val value:RDD[(String,String,Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var rents:Int = 0;
      var prices:Int = 0;
      for (elem <- cells) {
        val vals = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR")) {
          rents = Integer.parseInt(vals);
        }
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE")) {
          prices = Integer.parseInt(vals);
        }
      }
      if(rents < 1000){
        if(prices < 50){
          ("0-1000","0-50",x)
        }else if(prices < 100){
          ("0-1000","50-100",x)
        }else if(prices < 150){
          ("0-1000","100-150",x)
        }else if(prices < 200){
          ("0-1000","150-200",x)
        }else if(prices < 250){
          ("0-1000","200-250",x)
        }else{
          ("0-1000","250+",x)
        }
      }else if(rents < 1500){
        if(prices < 50){
          ("1000-1500","0-50",x)
        }else if(prices < 100){
          ("1000-1500","50-100",x)
        }else if(prices < 150){
          ("1000-1500","100-150",x)
        }else if(prices < 200){
          ("1000-1500","150-200",x)
        }else if(prices < 250){
          ("1000-1500","200-250",x)
        }else{
          ("1000-1500","250+",x)
        }
      }else if(rents < 2000){
        if(prices < 50){
          ("1500-2000","0-50",x)
        }else if(prices < 100){
          ("1500-2000","50-100",x)
        }else if(prices < 150){
          ("1500-2000","100-150",x)
        }else if(prices < 200){
          ("1500-2000","150-200",x)
        }else if(prices < 250){
          ("1500-2000","200-250",x)
        }else{
          ("1500-2000","250+",x)
        }
      }else if(rents < 2500){
        if(prices < 50){
          ("2000-2500","0-50",x)
        }else if(prices < 100){
          ("2000-2500","50-100",x)
        }else if(prices < 150){
          ("2000-2500","100-150",x)
        }else if(prices < 200){
          ("2000-2500","150-200",x)
        }else if(prices < 250){
          ("2000-2500","200-250",x)
        }else{
          ("2000-2500","250+",x)
        }
      }else if(rents < 3000){
        if(prices < 50){
          ("2500-3000","0-50",x)
        }else if(prices < 100){
          ("2500-3000","50-100",x)
        }else if(prices < 150){
          ("2500-3000","100-150",x)
        }else if(prices < 200){
          ("2500-3000","150-200",x)
        }else if(prices < 250){
          ("2500-3000","200-250",x)
        }else{
          ("2500-3000","250+",x)
        }
      }else{
        if(prices < 50){
          ("3000+","0-50",x)
        }else if(prices < 100){
          ("3000+","50-100",x)
        }else if(prices < 150){
          ("3000+","100-150",x)
        }else if(prices < 200){
          ("3000+","150-200",x)
        }else if(prices < 250){
          ("3000+","200-250",x)
        }else{
          ("3000+","250+",x)
        }
      }
    })
    //过滤，取出符合条件的数据
    val va : RDD[Result] = value.filter({
      case(x:String,z:String,y:Result) =>
        x.equals(rent) && z.equals(price)
    }).map(_._3)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x =>{
      val cells:Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL:String = null
      //城市等级
      var METRO3:String = null
      //房屋售价
      var VALUE:String = null
      //房屋租金
      var FMR:String = null
      //建成年份
      var BUILT:String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          METRO3 = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("VALUE"))
          VALUE = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          FMR = Bytes.toString(CellUtil.cloneValue(elem))
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          BUILT = Bytes.toString(CellUtil.cloneValue(elem))

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

}
