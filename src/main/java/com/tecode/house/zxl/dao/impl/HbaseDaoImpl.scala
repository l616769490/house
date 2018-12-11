package com.tecode.house.zxl.dao.impl


import java.util

import com.tecode.house.zxl.dao.HbaseDao
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._


class HbaseDaoImpl extends HbaseDao {

  /**
    * 查询家庭收入
    *
    * @return 查询结果
    */
  override def getIncome(): Map[String, Iterable[(String, Int, Int)]] = {

    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 50000) {
        ("5万以下", (x._1, x._2, x._3))
      } else if (i < 100000) {
        ("5万-10万", (x._1, x._2, x._3))
      } else if (i < 150000) {
        ("10万-15万", (x._1, x._2, x._3))
      } else if (i < 200000) {
        ("15万-20万", (x._1, x._2, x._3))
      } else if (i < 250000) {
        ("20万-25万", (x._1, x._2, x._3))
      } else {
        ("25万以上", (x._1, x._2, x._3))
      }
    }).groupByKey()
    s2.collect().foreach(map += (_))

//    sc.stop()

    map

  }

  /**
    * 查询家庭收入，根据收入区间查询
    *
    * @param year   要查询的年份
    * @param income 收入区间
    * @return 查询结果
    */
  override def getIncome(year: String, income: String): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, year)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 50000) {
        ("5万以下", (x._1, x._2, x._3))
      } else if (i < 100000) {
        ("5万-10万", (x._1, x._2, x._3))
      } else if (i < 150000) {
        ("10万-15万", (x._1, x._2, x._3))
      } else if (i < 200000) {
        ("15万-20万", (x._1, x._2, x._3))
      } else if (i < 250000) {
        ("20万-25万", (x._1, x._2, x._3))
      } else {
        ("25万以上", (x._1, x._2, x._3))
      }
    }).filter(_._1.equals(income)).groupByKey()
    s2.collect().foreach(map+=(_))

//    sc.stop()

    map
  }


  /**
    * 查询家庭收入，根据城市等级查询
    *
    * @param year 要查询的年份
    * @param city 要查询的城市等级
    * @return 查询结果
    */
  override def getIncome(year: String, city: Int): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, year)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2 == city)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 50000) {
        ("5万以下", (x._1, x._2, x._3))
      } else if (i < 100000) {
        ("5万-10万", (x._1, x._2, x._3))
      } else if (i < 150000) {
        ("10万-15万", (x._1, x._2, x._3))
      } else if (i < 200000) {
        ("15万-20万", (x._1, x._2, x._3))
      } else if (i < 250000) {
        ("20万-25万", (x._1, x._2, x._3))
      } else {
        ("25万以上", (x._1, x._2, x._3))
      }
    }).groupByKey()
    s2.collect().take(100).foreach(map += (_))

//    sc.stop()

    map
  }

  /**
    * 查询家庭收入，根据城市等级和收入区间查询
    *
    * @param tableName   要查询的表
    * @param income 要查询的收入区间
    * @param city   要查询的城市等级
    * @return 查询结果
    */
  override def getIncome(tableName: String, income: String, city: String,p:Int): List[(Int, (String, Int, Int))]  = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2 == city.toInt)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 50000) {
        ("5万以下", (x._1, x._2, x._3))
      } else if (i < 100000) {
        ("5万-10万", (x._1, x._2, x._3))
      } else if (i < 150000) {
        ("10万-15万", (x._1, x._2, x._3))
      } else if (i < 200000) {
        ("15万-20万", (x._1, x._2, x._3))
      } else if (i < 250000) {
        ("20万-25万", (x._1, x._2, x._3))
      } else {
        ("25万以上", (x._1, x._2, x._3))
      }
    }).filter(_._1.equals(income))

    val javaList = s2.take(p*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(p,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }



//    sc.stop()

    list
  }

  def setSublist(p:Int,count:Int,javaList: util.List[(String, (String, Int, Int))]): util.List[(String, (String, Int, Int))] ={
    if(p*10>count){
      javaList.subList(count-10,count)

    }else{
      javaList.subList((p-1)*10,p*10)
    }
  }

  /**
    * 获取家庭人数
    *
    * @return 查询结果
    */
  override def getPerson(): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt > 0)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("1人", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else if (i < 7) {
        ("6人", (x._1, x._2, x._3))
      } else {
        ("6人以上", (x._1, x._2, x._3))
      }
    }).groupByKey()

    s2.collect().take(100).foreach(map += (_))

//    sc.stop()

    map
  }

  /**
    * 获取家庭人数,根据人数进行查询
    *
    * @param year   要查询的年份
    * @param person 要查询的人数
    * @return 查询结果
    */
  override def getPerson(year: String, person: String): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, year)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt > 0)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("1人", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else if (i < 7) {
        ("6人", (x._1, x._2, x._3))
      } else {
        ("6人以上", (x._1, x._2, x._3))
      }
    }).groupByKey()
    s2.filter(_._1.equals(person)).collect().foreach(map += (_))

//    sc.stop()

     map
  }

  /**
    * 获取家庭人数,根据城市等级进行查询
    *
    * @param year 要查询的年份
    * @param city 要查询的城市等级
    * @return 查询结果
    */
  override def getPerson(year: String, city: Int): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, year)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt > 0).filter(city.toInt == _._2)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("1人", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else if (i < 7) {
        ("6人", (x._1, x._2, x._3))
      } else {
        ("6人以上", (x._1, x._2, x._3))
      }
    }).groupByKey()
    s2.collect().take(100).foreach(map += (_))

//    sc.stop()
     map
  }

  /**
    * 获取家庭人数,根据城市等级和家庭人数进行查询
    *
    * @param tableName   要查询的表
    * @param city   要查询的城市等级
    * @param person 要查询的年份
    * @return 查询结果
    */
  override def getPerson(tableName: String, person: String, city: String,p:Int):List[(Int, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2 == city.toInt)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("1人", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else if (i < 7) {
        ("6人", (x._1, x._2, x._3))
      } else {
        ("6人以上", (x._1, x._2, x._3))
      }

    }).filter(_._1.equals(person))
    val javaList = s2.take(p*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(p,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }


//    sc.stop()
    list
  }

  /**
    * 查询市场价
    *
    * @return 查询结果
    */
  override def getValue(): Map[String, Iterable[(String, Int, Int)]] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, "2013")
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE"))).toInt

    )).filter(_._3.toInt > 0)

    var map: Map[String, Iterable[(String, Int, Int)]] = Map()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 500000) {
        ("50万以下", (x._1, x._2, x._3))
      } else if (i < 1000000) {
        ("50万-100万", (x._1, x._2, x._3))
      } else if (i < 1500000) {
        ("100万-150万", (x._1, x._2, x._3))
      } else if (i < 2000000) {
        ("150万-200万", (x._1, x._2, x._3))
      } else if (i < 2500000) {
        ("200万-250万", (x._1, x._2, x._3))
      } else {
        ("250万以上", (x._1, x._2, x._3))
      }
    }).groupByKey()
    s2.collect().take(100).foreach(map += (_))

//    sc.stop()

    map
  }

  /**
    * 查询市场价，根据价格区间查询
    *
    * @param tableName  要查询的表
    * @param value 要查询的价格区间
    * @return 查询结果
    */
  override def getValue(tableName: String, value: String,p:Int): List[(Int, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 500000) {
        ("50万以下", (x._1, x._2, x._3))
      } else if (i < 1000000) {
        ("50万-100万", (x._1, x._2, x._3))
      } else if (i < 1500000) {
        ("100万-150万", (x._1, x._2, x._3))
      } else if (i < 2000000) {
        ("150万-200万", (x._1, x._2, x._3))
      } else if (i < 2500000) {
        ("200万-250万", (x._1, x._2, x._3))
      } else {
        ("250万以上", (x._1, x._2, x._3))
      }
    }).filter(_._1.equals(value))

    val javaList = s2.take(p*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(p,count.toInt,javaList)
    val value2 = tuples.iterator()

    while (value2.hasNext) {
      val tuple = value2.next()
      list:+=(count.toInt,tuple._2)

    }

//    sc.stop()

    list

  }

  /**
    * 统计市场价的分布情况
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  override def getValueDistribution(tableName: String): Map[String, Int] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")
    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

    var map: Map[String, Int] = Map()

    val s = hbaseRDD.map(x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VALUE")))

    ).map(_.toInt).filter(_ > 1)

    val s2 = s.map(i => {

      if (i < 500000) {
        ("0-50万", 1)
      } else if (i < 1000000) {
        ("50万-100万", 1)
      } else if (i < 1500000) {
        ("100万-150万", 1)
      } else if (i < 2000000) {
        ("150万-200万", 1)
      } else if (i < 2500000) {
        ("200万-250万", 1)
      } else {
        ("250万以上", 1)
      }
    }).reduceByKey(_ + _)

    val max = s.max()
    val min = s.min()

    val sum = s.sum()
    val count = s.count()
    val avg = sum / count

    s2.collect().foreach(x => map += ((x._1, x._2)))

    map += (("max", max))
    map += (("min", min))
    map += (("avg", avg.toInt))
//    sc.stop()
    map

  }

  /**
    * 统计家庭人数的分布情况
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  override def getPersonDistribution(tableName: String): Map[String, Int] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER")))

    ).map(_.toInt).filter(_ > 0)

    var map: Map[String, Int] = Map()

    val s2 = s.map(i => {


      if (i < 2) {
        ("1人", 1)
      } else if (i < 3) {
        ("2人", 1)
      } else if (i < 4) {
        ("3人", 1)
      } else if (i < 5) {
        ("4人", 1)
      } else if (i < 6) {
        ("5人", 1)
      } else if (i < 7) {
        ("6人", 1)
      } else {
        ("6人以上", 1)
      }
    }).reduceByKey(_ + _)

    val max = s.max()
    val min = s.min()

    val sum = s.sum()
    val count = s.count()
    val avg = sum / count
    s2.collect().foreach(map += (_))

    map += (("max", max))
    map += (("min", min))
    map += (("avg", avg.toInt))

//    sc.stop()

   map
  }
  /**
    * 按照城市统计家庭收入
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  override def getIncomeDistributionByCity(tableName: String): Map[String, Int] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = new SparkContext(spconf)
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("METRO3"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._2.toInt > 0).groupByKey()

    var map: Map[String, Int] = Map()

    val s2 = s.flatMap(x => {
      val a = x._2.iterator
      var list: List[(String, Int)] = List()
      while (a.hasNext) {
        list :+= (x._1, a.next())
      }
      list
    })

    val s3 = s2.map(x => {
      var i = x._2.toInt
      if (i < 50000) {
        (x._1 + "_10万以下", 1)
      } else if (i < 300000) {
        (x._1 + "_10万-30万", 1)
      } else if (i < 500000) {
        (x._1 + "_30万-50万", 1)
      } else {
        (x._1 + "_50万以上", 1)
      }
    }
    )
    val s4 = s3.reduceByKey(_ + _)

    s2.map(x => (x._1, x._2.toInt)).reduceByKey(math.max(_, _)).collect().foreach(x => map += ((x._1 + "_max", x._2)))

    s2.map(x => (x._1, x._2.toInt)).reduceByKey(math.min(_, _)).collect().foreach(x => map += ((x._1 + "_min", x._2)))

    s.map(x => (x._1, x._2.toList.sum / x._2.toList.size)).collect().foreach(x => map += ((x._1 + "_avg", x._2.toInt)))

    s4.collect().foreach(map += (_))


     map
  }
}
