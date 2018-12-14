package com.tecode.house.liaolian.dao.impl

import java.util

import com.tecode.house.liaolian.dao.LLHbaseDao
import com.tecode.house.lijin.utils.SparkUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.JavaConverters._


class LLHbaseDaoImpl extends LLHbaseDao {

  /**
    * 查询家庭收入，根据城市查询
    *
    * @return 查询结果
    */
  override def getIncome(tableName: String, city: Int,page:Int): List[(Int, (String, Int, Int))] = {

    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2==city)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("2人以下", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else {
        ("5人以上", (x._1, x._2, x._3))
      }
    })

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }

//    sc.stop()

    list

  }

  override def getIncome(tableName: String, income: String,page:Int): List[(Int, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("2人以下", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else {
        ("5人以上", (x._1, x._2, x._3))
      }
    }).filter(_._1.equals(income))

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }

//    sc.stop()

    list
  }


  /**
    * 查询家庭收入,没有查询条件
    * @return 查询结果
    */
  override def getIncome(talbeName: String, page: Int): List[(String, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, talbeName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")


//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(String, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("2人以下", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else {
        ("5人以上", (x._1, x._2, x._3))
      }
    })

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt+"_"+tuple._1,tuple._2)

    }


//    sc.stop()

    list
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


//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2 == city.toInt)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 2) {
        ("2人以下", (x._1, x._2, x._3))
      } else if (i < 3) {
        ("2人", (x._1, x._2, x._3))
      } else if (i < 4) {
        ("3人", (x._1, x._2, x._3))
      } else if (i < 5) {
        ("4人", (x._1, x._2, x._3))
      } else if (i < 6) {
        ("5人", (x._1, x._2, x._3))
      } else {
        ("5人以上", (x._1, x._2, x._3))
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

  /**
    * 截取list
    * @param p
    * @param count
    * @param javaList
    * @return
    */
  def setSublist(p:Int,count:Int,javaList: util.List[(String, (String, Int, Int))]): util.List[(String, (String, Int, Int))] ={
    if(count<10){
      javaList.subList(0,count)
    }else{
      if(p*10>count){
        javaList.subList(count-10,count)

      }else{
        javaList.subList((p-1)*10,p*10)
      }
    }
    }


  /**
    *
    * 获取家庭人数，根据城市等级查询
    * @param tableName
    * @param city
    * @param page
    * @return 查询结果
    */

  override def getPerson(tableName: String,city:Int, page: Int): List[(Int, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2==city)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 500) {
        ("1万以内", (x._1, x._2, x._3))
      } else if (i < 1000 && i > 500) {
        ("2万以内", (x._1, x._2, x._3))
      } else if (i < 1500 && i > 500) {
        ("3万以内", (x._1, x._2, x._3))
      } else if (i < 2000 && i > 1500) {
        ("4万以内", (x._1, x._2, x._3))
      } else if (i < 3000 && i > 2500) {
        ("5万以内", (x._1, x._2, x._3))
      } else if (i < 4000 && i > 3500) {
        ("6万以内", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
      }
    })
    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }


//    sc.stop()

    list

  }

  /**
    * 获取家庭人数,根据人数进行查询
    *
    * @param year   要查询的年份
    * @param person 要查询的人数
    * @return 查询结果
    */
  override def getPerson(year: String, person: String,page:Int): List[(Int, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, year)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))).toInt

    )).filter(_._3.toInt > 0)
    var list:List[(Int, (String, Int, Int))]=List()

    val s2 = s.map(x => {
      val i = x._3
      if (i < 500) {
        ("1万以内", (x._1, x._2, x._3))
      } else if (i < 1000 && i > 500) {
        ("2万以内", (x._1, x._2, x._3))
      } else if (i < 1500 && i > 500) {
        ("3万以内", (x._1, x._2, x._3))
      } else if (i < 2000 && i > 1500) {
        ("4万以内", (x._1, x._2, x._3))
      } else if (i < 3000 && i > 2500) {
        ("5万以内", (x._1, x._2, x._3))
      } else if (i < 4000 && i > 3500) {
        ("6万以内", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
      }
    }).filter(_._1.equals(person))

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt,tuple._2)

    }


   list
  }

  /**
    */
  override def getPerson(tableName: String, page: Int):List[(String, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(String, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 500) {
        ("1万以内", (x._1, x._2, x._3))
      } else if (i < 1000 && i > 500) {
        ("2万以内", (x._1, x._2, x._3))
      } else if (i < 1500 && i > 500) {
        ("3万以内", (x._1, x._2, x._3))
      } else if (i < 2000 && i > 1500) {
        ("4万以内", (x._1, x._2, x._3))
      } else if (i < 3000 && i > 2500) {
        ("5万以内", (x._1, x._2, x._3))
      } else if (i < 4000 && i > 3500) {
        ("6万以内", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
      }
    })

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value = tuples.iterator()

    while (value.hasNext) {
      val tuple = value.next()
      list:+=(count.toInt+"_"+tuple._1,tuple._2)

    }


//    sc.stop()

    list

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

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))).toInt

    )).filter(_._3.toInt > 0).filter(_._2 == city.toInt)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 500) {
        ("1万以内", (x._1, x._2, x._3))
      } else if (i < 1000 && i > 500) {
        ("2万以内", (x._1, x._2, x._3))
      } else if (i < 1500 && i > 500) {
        ("3万以内", (x._1, x._2, x._3))
      } else if (i < 2000 && i > 1500) {
        ("4万以内", (x._1, x._2, x._3))
      } else if (i < 3000 && i > 2500) {
        ("5万以内", (x._1, x._2, x._3))
      } else if (i < 4000 && i > 3500) {
        ("6万以内", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
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
    * 查询市场价,无查询结果
    *
    * @return 查询结果
    */
  override def getValue(tableName:String,page:Int):List[(String, (String, Int, Int))] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(String, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 10000) {
        ("0-1万", (x._1, x._2, x._3))
      } else if (i < 20000 && i >10000) {
        ("2万-3万", (x._1, x._2, x._3))
      } else if (i < 30000 && i >20000) {
        ("3万-4万", (x._1, x._2, x._3))
      } else if (i < 40000 && i >30000) {
        ("4万-5万", (x._1, x._2, x._3))
      } else if (i < 50000 && i >40000) {
        ("5万-6万", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
      }
    })

    val javaList = s2.take(page*10).toList.asJava
    val count: Long = s2.count()
    val tuples = setSublist(page,count.toInt,javaList)
    val value2 = tuples.iterator()

    while (value2.hasNext) {
      val tuple = value2.next()
      list:+=(count.toInt+"_"+tuple._1,tuple._2)

    }


//    sc.stop()
    list

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


//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))).toInt,
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))).toInt

    )).filter(_._3.toInt > 0)

    var list:List[(Int, (String, Int, Int))]=List()
    val s2 = s.map(x => {
      val i = x._3
      if (i < 10000) {
        ("0-1万", (x._1, x._2, x._3))
      } else if (i < 20000 && i >10000) {
        ("2万-3万", (x._1, x._2, x._3))
      } else if (i < 30000 && i >20000) {
        ("3万-4万", (x._1, x._2, x._3))
      } else if (i < 40000 && i >30000) {
        ("4万-5万", (x._1, x._2, x._3))
      } else if (i < 50000 && i >40000) {
        ("5万-6万", (x._1, x._2, x._3))
      } else {
        ("6万以上", (x._1, x._2, x._3))
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
//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

    var map: Map[String, Int] = Map()

    val s = hbaseRDD.map(x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZINC2")))

    ).map(_.toInt).filter(_ > 1)

    val s2 = s.map(i => {

      if (i < 5000) {
        ("0-1万", 1)
      } else if (i < 10000 && i > 5000) {
        ("2万-3万", 1)
      } else if (i < 15000 && i > 10000) {
        ("3万-4万", 1)
      } else if (i < 20000 && i > 15000) {
        ("4万-5万", 1)
      } else if (i < 25000 && i > 20000) {
        ("5万-6万", 1)
      } else {
        ("6万以上", 1)
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
    * 统计税费的分布情况
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  override def getPersonDistribution(tableName: String): Map[String, Int] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc = SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x =>

      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC")))

    ).map(_.toInt).filter(_ > 0)

    var map: Map[String, Int] = Map()

    val s2 = s.map(i => {


      if (i < 500) {
        ("1万以内", 1)
      } else if (i < 1000  && i > 500) {
        ("2万以内", 1)
      } else if (i < 1500  && i > 1000) {
        ("3万以内", 1)
      } else if (i < 2000  && i > 1500) {
        ("4万以内", 1)
      } else if (i < 3000  && i > 2500) {
        ("5万以内", 1)
      } else if (i < 4000  && i > 3500) {
        ("6万以内", 1)
      } else {
        ("6万以上", 1)
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
    * 按照城市统计家庭人数
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  override def getIncomeDistributionByCity(tableName: String): Map[String, Int] = {
    val conf = HBaseConfiguration.create()
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMN_FAMILY, "info")

//    val spconf = new SparkConf().setMaster("local[*]").setAppName("hbase")
    val sc =SparkUtil.getSparkContext
    val hbaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val s = hbaseRDD.map(x => (
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("REGION"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("PER"))).toInt

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
      if (i < 2) {
        (x._1 + "_2人以下", 1)
      } else if (i < 3 && i>=2 ) {
        (x._1 + "_2人", 1)
      } else if (i < 4 && i>=3) {
        (x._1 + "_3人", 1)
      } else {
        (x._1 + "_3人以上", 1)
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
