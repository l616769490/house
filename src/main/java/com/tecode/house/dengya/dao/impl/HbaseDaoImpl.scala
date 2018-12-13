package com.tecode.house.dengya.dao.impl

import java.util

import com.tecode.house.dengya.dao.HbaseDao
import com.tecode.house.lijin.utils.SparkUtil
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
  override def getAllForUnits(tableName: String,filter:String, page: Int): (Long, util.List[util.ArrayList[String]]) = {

    val sc =SparkUtil.getSparkContext
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(_._2)

      val value:RDD[(String,Result)] = v.map(x =>{
     val cells:Array[Cell] = x.rawCells();
     var NUNITS:Int = 0
     for(elem <- cells){
       val nuitsValue = Bytes.toString(CellUtil.cloneValue(elem))
       if(Bytes.toString(CellUtil.cloneQualifier(elem)).equals("NUNITS")){
         NUNITS = Integer.parseInt(nuitsValue)
       }
     }
     //将单元数转成（units，result）
     if(NUNITS < 100 ){
       ("0-100",x)
     }else if(NUNITS < 200){
       ("100-200",x)
     }else if(NUNITS <300){
       ("200-300",x)
     }else if(NUNITS < 400){
       ("300-400",x)
     }else if(NUNITS < 500){
       ("400-500",x)
     }else if(NUNITS < 600){
       ("500-600",x)
     }else if(NUNITS < 700){
       ("600-700",x)
     }else if(NUNITS < 800){
       ("700-800",x)
     }else if(NUNITS < 900){
       ("800-900",x)
     }else{
       ("900-1000",x)
     }
   })
    //过滤，取出符合条件的数据
    val va:RDD[Result] = value.filter({
      case(x :String,y:Result) => {
        //判断是否有搜索条件
        if(filter != null){
          x.equals(filter)
        }else{
          true
        }
      }
    })map(_._2)

    val rowRDD:RDD[util.ArrayList[String]] = va.map(x =>{
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
        val string: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if(string.equals("METRO3")){
          METRO3=value
        }
        if(string.equals("BUILT")){
          BUILT = value
        }
        if(string.equals("NUNITS")){
          NUNITS = value
        }

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(BUILT)
      list.add(NUNITS)
      list
    })
    //将RDD装换成scala的list
    val l: Long = rowRDD.count()
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    var count = java.size();
    if(count < 10){
      count = 10
    }
    var rows: util.List[util.ArrayList[String]] = java.subList(count - 10,java.size());
//    sc.stop()
    (l,rows)
  }



  /**
    * 按城市规模统计价格过滤查询数据
    *
    * @param tableName 表名
    * @param rent      租金区间条件
    * @param city      城市规模条件
    * @param price     售价
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getForValue(tableName: String, citys: String, rents: String, prices: String, page: Int): (Long, util.List[util.ArrayList[String]]) = {
//    val conf = new SparkConf().setAppName("getForValue").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //调用过滤方法，获取符合传入条件的数据
    val results: RDD[Result] = fiter(v, citys, rents, prices)

    //取出result结果中需要的列的值
    val rowRDD: RDD[util.ArrayList[String]] = results.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //房屋编号//房屋编号
      var CONTROL: String = null
      //城市等级
      var METRO3: String = null
      //房屋售价
      var VALUE: String = null
      //房屋租金
      var FMR: String = null
      //建成年份
      var BUILT: String = null
      val list = new util.ArrayList[String]();
      for (elem <- cells) {
        //        获取传入的列的值
        val str: String = Bytes.toString(CellUtil.cloneRow(elem))
        val rowKey: Array[String] = str.split("_")
        CONTROL = rowKey(1)
        val string: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (string.equals("METRO3")) {
          METRO3 = value
        }
        if (string.equals("VALUE")) {
          VALUE = value
        }
        if (string.equals("FMR")) {
          FMR = value
        }
        if (string.equals("BUILT")) {
          BUILT = value
        }

      }
      list.add(CONTROL)
      list.add(METRO3)
      list.add(VALUE)
      list.add(FMR)
      list.add(BUILT)
      list

  })
    val l: Long = rowRDD.count()
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    //var rows: util.List[util.ArrayList[String]] = null;
    var count = java.size()
    if(count < 10){
      count = 10
    }
   // if (page * 10 > count) {
     // rows = java.subList((page - 1) * 10, count)
   // } else {
      //rows = java.subList((page - 1) * 10, page * 10)
   // }
//    sc.stop()
    val rows = java.subList(count - 10,java.size())
    (l, rows)

  }

  /**
    * 过滤符合搜索条件的数据
    * @param v 待过滤的RDD
    * @param citys  城市规模过滤条件
    * @param rents   租金区间的过滤条件
    * @param prices   价格区间的过滤条件
    * @return
    */
  def fiter(v:RDD[Result],cityss:String,rents:String,prices:String):RDD[Result] = {
    //根据城市规模、租金区间和价格区间讲result转换为（城市规模，租金区间，价格区间，result）的RDD
    val vvv: RDD[(String, String, String, Result)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var city: Int = 0;
      var rent: Int = 0;
      var price: Int = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("METRO3")) {
          city = Integer.parseInt(value);
        }
        if (name.equals("FMR")) {
          rent = Integer.parseInt(value)
        }
        if (name.equals("VALUE")) {
          price = Integer.valueOf(value)/10000
        }
      }
      //将具体的城市规模、租金及价格数据替换为（城市规模，租金，价格，Result）的元祖
      if (city == 1) {
        if (rent < 1000) {
          if (price < 50) {
            ("1", "0-1000", "0-50", x)
          } else if (price < 100) {
            ("1", "0-1000", "50-100", x)
          } else if (price < 150) {
            ("1", "0-1000", "100-150", x)
          } else if (price < 200) {
            ("1", "0-1000", "150-200", x)
          } else if (price < 250) {
            ("1", "0-1000", "200-250", x)
          } else {
            ("1", "0-1000", "250-300", x)
          }
        } else if (rent < 1500) {
          if (price < 50) {
            ("1", "1000-1500", "0-50", x)
          } else if (price < 100) {
            ("1", "1000-1500", "50-100", x)
          } else if (price < 150) {
            ("1", "1000-1500", "100-150", x)
          } else if (price < 200) {
            ("1", "1000-1500", "150-200", x)
          } else if (price < 250) {
            ("1", "1000-1500", "200-250", x)
          } else {
            ("1", "1000-1500", "250-300", x)
          }
        } else if (rent < 2000) {
          if (price < 50) {
            ("1", "1500-2000", "0-50", x)
          } else if (price < 100) {
            ("1", "1500-2000", "50-100", x)
          } else if (price < 150) {
            ("1", "1500-2000", "100-150", x)
          } else if (price < 200) {
            ("1", "1500-2000", "150-200", x)
          } else if (price < 250) {
            ("1", "1500-2000", "200-250", x)
          } else {
            ("1", "1500-2000", "250-300", x)
          }
        } else if (rent < 2500) {
          if (price < 50) {
            ("1", "2000-2500", "0-50", x)
          } else if (price < 100) {
            ("1", "2000-2500", "50-100", x)
          } else if (price < 150) {
            ("1", "2000-2500", "100-150", x)
          } else if (price < 200) {
            ("1", "2000-2500", "150-200", x)
          } else if (price < 250) {
            ("1", "2000-2500", "200-250", x)
          } else {
            ("1", "2000-2500", "250-300", x)
          }
        } else if (rent < 3000) {
          if (price < 50) {
            ("1", "2500-3000", "0-50", x)
          } else if (price < 100) {
            ("1", "2500-3000", "50-100", x)
          } else if (price < 150) {
            ("1", "2500-3000", "100-150", x)
          } else if (price < 200) {
            ("1", "2500-3000", "150-200", x)
          } else if (price < 250) {
            ("1", "2500-3000", "200-250", x)
          } else {
            ("1", "2500-3000", "250-300", x)
          }
        } else {
          if (price < 50) {
            ("1", "3000+", "0-50", x)
          } else if (price < 100) {
            ("1", "3000+", "50-100", x)
          } else if (price < 150) {
            ("1", "3000+", "100-150", x)
          } else if (price < 200) {
            ("1", "3000+", "150-200", x)
          } else if (price < 250) {
            ("1", "3000+", "200-250", x)
          } else {
            ("1", "3000+", "250-300", x)
          }
        }
      } else if (city == 2) {
        if (rent < 1000) {
          if (price < 50) {
            ("2", "0-1000", "0-50", x)
          } else if (price < 100) {
            ("2", "0-1000", "50-100", x)
          } else if (price < 150) {
            ("2", "0-1000", "100-150", x)
          } else if (price < 200) {
            ("2", "0-1000", "150-200", x)
          } else if (price < 250) {
            ("2", "0-1000", "200-250", x)
          } else {
            ("2", "0-1000", "250-300", x)
          }
        } else if (rent < 1500) {
          if (price < 50) {
            ("2", "1000-1500", "0-50", x)
          } else if (price < 100) {
            ("2", "1000-1500", "50-100", x)
          } else if (price < 150) {
            ("2", "1000-1500", "100-150", x)
          } else if (price < 200) {
            ("2", "1000-1500", "150-200", x)
          } else if (price < 250) {
            ("2", "1000-1500", "200-250", x)
          } else {
            ("2", "1000-1500", "250-300", x)
          }
        } else if (rent < 2000) {
          if (price < 50) {
            ("2", "1500-2000", "0-50", x)
          } else if (price < 100) {
            ("2", "1500-2000", "50-100", x)
          } else if (price < 150) {
            ("2", "1500-2000", "100-150", x)
          } else if (price < 200) {
            ("2", "1500-2000", "150-200", x)
          } else if (price < 250) {
            ("2", "1500-2000", "200-250", x)
          } else {
            ("2", "1500-2000", "250-300", x)
          }
        } else if (rent < 2500) {
          if (price < 50) {
            ("2", "2000-2500", "0-50", x)
          } else if (price < 100) {
            ("2", "2000-2500", "50-100", x)
          } else if (price < 150) {
            ("2", "2000-2500", "100-150", x)
          } else if (price < 200) {
            ("2", "2000-2500", "150-200", x)
          } else if (price < 250) {
            ("2", "2000-2500", "200-250", x)
          } else {
            ("2", "2000-2500", "250-300", x)
          }
        } else if (rent < 3000) {
          if (price < 50) {
            ("2", "2500-3000", "0-50", x)
          } else if (price < 100) {
            ("2", "2500-3000", "50-100", x)
          } else if (price < 150) {
            ("2", "2500-3000", "100-150", x)
          } else if (price < 200) {
            ("2", "2500-3000", "150-200", x)
          } else if (price < 250) {
            ("2", "2500-3000", "200-250", x)
          } else {
            ("2", "2500-3000", "250-300", x)
          }
        } else {
          if (price < 50) {
            ("2", "3000+", "0-50", x)
          } else if (price < 100) {
            ("2", "3000+", "50-100", x)
          } else if (price < 150) {
            ("2", "3000+", "100-150", x)
          } else if (price < 200) {
            ("2", "3000+", "150-200", x)
          } else if (price < 250) {
            ("2", "3000+", "200-250", x)
          } else {
            ("2", "3000+", "250-300", x)
          }
        }
      } else if (city == 3) {
        if (rent < 1000) {
          if (price < 50) {
            ("3", "0-1000", "0-50", x)
          } else if (price < 100) {
            ("3", "0-1000", "50-100", x)
          } else if (price < 150) {
            ("3", "0-1000", "100-150", x)
          } else if (price < 200) {
            ("3", "0-1000", "150-200", x)
          } else if (price < 250) {
            ("3", "0-1000", "200-250", x)
          } else {
            ("3", "0-1000", "250-300", x)
          }
        } else if (rent < 1500) {
          if (price < 50) {
            ("3", "1000-1500", "0-50", x)
          } else if (price < 100) {
            ("3", "1000-1500", "50-100", x)
          } else if (price < 150) {
            ("3", "1000-1500", "100-150", x)
          } else if (price < 200) {
            ("3", "1000-1500", "150-200", x)
          } else if (price < 250) {
            ("3", "1000-1500", "200-250", x)
          } else {
            ("3", "1000-1500", "250-300", x)
          }
        } else if (rent < 2000) {
          if (price < 50) {
            ("3", "1500-2000", "0-50", x)
          } else if (price < 100) {
            ("3", "1500-2000", "50-100", x)
          } else if (price < 150) {
            ("3", "1500-2000", "100-150", x)
          } else if (price < 200) {
            ("3", "1500-2000", "150-200", x)
          } else if (price < 250) {
            ("3", "1500-2000", "200-250", x)
          } else {
            ("3", "1500-2000", "250-300", x)
          }
        } else if (rent < 2500) {
          if (price < 50) {
            ("3", "2000-2500", "0-50", x)
          } else if (price < 100) {
            ("3", "2000-2500", "50-100", x)
          } else if (price < 150) {
            ("3", "2000-2500", "100-150", x)
          } else if (price < 200) {
            ("3", "2000-2500", "150-200", x)
          } else if (price < 250) {
            ("3", "2000-2500", "200-250", x)
          } else {
            ("3", "2000-2500", "250-300", x)
          }
        } else if (rent < 3000) {
          if (price < 50) {
            ("3", "2500-3000", "0-50", x)
          } else if (price < 100) {
            ("3", "2500-3000", "50-100", x)
          } else if (price < 150) {
            ("3", "2500-3000", "100-150", x)
          } else if (price < 200) {
            ("3", "2500-3000", "150-200", x)
          } else if (price < 250) {
            ("3", "2500-3000", "200-250", x)
          } else {
            ("3", "2500-3000", "250-300", x)
          }
        } else {
          if (price < 50) {
            ("3", "3000+", "0-50", x)
          } else if (price < 100) {
            ("3", "3000+", "50-100", x)
          } else if (price < 150) {
            ("3", "3000+", "100-150", x)
          } else if (price < 200) {
            ("3", "3000+", "150-200", x)
          } else if (price < 250) {
            ("3", "3000+", "200-250", x)
          } else {
            ("3", "3000+", "250-300", x)
          }
        }
      } else if (city == 4) {
        if (rent < 1000) {
          if (price < 50) {
            ("4", "0-1000", "0-50", x)
          } else if (price < 100) {
            ("4", "0-1000", "50-100", x)
          } else if (price < 150) {
            ("4", "0-1000", "100-150", x)
          } else if (price < 200) {
            ("4", "0-1000", "150-200", x)
          } else if (price < 250) {
            ("4", "0-1000", "200-250", x)
          } else {
            ("4", "0-1000", "250-300", x)
          }
        } else if (rent < 1500) {
          if (price < 50) {
            ("4", "1000-1500", "0-50", x)
          } else if (price < 100) {
            ("4", "1000-1500", "50-100", x)
          } else if (price < 150) {
            ("4", "1000-1500", "100-150", x)
          } else if (price < 200) {
            ("4", "1000-1500", "150-200", x)
          } else if (price < 250) {
            ("4", "1000-1500", "200-250", x)
          } else {
            ("4", "1000-1500", "250-300", x)
          }
        } else if (rent < 2000) {
          if (price < 50) {
            ("4", "1500-2000", "0-50", x)
          } else if (price < 100) {
            ("4", "1500-2000", "50-100", x)
          } else if (price < 150) {
            ("4", "1500-2000", "100-150", x)
          } else if (price < 200) {
            ("4", "1500-2000", "150-200", x)
          } else if (price < 250) {
            ("4", "1500-2000", "200-250", x)
          } else {
            ("4", "1500-2000", "250-300", x)
          }
        } else if (rent < 2500) {
          if (price < 50) {
            ("4", "2000-2500", "0-50", x)
          } else if (price < 100) {
            ("4", "2000-2500", "50-100", x)
          } else if (price < 150) {
            ("4", "2000-2500", "100-150", x)
          } else if (price < 200) {
            ("4", "2000-2500", "150-200", x)
          } else if (price < 250) {
            ("4", "2000-2500", "200-250", x)
          } else {
            ("4", "2000-2500", "250-300", x)
          }
        } else if (rent < 3000) {
          if (price < 50) {
            ("4", "2500-3000", "0-50", x)
          } else if (price < 100) {
            ("4", "2500-3000", "50-100", x)
          } else if (price < 150) {
            ("4", "2500-3000", "100-150", x)
          } else if (price < 200) {
            ("4", "2500-3000", "150-200", x)
          } else if (price < 250) {
            ("4", "2500-3000", "200-250", x)
          } else {
            ("4", "2500-3000", "250-300", x)
          }
        } else {
          if (price < 50) {
            ("4", "3000+", "0-50", x)
          } else if (price < 100) {
            ("4", "3000+", "50-100", x)
          } else if (price < 150) {
            ("4", "3000+", "100-150", x)
          } else if (price < 200) {
            ("4", "3000+", "150-200", x)
          } else if (price < 250) {
            ("4", "3000+", "200-250", x)
          } else {
            ("4", "3000+", "250-300", x)
          }
        }
      } else {
        if (rent < 1000) {
          if (price < 50) {
            ("5", "0-1000", "0-50", x)
          } else if (price < 100) {
            ("5", "0-1000", "50-100", x)
          } else if (price < 150) {
            ("5", "0-1000", "100-150", x)
          } else if (price < 200) {
            ("5", "0-1000", "150-200", x)
          } else if (price < 250) {
            ("5", "0-1000", "200-250", x)
          } else {
            ("5", "0-1000", "250-300", x)
          }
        } else if (rent < 1500) {
          if (price < 50) {
            ("5", "1000-1500", "0-50", x)
          } else if (price < 100) {
            ("5", "1000-1500", "50-100", x)
          } else if (price < 150) {
            ("5", "1000-1500", "100-150", x)
          } else if (price < 200) {
            ("5", "1000-1500", "150-200", x)
          } else if (price < 250) {
            ("5", "1000-1500", "200-250", x)
          } else {
            ("5", "1000-1500", "250-300", x)
          }
        } else if (rent < 2000) {
          if (price < 50) {
            ("5", "1500-2000", "0-50", x)
          } else if (price < 100) {
            ("5", "1500-2000", "50-100", x)
          } else if (price < 150) {
            ("5", "1500-2000", "100-150", x)
          } else if (price < 200) {
            ("5", "1500-2000", "150-200", x)
          } else if (price < 250) {
            ("5", "1500-2000", "200-250", x)
          } else {
            ("5", "1500-2000", "250-300", x)
          }
        } else if (rent < 2500) {
          if (price < 50) {
            ("5", "2000-2500", "0-50", x)
          } else if (price < 100) {
            ("5", "2000-2500", "50-100", x)
          } else if (price < 150) {
            ("5", "2000-2500", "100-150", x)
          } else if (price < 200) {
            ("5", "2000-2500", "150-200", x)
          } else if (price < 250) {
            ("5", "2000-2500", "200-250", x)
          } else {
            ("5", "2000-2500", "250-300", x)
          }
        } else if (rent < 3000) {
          if (price < 50) {
            ("5", "2500-3000", "0-50", x)
          } else if (price < 100) {
            ("5", "2500-3000", "50-100", x)
          } else if (price < 150) {
            ("5", "2500-3000", "100-150", x)
          } else if (price < 200) {
            ("5", "2500-3000", "150-200", x)
          } else if (price < 250) {
            ("5", "2500-3000", "200-250", x)
          } else {
            ("5", "2500-3000", "250-300", x)
          }
        } else {
          if (price < 50) {
            ("5", "3000+", "0-50", x)
          } else if (price < 100) {
            ("5", "3000+", "50-100", x)
          } else if (price < 150) {
            ("5", "3000+", "100-150", x)
          } else if (price < 200) {
            ("5", "3000+", "150-200", x)
          } else if (price < 250) {
            ("5", "3000+", "200-250", x)
          } else {
            ("5", "3000+", "250-300", x)
          }
        }
      }
    })

    //过滤，取出符合条件的数据
    val result:RDD[Result] = vvv.filter({
      case(x:String,y:String,z:String,r:Result) =>{
        //判断搜索条件为三个还是两个或者没有
        //三个搜索条件
        if(cityss != null && rents != null&& prices != null){
          x.equals(cityss) && y.equals(rents)&&z.equals(prices)
          //两个搜索条件（city和rents）
        }else if(cityss != null && rents !=null &&prices ==null){
          x.equals(cityss) && y.equals(rents)
          //两个搜索条件（city 和 prices）
        }else if(cityss != null && rents ==null && prices != null){
          x.equals(cityss) && z.equals( prices)
          //两个搜索条件（rent和price）
        }else if(cityss == null && rents != null && prices != null){
          y.equals(rents) && z.equals(prices)
        //一个搜索条件(citys)
        }else if(cityss != null && rents == null&& prices == null){
          x.equals(cityss)
          //一个搜索条件（rent）
        }else if(cityss == null && rents != null && prices == null){
          y.equals(rents)
          //一个搜索条件（prices）
        }else if(cityss == null && rents == null && prices != null){
          z.equals(prices)
        }else{
          //没有搜索条件
          true
        }
      }
    }).map(_._4)
    result
  }
}