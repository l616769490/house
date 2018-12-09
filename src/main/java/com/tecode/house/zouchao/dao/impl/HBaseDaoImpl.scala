package com.tecode.house.zouchao.dao.impl

import java.util

import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.house.zouchao.dao.HBaseDao
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
class HBaseDaoImpl extends HBaseDao {


  override def getForRent(tableName: String, filter: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {

//    val conf = new SparkConf().setAppName("getForRent").setMaster("local[*]")
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
      val cells: Array[Cell] = x.rawCells();
      var FMR: Int = 0;
      for (elem <- cells) {
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR")) {
          FMR = Integer.parseInt(value);
        }
      }
      //      将具体的租金转换成租金区间（租金区间，Result）
      if (FMR < 1000) {
        ("0-1000", x);
      } else if (FMR < 1500) {
        ("1000-1500", x);
      } else if (FMR < 2000) {
        ("1500-2000", x);
      } else if (FMR < 2500) {
        ("2000-2500", x);
      } else if (FMR < 3000) {
        ("2500-3000", x);
      } else {
        ("3000+", x);
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
      var FMR: String = null;
      var METRO3: String = null;
      var REGION: String = null;
      var STRUCTURETYPE: String = null;
      var ROOMS: String = null;
      var BEDRMS: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("FMR")) {
          FMR = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("REGION")) {
          REGION = value
        }
        if (str.equals("STRUCTURETYPE")) {
          STRUCTURETYPE = value
        }
        if (str.equals("ROOMS")) {
          ROOMS = value
        }
        if (str.equals("BEDRMS")) {
          BEDRMS = value
        }
      }

      list.add(FMR)
      list.add(METRO3)
      list.add(REGION)
      list.add(STRUCTURETYPE)
      list.add(ROOMS)
      list.add(BEDRMS)
      list
    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page*10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = java.subList(count - 10, count);

    sc.stop()
    (count, rows)
  }


  override def getForValue(tableName: String, builds: String, citys: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForValue").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    调用过滤方法，获取符合传入条件的数据
    val result: RDD[Result] = fiter(v, builds, citys);

    //    取出Result结果中的需要的列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()

      var BUILT: String = null;
      var FMR: String = null;
      var VALUE: String = null;
      var METRO3: String = null;
      var REGION: String = null;
      var STRUCTURETYPE: String = null;
      var ROOMS: String = null;
      var BEDRMS: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("BUILT")) {
          BUILT = value
        }
        if (str.equals("VALUE")) {
          VALUE = value
        }
        if (str.equals("FMR")) {
          FMR = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("REGION")) {
          REGION = value
        }
        if (str.equals("STRUCTURETYPE")) {
          STRUCTURETYPE = value
        }
        if (str.equals("ROOMS")) {
          ROOMS = value
        }
        if (str.equals("BEDRMS")) {
          BEDRMS = value
        }
      }

      list.add(BUILT)
      list.add(FMR)
      list.add(VALUE)
      list.add(METRO3)
      list.add(REGION)
      list.add(STRUCTURETYPE)
      list.add(ROOMS)
      list.add(BEDRMS)
      list
    })
    //    将RDD转换为List
    val list: List[util.ArrayList[String]] = rowRDD.take(page*10).toList

    //    将scala的List转换为java的List
    val java: util.List[util.ArrayList[String]] = list.asJava
    var rows: util.List[util.ArrayList[String]] = null;
    //    获取数据的总条数
    val count = java.size()
    //    截取需要的页码的数据
    //    判断页数是否为最后一页（最后一页的下标算法可能造成下标越界）
    if (page * 10 > count) {
      //      如果越界则将其换为List的长度
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    sc.stop()
    (count, rows)
  }

  override def getForRom(tableName: String, builds: String, citys: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForRom").setMaster("local[*]")
    val sc = new SparkContext(conf)
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
    val result = fiter(v, builds, citys)
    //    取出Result结果中的所需列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var BUILT: String = null;
      var ROOMS: String = null;
      var BEDRMS: String = null;
      var METRO3: String = null;
      var REGION: String = null;
      var PER: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("BUILT")) {
          BUILT = value
        }
        if (str.equals("ROOMS")) {
          ROOMS = value
        }
        if (str.equals("BEDRMS")) {
          BEDRMS = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("REGION")) {
          REGION = value
        }
        if (str.equals("PER")) {
          PER = value
        }
      }
      list.add(BUILT)
      list.add(ROOMS)
      list.add(BEDRMS)
      list.add(METRO3)
      list.add(REGION)
      list.add(PER)
      list
    })

    val list: List[util.ArrayList[String]] = rowRDD.take(page*10).toList
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
    * 过滤符合搜索条件的数据
    *
    * @param v      待过滤的RDD
    * @param builds 建成年份过滤条件
    * @param citys  城市规模过滤条件
    * @return 符合条件的RDD
    */
  def fiter(v: RDD[Result], builds: String, citys: String): RDD[Result] = {
    //根据建成年份及城市等级将Result转换为（年份区间，城市等级，Result）的RDD
    val vvv: RDD[(String, String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //      获取BUILT与METRO3两列的值
      var build: Int = 0;
      var city: Int = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value: String = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("BUILT")) {
          build = Integer.parseInt(value)
        }
        if (name.equals("METRO3")) {
          city = Integer.parseInt(value)
        }
      }
      //      将具体的建成年份及城市规模数据替换为（年份区间，城市等级，Result）的元祖
      if (build < 1940) {
        if (city == 1) {
          ("1900-1940", "1", x)
        } else if (city == 2) {
          ("1900-1940", "2", x)
        } else if (city == 3) {
          ("1900-1940", "3", x)
        } else if (city == 4) {
          ("1900-1940", "4", x)
        } else {
          ("1900-1940", "5", x)
        }
      } else if (build < 1960) {
        if (city == 1) {
          ("1940-1960", "1", x)
        } else if (city == 2) {
          ("1940-1960", "2", x)
        } else if (city == 3) {
          ("1940-1960", "3", x)
        } else if (city == 4) {
          ("1940-1960", "4", x)
        } else {
          ("1940-1960", "5", x)
        }
      } else if (build < 1980) {
        if (city == 1) {
          ("1960-1980", "1", x)
        } else if (city == 2) {
          ("1960-1980", "2", x)
        } else if (city == 3) {
          ("1960-1980", "3", x)
        } else if (city == 4) {
          ("1960-1980", "4", x)
        } else {
          ("1960-1980", "5", x)
        }
      } else if (build < 2000) {
        if (city == 1) {
          ("1980-2000", "1", x)
        } else if (city == 2) {
          ("1980-2000", "2", x)
        } else if (city == 3) {
          ("1980-2000", "3", x)
        } else if (city == 4) {
          ("1980-2000", "4", x)
        } else {
          ("1980-2000", "5", x)
        }
      } else {
        if (city == 1) {
          ("2000+", "1", x)
        } else if (city == 2) {
          ("2000+", "2", x)
        } else if (city == 3) {
          ("2000+", "3", x)
        } else if (city == 4) {
          ("2000+", "4", x)
        } else {
          ("2000+", "5", x)
        }
      }
    })
    //    过滤符合传入条件的Result数据
    val result: RDD[Result] = vvv.filter({
      case (x: String, y: String, z: Result) => {
        //        判断搜索条件为一个还是两个或者没有搜索条件
        //        两个搜索条件
        if (builds != null && citys != null) {
          x.equals(builds) && y.equals(citys)
          //        一个搜索条件
        } else if (builds != null && citys == null) {
          x.equals(builds)
          //        一个搜索条件
        } else if (builds == null && citys != null) {
          y.equals(citys)
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
