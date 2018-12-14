package com.tecode.house.lijun.dao.impl

import java.util

import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.house.lijun.dao.GetHBaseDao
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._

@Repository
class GetHBaseDaoImpl extends GetHBaseDao {
  /**
    * 得到房屋费用
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    */
  override def getForCOST(tableName: String,filter: String, page: Int): (Long, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForCost").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的租金列的值,转换为（租金区间，Result）的元祖
    val value: RDD[(String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var total: Double = 0;
      var ZSMHC: Double = 0;
      var UTILITY: Double = 0;
      var OTHERCOST: Double = 0;
      for (elem <- cells) {
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("ZSMHC")) {
          ZSMHC = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
      }
      for (elem <- cells) {
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("UTILITY")) {
          UTILITY = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
      }
      for (elem <- cells) {
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("OTHERCOST")) {
          OTHERCOST = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
      }
      total=ZSMHC+UTILITY+OTHERCOST

    /*  if (age < 18) {
        ("0-18", x);
      } else if (age < 40) {
        ("18-40", x);
      } else if (age < 65) {
        ("40-65", x);
      }  else {
        ("65+", x);
      }*/

      if (total < 1500) {
        ("0-1500", x)
      } else if (total < 3000) {
        ("1500-3000", x)
      } else if (total < 4500) {
        ("3000-4500", x)
      } else if (total < 6000) {
        ("4500-6000", x)
      }  else {
        ("6000+", x)
      }
    })
    //    过滤，取出符合条件的数据
    val va: RDD[Result] = value.filter({
      case (x: String, y: Result) => {
        //        判断是否有搜索条件
        if (filter != null) {

          x.equals(filter)
        } else {

          true
        }
      }
    }) map (_._2)
    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var METRO3: String = null;
      var ZSMHC: String = null;
      var UTILITY: String = null;
      var OTHERCOST: String = null;
      var ZINC2: String = null;
      var PER: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("ZSMHC")) {
          ZSMHC = value
        }
        if (str.equals("UTILITY")) {
          UTILITY = value
        }
        if (str.equals("OTHERCOST")) {
          OTHERCOST = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }
        if (str.equals("PER")) {
          PER = value
        }
      }

      list.add(METRO3)
      list.add(ZSMHC)
      list.add(UTILITY)
      list.add(OTHERCOST)
      list.add(ZINC2)
      list.add(PER)
      list.add((UTILITY.toDouble+OTHERCOST.toDouble+ZSMHC.toDouble).toString)
      list
    })
    val l: Long = rowRDD.count()
    val list: List[util.ArrayList[String]] = rowRDD.take(page*10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    var count = java.size()
    if(count<10){
      count=10
    }
    var rows: util.List[util.ArrayList[String]] = java.subList(count - 10, java.size());

    //    sc.stop()
    (l, rows)
  }


  /**
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    */
  override def getPrice(tableName: String, ages:String, values:String, page: Int): (Long, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForValue").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    调用过滤方法
    val result = fiterValue(v, ages, values)
    //    取出Result结果中的所需列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()

      var AGE1: String = null;
      var VALUE: String = null;
      var METRO3: String = null;
      var ZSMHC: String = null;
      var OTHERCOST: String = null;
      var FMR: String = null;
      var ZINC2: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("VALUE")) {
          VALUE = value
        }
        if (str.equals("AGE1")) {
          AGE1 = value
        }
        if (str.equals("ZSMHC")) {
          ZSMHC = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("OTHERCOST")) {
          OTHERCOST = value
        }
        if (str.equals("FMR")) {
          FMR = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }

      }

      list.add(VALUE)
      list.add(AGE1)
      list.add(ZSMHC)
      list.add(OTHERCOST)
      list.add(METRO3)
      list.add(FMR)
      list.add(ZINC2)
      list
    })


    val l: Long = rowRDD.count()
    //    将RDD转换为List
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList

    //    将scala的List转换为java的List
    val java: util.List[util.ArrayList[String]] = list.asJava
    //    获取数据的总条数
    var count = java.size()
    //    截取需要的页码的数据
    //    判断页数是否为最后一页（最后一页的下标算法可能造成下标越界）
    if (count < 10) {
      count = 10
    }
    val rows: util.List[util.ArrayList[String]] = java.subList(count - 10, java.size())
    //    sc.stop()
    (l, rows)
  }

  override def getForIncome(tableName: String, ages:String,incomes:String,page: Int): (Long, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForIncome").setMaster("local[*]")
    val sc = SparkUtil.getSparkContext
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    调用过滤方法
    val result = fiterIncome(v, ages, incomes)
    //    取出Result结果中的所需列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()

      var VALUE: String = null;
      var AGE1: String = null;
      var ZSMHC: String = null;
      var OTHERCOST: String = null;
      var BEDRMS: String = null;
      var ZINC2: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("VALUE")) {
          VALUE = value
        }
        if (str.equals("AGE1")) {
          AGE1 = value
        }
        if (str.equals("ZSMHC")) {
          ZSMHC = value
        }
        if (str.equals("OTHERCOST")) {
          OTHERCOST = value
        }
        if (str.equals("BEDRMS")) {
          BEDRMS = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }
      }
      /*
     var VALUE: String = null;
      var AGE1: String = null;
      var ZSMHC: String = null;
      var OTHERCOST: String = null;
      var BEDRMS: String = null;
      var ZINC2: String = null;
       */
      list.add(VALUE)
      list.add(AGE1)
      list.add(ZSMHC)
      list.add(OTHERCOST)
      list.add(BEDRMS)
      list.add(ZINC2)
      list
    })
    val l: Long = rowRDD.count()

    val list: List[util.ArrayList[String]] = rowRDD.collect().toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    (l,rows)
  }


  def fiterValue(v: RDD[Result], ages: String, values: String): RDD[Result] = {
    //根据户主年龄及价格统计将Result转换为（年龄区间，价格区间，Result）的RDD
    val vvv: RDD[(String, String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //      获取BUILT与METRO3两列的值
      var age: Double = 0;
      var priceValue: Double = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value: String = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("AGE1")) {
          age = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
        if (name.equals("VALUE")) {
          priceValue =(Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
      }
      //      将具体的户主你年龄及住房价格据替换为的元祖
      if (age < 18) {
        if (priceValue<500000) {
          ("0-18", "0-50", x)
        } else if (priceValue<1000000) {
          ("0-18", "50-100", x)
        } else if (priceValue<1500000) {
          ("0-18", "100-150", x)
        } else if (priceValue<2000000) {
          ("0-18", "15000-200", x)
        } else if (priceValue<2500000) {
          ("0-18", "200-250", x)
        }  else {
          ("0-18", "250-300", x)
        }
      } else if (age < 40) {
        if (priceValue<500000) {
          ("18-40", "0-50", x)
        } else if (priceValue<1000000) {
          ("18-40", "50-100", x)
        } else if (priceValue<1500000) {
          ("18-40", "100-150", x)
        } else if (priceValue<2000000) {
          ("18-40", "150-200", x)
        } else if (priceValue<2500000) {
          ("18-40", "200-250", x)
        }  else {
          ("18-40", "250-300", x)
        }
      } else if (age < 65) {
        if (priceValue<500000) {
          ("40-65", "0-50", x)
        } else if (priceValue<1000000) {
          ("40-65", "50-100", x)
        } else if (priceValue<1500000) {
          ("40-65", "100-150", x)
        } else if (priceValue<2000000) {
          ("40-65", "150-200", x)
        } else if (priceValue<2500000) {
          ("40-65", "200-250", x)
        }  else {
          ("40-65", "250-300", x)
        }
      }  else {
        if (priceValue<500000) {
          ("65+", "0-50", x)
        } else if (priceValue<1000000) {
          ("65+", "50-100", x)
        } else if (priceValue<1500000) {
          ("65+", "100-150", x)
        } else if (priceValue<2000000) {
          ("65+", "150-200", x)
        } else if (priceValue<2500000) {
          ("65+", "200-250", x)
        }  else {
          ("65+", "250-300", x)
        }
      }
    })
    //    过滤符合传入条件的Result数据
    val result: RDD[Result] = vvv.filter({
      case (x: String, y: String, z: Result) => {
        //        判断搜索条件为一个还是两个或者没有搜索条件
        //        两个搜索条件
        if (ages != null && values != null) {
          x.equals(ages) && y.equals(values)
          //        一个搜索条件
        } else if (ages != null && values == null) {
          x.equals(ages)
          //        一个搜索条件
        } else if (ages == null && values != null) {
          y.equals(values)
          //          没有搜索条件
        } else {
          true
        }
      }
      //        将（户主年龄，价格统计，Result）的元祖的RDD转换为Result的RDD
    }).map(_._3)
    result
  }


  def fiterIncome(v: RDD[Result], ages: String, incomes: String): RDD[Result] = {
    //根据户主年龄及价格统计将Result转换为（年龄区间，价格区间，Result）的RDD
    val vvv: RDD[(String, String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //      获取BUILT与METRO3两列的值
      var age: Double = 0;
      var priceIncome: Double = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value: String = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("AGE1")) {
          age = (Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
        if (name.equals("ZINC2")) {
          priceIncome =(Bytes.toString(CellUtil.cloneValue(elem))).toDouble
        }
      }
      //      将具体的户主年龄及家庭收入数据替换为的元祖
      if (age < 18) {
        if (priceIncome<50000) {
          ("0-18", "0-5", x)
        } else if (priceIncome<100000) {
          ("0-18", "5-10", x)
        } else if (priceIncome<150000) {
          ("0-18", "10-15", x)
        } else if (priceIncome<200000) {
          ("0-18", "15-20", x)
        } else {
          ("0-18", "20+", x)
        }
      } else if (age < 40) {
        if (priceIncome<50000) {
          ("18-40", "0-5", x)
        } else if (priceIncome<100000) {
          ("18-40", "5-10", x)
        } else if (priceIncome<150000) {
          ("18-40", "10-15", x)
        } else if (priceIncome<200000) {
          ("18-40", "15-20", x)
        } else {
          ("18-40", "20+", x)
        }
      } else if (age < 65) {
        if (priceIncome<50000) {
          ("40-65", "0-5", x)
        } else if (priceIncome<100000) {
          ("40-65", "5-10", x)
        } else if (priceIncome<150000) {
          ("40-65", "10-15", x)
        } else if (priceIncome<200000) {
          ("40-65", "15-20", x)
        } else {
          ("40-65", "20+", x)
        }
      }  else {
        if (priceIncome<50000) {
          ("65+", "0-5", x)
        } else if (priceIncome<100000) {
          ("65+", "5-10", x)
        } else if (priceIncome<150000) {
          ("65+", "10-15", x)
        } else if (priceIncome<200000) {
          ("65+", "15-20", x)
        } else {
          ("65+", "20+", x)
        }
      }
    })
    //    过滤符合传入条件的Result数据
    val result: RDD[Result] = vvv.filter({
      case (x: String, y: String, z: Result) => {
        //        判断搜索条件为一个还是两个或者没有搜索条件
        //        两个搜索条件
        if (ages != null && incomes != null) {
          x.equals(ages) && y.equals(incomes)
          //        一个搜索条件
        } else if (ages != null && incomes == null) {
          x.equals(ages)
          //        一个搜索条件
        } else if (ages == null && incomes != null) {
          y.equals(incomes)
          //          没有搜索条件
        } else {
          true
        }
      }
      //        将（建成年份区间，城市规模，Result）的元祖的RDD转换为Result的RDD
    }).map(_._3)
    result
  }
}
