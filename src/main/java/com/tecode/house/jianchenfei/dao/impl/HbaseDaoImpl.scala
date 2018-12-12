package com.tecode.house.jianchenfei.dao.impl

import java.util

import com.tecode.house.jianchenfei.dao.HBaseDao
import com.tecode.house.lijin.utils.SparkUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD

import scala.collection.JavaConverters._


/**
  * Created by Administrator on 2018/12/9.
  */
class HbaseDaoImpl extends HBaseDao {
  /**
    * 根据过滤条件查询家庭人数报表的数据
    *
    * @param tableName 表名
    * @param filter    过滤条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getPer(tableName: String, filter: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {

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
    //    取出Result结果中的per列的值,转换为（家庭人数，Result）的元祖
    val value: RDD[(String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      var PER: Int = 0;
      for (elem <- cells) {
        val value = Bytes.toString(CellUtil.cloneValue(elem));
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("PER")) {
          PER = Integer.parseInt(value);
        }
      }
      //      （家庭人数，Result）
      if (PER == 1) {
        ("1人", x);
      } else if (PER == 2) {
        ("2人", x);
      } else if (PER == 3) {
        ("3人", x);
      } else if (PER == 4) {
        ("4人", x);
      } else if (PER == 5) {
        ("5人", x);
      } else if (PER == 6) {
        ("6人", x);
      } else {
        ("6+", x)
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
    }).map (_._2)

    //获取所需列的数据
    val rowRDD: RDD[util.ArrayList[String]] = va.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var CONTROL: String = null;
      var METRO3: String = null;
      var ZINC2: String = null;
      var ROOMS: String = null;
      var BEDRMS: String = null;
      var PER: String = null;
      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("CONTROL")) {
          CONTROL = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }
        if (str.equals("ROOMS")) {
          ROOMS = value
        }
        if (str.equals("BEDRMS")) {
          BEDRMS = value
        }
        if (str.equals("PER")) {
          PER = value
        }
      }

      list.add(CONTROL)
      list.add(METRO3)
      list.add(ZINC2)
      list.add(ROOMS)
      list.add(BEDRMS)
      list.add(PER)
      list
    })
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList
    val java: util.List[util.ArrayList[String]] = list.asJava
    val count = java.size()
    var rows: util.List[util.ArrayList[String]] = java.subList(count - 10, count);

//    sc.stop()
    (count, rows)
  }


  /**
    * 按建成年份统计房产税过滤查询数据
    *
    * @param tableName 表名
    * @param builds     建成年份区间条件
    * @param rates      房产税条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getRate(tableName: String, builds: String, rates: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {

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
    //    调用过滤方法，获取符合传入条件的数据
    val result: RDD[Result] = fiterBuild(v, builds, rates);

    //    取出Result结果中的需要的列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()

      var CONTROL: String = null;
      var METRO3: String = null;
      var ZINC2: String = null;
      var BUILT: String = null;
      var VALUE: String = null;
      var ZSMHC: String = null;

      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("CONTROL")) {
          CONTROL = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }
        if (str.equals("VALUE")) {
          VALUE = value
        }
        if (str.equals("ZSMHC")) {
          ZSMHC = value
        }
        if (str.equals("BUILT")) {
          BUILT = value
        }
      }

      list.add(CONTROL)
      list.add(METRO3)
      list.add(ZINC2)
      list.add(BUILT)
      list.add(VALUE)
      list.add(ZSMHC)
      list

    })
    //    将RDD转换为List
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList

    //    将scala的List转换为java的List
    val java: util.List[util.ArrayList[String]] = list.asJava
    var rows: util.List[util.ArrayList[String]] = null;
    //    获取数据的总条数
    val count = java.size()
    //    截取需要的页码的数据
    //    判断页数是否为最后一页（最后一页的下标算法可能造成下标越界）
    if (page * 10 > count) {
      //      如果越界则将其换为List的长度
      rows = java.subList(count -10, count)
    } else {
      rows = java.subList((page - 1) * 10, page * 10)
    }
//    sc.stop()
    (count, rows)

  }


  /**
    * 按区域统计独栋比例查询数据
    *
    * @param tableName 表名
    * @param regions   地区条件
    * @param singles    独栋比例条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  override def getSingle(tableName: String, regions: String, singles: String, page: Int): (Int, util.List[util.ArrayList[String]]) = {

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
    //    调用过滤方法，获取符合传入条件的数据
    val result: RDD[Result] = fiterSingle(v, regions, singles);

    //    取出Result结果中的需要的列的值
    val rowRDD: RDD[util.ArrayList[String]] = result.map(x => {
      val cells: Array[Cell] = x.rawCells()

      var CONTROL: String = null;
      var METRO3: String = null;
      var ZINC2: String = null;
      var ROOMS: String = null;
      var REGION: String = null;
      var NUNITS: String = null;

      val list = new util.ArrayList[String]()
      for (elem <- cells) {
        val str: String = Bytes.toString(CellUtil.cloneQualifier(elem))
        val value: String = Bytes.toString(CellUtil.cloneValue(elem))
        if (str.equals("CONTROL")) {
          CONTROL = value
        }
        if (str.equals("METRO3")) {
          METRO3 = value
        }
        if (str.equals("ZINC2")) {
          ZINC2 = value
        }
        if (str.equals("VALUE")) {
          ROOMS = value
        }
        if (str.equals("ZSMHC")) {
          REGION = value
        }
        if (str.equals("BUILT")) {
          NUNITS = value
        }
      }

      list.add(CONTROL)
      list.add(METRO3)
      list.add(ZINC2)
      list.add(ROOMS)
      list.add(REGION)
      list.add(NUNITS)
      list
    })
    //    将RDD转换为List
    val list: List[util.ArrayList[String]] = rowRDD.take(page * 10).toList

    //    将scala的List转换为java的List
    val java: util.List[util.ArrayList[String]] = list.asJava

    //    获取数据的总条数
    var count = java.size()
    if (count < 10) {
      count = 10
    }
    //    截取需要的页码的数据
    //    判断页数是否为最后一页（最后一页的下标算法可能造成下标越界）
    val rows: util.List[util.ArrayList[String]] = java.subList(count - 10, java.size())

    (count, rows)
  }

  def fiterBuild(v: RDD[Result], builds: String, rates: String): RDD[Result] = {
    //根据建成年份及城市等级将Result转换为（年份区间，城市等级，Result）的RDD
    val vvv: RDD[(String, String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //      获取BUILT与METRO3两列的值
      var build: Int = 0;
      var rate: Int = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value: String = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("BUILT")) {
          build = Integer.parseInt(value)
        }
        if (name.equals("ZSMHC")) {
          rate = Integer.parseInt(value)
        }
      }
      //      将具体的建成年份及城市规模数据替换为（年份区间，区域，Result）的元祖
      if (build < 2000) {
        if (rate < 500) {
          ("1900-2000 ", "0-500", x)
        } else if (rate < 1000) {
          ("1900-2000", "500-1000", x)
        } else if (rate < 1500) {
          ("1900-2000", "1000-1500", x)
        } else if (rate < 2000) {
          ("1900-2000", "1500-2000", x)
        } else if (rate < 2500) {
          ("1900-2000", "2000-2500", x)
        } else if (rate < 3000) {
          ("1900-2000", "2500-3000", x)
        } else if (rate < 3500) {
          ("1900-2000", "3000-3500", x)
        } else {
          ("1900-2000", "3500+", x)
        }
      } else if (build < 2010 && build > 2000) {
        if (rate < 500) {
          ("2000-2010", "0-500", x)
        } else if (rate < 1000) {
          ("2000-2010", "500-1000", x)
        } else if (rate < 1500) {
          ("2000-2010", "1000-1500", x)
        } else if (rate < 2000) {
          ("2000-2010", "1500-2000", x)
        } else if (rate < 2500) {
          ("2000-2010", "2000-2500", x)
        } else if (rate < 3000) {
          ("2000-2010", "2500-3000", x)
        } else if (rate < 3500) {
          ("2000-2010", "3000-3500", x)
        } else {
          ("1900-2000", "3500+", x)
        }
      } else {
        if (rate < 500) {
          ("2010-2018", "0-500", x)
        } else if (rate < 1000) {
          ("2010-2018", "500-1000", x)
        } else if (rate < 1500) {
          ("2010-2018", "1000-1500", x)
        } else if (rate < 2000) {
          ("2010-2018", "1500-2000", x)
        } else if (rate < 2500) {
          ("2010-2018", "2000-2500", x)
        } else if (rate < 3000) {
          ("2010-2018", "2500-3000", x)
        } else if (rate < 3500) {
          ("2010-2018", "3000-3500", x)
        } else {
          ("2010-2018", "3500+", x)
        }
      }
    })
    //    过滤符合传入条件的Result数据
    val result: RDD[Result] = vvv.filter({
      case (x: String, y: String, z: Result) => {
        //        判断搜索条件为一个还是两个或者没有搜索条件
        //        两个搜索条件
        if (builds != null && rates != null) {
          x.equals(builds) && y.equals(rates)
          //        一个搜索条件
        } else if (builds != null && rates == null) {
          x.equals(builds)
          //        一个搜索条件
        } else if (builds == null && rates != null) {
          y.equals(rates)
          //          没有搜索条件
        } else {
          true
        }
      }
      //        将（建成年份区间，城市区域，Result）的元祖的RDD转换为Result的RDD
    }).map(_._3)
    result
  }

  def fiterSingle(v: RDD[Result], regions: String, singles: String): RDD[Result] = {
    //根据建成年份及城市等级将Result转换为（年份区间，城市等级，Result）的RDD
    val vvv: RDD[(String, String, Result)] = v.map(x => {
      val cells: Array[Cell] = x.rawCells();
      //      获取BUILT与METRO3两列的值
      var region: Int = 0;
      var single: Int = 0;
      for (elem <- cells) {
        val name: String = Bytes.toString(CellUtil.cloneQualifier(elem));
        val value: String = Bytes.toString(CellUtil.cloneValue(elem));
        if (name.equals("REGION")) {
          region = Integer.parseInt(value)
        }
        if (name.equals("NUNITS")) {
          single = Integer.parseInt(value)
        }
      }
      if (region == 1) {
        if (single < 2) {
          ("1 ", "是", x)
        } else {
          ("1", "否", x)
        }
      }
      else if (region == 2) {
        if (single < 2) {
          ("2 ", "是", x)
        } else {
          ("2", "否", x)
        }
      } else if (region == 3) {
        if (single < 2) {
          ("3 ", "是", x)
        } else {
          ("3", "否", x)
        }
      } else {
        if (single < 2) {
          ("4 ", "是", x)
        } else {
          ("4", "否", x)
        }
      }
    })
    //    过滤符合传入条件的Result数据
    val result: RDD[Result] = vvv.filter({
      case (x: String, y: String, z: Result) => {
        //        判断搜索条件为一个还是两个或者没有搜索条件
        //        两个搜索条件
        if (regions != null && singles != null) {
          x.equals(regions) && y.equals(singles)
          //        一个搜索条件
        } else if (regions != null && singles == null) {
          x.equals(regions)
          //        一个搜索条件
        } else if (regions == null && singles != null) {
          y.equals(regions)
          //          没有搜索条件
        } else {
          true
        }
      }
      //        将（是否独栋，城市区域，Result）的元祖的RDD转换为Result的RDD
    }).map(_._3)
    result

  }

}

