package com.tecode.house.lijun.dao.impl

import java.util

import com.tecode.house.lijun.dao.HBaseDao
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

  override def getForCOST(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForRent").setMaster("local[*]")
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
    //    取出Result结果中的所需列的值，封装成java的List集合
    val rowRDD: RDD[util.ArrayList[String]] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      //      待获取的列的值
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
      list
    })
    //    将RDD转换成scala的list
    val list: List[util.ArrayList[String]] = rowRDD.collect().toList
    //    将scala的list转换成java的list
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    //    封装查询页的数据list
    var rows: util.List[util.ArrayList[String]] = null;
    //    判断是否为最后一页（可能会下标越界）
    if (page * 10 > count) {
      //      越界则更改下标为list的size
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    (count, rows)
  }

  override def getAllForValue(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getAllForValue").setMaster("local[*]")
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
    //    取出Result结果中的所需列的值
    val rowRDD: RDD[util.ArrayList[String]] = v.map(x => {
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
    val list: List[util.ArrayList[String]] = rowRDD.collect().toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    var rows: util.List[util.ArrayList[String]] = null;
    val count = java.size()
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    (count, rows)
  }

  override def getForIncome(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {
    val conf = new SparkConf().setAppName("getForRom").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //    获取数据
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的所需列的值
    val rowRDD: RDD[util.ArrayList[String]] = v.map(x => {
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
    val list: List[util.ArrayList[String]] = rowRDD.collect().toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = null;
    if (page * 10 > count) {
      rows = java.subList((page - 1) * 10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
    (count, rows)
  }
}
