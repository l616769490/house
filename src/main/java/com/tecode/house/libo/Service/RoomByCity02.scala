package com.tecode.house.libo.Service

import java.sql
import java.sql.{Connection, SQLException}

import com.tecode.house.d01.service.Analysis
import com.tecode.house.libo.dao.impl.MysqlDaoImpl
import com.tecode.house.libo.util.DBUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object RoomByCity02 {
  def main(args: Array[String]): Unit = {
   val room = new RoomByCity02
    // room.selfOrRent("2013")
   room.RoomAndBedRom()
  }


}

case class Rooms(city:Int ,room:Int,bedrom:Int)
class RoomByCity02 extends Analysis {

  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = new SparkContext(sparkConf)
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()
  var msconn: sql.Connection = DBUtil.getConn

  val tableName = "2013"
  //链接hbase
  val hconf = HBaseConfiguration.create()
  hconf.set(TableInputFormat.INPUT_TABLE, tableName)
  hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

  val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
    classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
    classOf[org.apache.hadoop.hbase.client.Result])


  /**
    * 自住\租赁
    */
  def selfOrRent(tableName: String): Unit = {

    import scala.collection.JavaConverters._
    var selfOrRentMap = Map[String, Int]()

    val value: RDD[(String, Int)] = hbaseRDD.map(x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "OWNRENT".getBytes())))
    }).map(x => {
      (x, 1)
    }).reduceByKey(_ + _)
    //value.collect().foreach(println)
    value.collect().foreach(selfOrRentMap += (_))
//    //导入mysql
    setIntoMysqlTable(selfOrRentMap, tableName)
  }

  /**
    * 插入mysql
    *
    * @param vacancyMap
    * @param tableName
    */
  def setIntoMysqlTable(tenureMap: Map[String, Int], tableName: String): Unit = {
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      //插入report表
      val rName: String = "住房自住、租赁分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "basic"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      val reportId: Int = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = tableName
      val dText = "居住状态图"
      val diagramId: Int = table.insertIntoDiagram(dName, 2, reportId, dText)

      //图例
      val legendId:Int = table.insertIntoLegend("自住、租赁图例","自住",diagramId)

      //插入X轴
      val xName: String = "自住\\租赁"
      val xId: Int = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      val yId: Int = table.insertIntoYAxis("222", diagramId)


      //插入数据集表
      val ddId: Int = table.insertIntoDimension("basic", "自住、租赁状态", "TENURE")

      //数据表
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1,"自住、租赁图例")
      }

    } catch {
      case e: Exception => {
        println("=====b=====")
        //回滚事务
        try {
          msconn.rollback();
        } catch {
          case e1: SQLException => e1.printStackTrace()
        }
        e.printStackTrace();
      }
    } finally {
      DBUtil.close(msconn);
    }
  }


  /**
    * 城市规模统计：房间总数和卧室数
    */

  def RoomAndBedRom(): Unit = {

    val tableName = "2013"
//    val sparkConf = new SparkConf().setAppName("HbaseApp").setMaster("local[*]");
//    val sc = new SparkContext(sparkConf);

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    conf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //获取数据
    val hBaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    var RoomAndBedRom = Map[String, Int]()
    import spark.implicits._
    val value = hBaseRDD.map { x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "METRO3".getBytes())).toInt, (
        Bytes.toString(x._2.getValue("info".getBytes(), "ROOMS".getBytes())).toInt,
        Bytes.toString(x._2.getValue("info".getBytes(), "BEDRMS".getBytes())).toInt)
      )
      //(x,(y,z))
    }
    }.filter(_._2._1.toInt>0).filter(_._2._2.toInt>0).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).map{x=>
      (x._1,x._2._1,x._2._2)
    }.map(x=>Rooms(x._1,x._2,x._3))
    val frame = value.toDF()
    frame.createOrReplaceTempView("tmp")
    val dataFrame = spark.sql("select city,room,bedrom from tmp")
    val rdd1 = dataFrame.rdd.map(x => (x.get(0).toString,x.get(1)+"-"+x.get(2)))


    rdd1.collect().foreach(println)
    //spark.sql("select city,houseDuty from tmp  order by houseDuty desc ").show()
    spark.stop()
   //setIntoMysqlTableHouseDuty(cmap,"房产税柱状图")

   // value.collect().foreach(RoomAndBedRom += (_))
    //导入mysql
    //setIntoMysqlTable(RoomAndBedRom, tableName)

  }

  /**
    * 房间数，卧室数
    */

  def  RoomAndBedTable(tenureMap: Map[String, Int], tableName: String): Unit = {
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      //插入report表
      val rName: String = "住房自住、租赁分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "basic"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      val reportId: Int = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = tableName
      val dText = "居住状态图"
      val diagramId: Int = table.insertIntoDiagram(dName, 2, reportId, dText)

      //图例
      val legendId:Int = table.insertIntoLegend("自住、租赁图例","自住",diagramId)

      //插入X轴
      val xName: String = "自住\\租赁"
      val xId: Int = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      val yId: Int = table.insertIntoYAxis("222", diagramId)


      //插入数据集表
      val ddId: Int = table.insertIntoDimension("basic", "自住、租赁状态", "TENURE")

      //数据表
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1,"自住、租赁图例")
      }

    } catch {
      case e: Exception => {
        println("=====b=====")
        //回滚事务
        try {
          msconn.rollback();
        } catch {
          case e1: SQLException => e1.printStackTrace()
        }
        e.printStackTrace();
      }
    } finally {
      DBUtil.close(msconn);
    }
  }

  /**
    * 按照建成年份：统计独栋建筑比例
    */
  def SingleBuildByYear(): Unit = {
    //spark配置与conf
    val conf = new SparkConf().setAppName("SingleBuildByYear").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //链接hbase
    val hconf = HBaseConfiguration.create()
    val tableName = "2013"
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //获取数据
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    //获得需要得列
    val b = hbaseRDD.map(x => {
      ((Bytes.toString(x._2.getValue("info".getBytes(), "BUILT".getBytes()))).toInt,
        (Bytes.toString(x._2.getValue("info".getBytes(), "STRUCTURETYPE".getBytes()))).toInt)
    }).filter(x => x._2 == 1).map(x => {
      if (x._1 >= 1900 && x._1 < 2000) {
        ("1900-2000", x._2)
      } else if (x._1 >= 2000 && x._1 <= 2010) {
        ("2000-2010", x._2)
      } else {
        ("2010-2018", x._2)
      }
    }).reduceByKey(_ + _).foreach(println)


    /*
    1900-2000
    2000-2010
    2010-2018


     */


  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = ???
}
