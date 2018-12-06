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

import scala.collection.immutable.HashMap

object RoomByCity02 {
  def main(args: Array[String]): Unit = {
   val room = new RoomByCity02
     room.selfOrRent("2013")
    println("1::success")
   // println("===============")
   //room.RoomAndBedRom("2013")
  // println("1::success")
    //println("----------------")
   //room. SingleBuildByYear("2013");
    //print("asdcvc".split(";")(0))
  }


}

case class Rooms(city:Int ,room:Int,bedrom:Int)


class RoomByCity02 extends Analysis {

  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = new SparkContext(sparkConf)
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()


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
    //获得Mysql的链接
    var msconn: sql.Connection = DBUtil.getConn
    //定义一个Map   将查询的结果存入该Map  传入setIntoMysqlTable中
    var selfOrRentMap = Map[String, Int]()


    val value: RDD[(String, Int)] = hbaseRDD.map(x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "OWNRENT".getBytes())))
    }).map(x => {
      (x, 1)
    }).reduceByKey(_ + _)
    //收集结果  封装进Map
    value.collect().foreach(selfOrRentMap += (_))
   //调用传入MySQLde 方法
    setIntoMysqlTable(selfOrRentMap, tableName)
  }

  /**
    * 插入mysql
    */
  def setIntoMysqlTable(tenureMap: Map[String, Int], tableName: String): Unit = {
    val msconn1: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl
    try {
      //定义下面方法要返回的变量    下方用于判断
      var reportId = 0;
      var diagramId = 0;
      var legendId = 0;
      var xId = 0;
      var yId = 0;

      //开启事物
      msconn1.setAutoCommit(false)

      //插入report表
      val rName: String = "住房自住、租赁分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "basic"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      reportId= table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = tableName
      val dText = "居住状态图"
      diagramId = table.insertIntoDiagram(dName, 2, reportId, dText)

      //图例
      legendId  = table.insertIntoLegend("自住、租赁图例","自住",diagramId)

      //插入X轴
      val xName: String = "自住\\租赁"
       xId= table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      yId= table.insertIntoYAxis("222", diagramId)
      val yId1 = -1;


      //插入数据集表
      val ddId: Int = table.insertIntoDimension("basic", "自住、租赁状态", "TENURE")

      //数据表   tenureMap：Map
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1,"自住、租赁图例")
      }
      //判断是否插入成功
      if(reportId>0&& diagramId>0&& legendId>0&& xId>0&& yId>0) {
        msconn1.commit();
        return true;
      }
    } catch {
      case e: Exception => {
        //回滚事务
        try {
          msconn1.rollback();
        } catch {
          case e1: SQLException => e1.printStackTrace()
        }
        e.printStackTrace();
      }
    } finally {
      DBUtil.close(msconn1);
    }
  }

  /**
    * 城市规模统计：房间总数和卧室数
    */

  def RoomAndBedRom(tableName: String): Unit = {
    //MySQL链接
    var msconn: sql.Connection = DBUtil.getConn

    var RoomAndBedRom = Map[String, String]()
    import spark.implicits._
    val value = hbaseRDD.map { x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "METRO3".getBytes())).toInt, (
        Bytes.toString(x._2.getValue("info".getBytes(), "ROOMS".getBytes())).toInt,
        Bytes.toString(x._2.getValue("info".getBytes(), "BEDRMS".getBytes())).toInt)
      )
    }
    }.filter(_._2._1.toInt>0).filter(_._2._2.toInt>0).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).map{x=>
      (x._1,x._2._1,x._2._2)
    }.map(x=>Rooms(x._1,x._2,x._3))
    val frame = value.toDF()
    frame.createOrReplaceTempView("tmp")
    val dataFrame = spark.sql("select city,room,bedrom from tmp")
    val rdd1 = dataFrame.rdd.map(x => (x.get(0).toString,x.get(1)+"-"+x.get(2)))

    rdd1.collect().foreach(RoomAndBedRom+=(_))

    //导入mysql
    RoomAndBedRomToMysql(RoomAndBedRom, tableName)
  }

  /**
    * 房间数，卧室数   封装
    */

  def  RoomAndBedRomToMysql(tenureMap: Map[String, String], tableName: String): Unit = {
    var msconn: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl

    try {
      var reportId = 0;
      var diagramId = 0;
      var legendId = 0;
      var xId = 0;
      var yId = 0;
      var ddId = 0;
      msconn.setAutoCommit(false)
      //插入report表
      val rName: String = "房间数、卧室数分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "basic"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      reportId= table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = tableName
      val dText = "房间状态图"
      diagramId = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例
      legendId = table.insertIntoLegend("总房间数、卧室数","总房间数",diagramId)

      //插入X轴
      val xName: String = "房屋类型"
      xId = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      yId = table.insertIntoYAxis("222", diagramId)


      //插入数据集表
      ddId = table.insertIntoDimension("basic", "总房数、卧室数分布", "ROOMS")

      //数据表
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.split("-")(0),xId,legendId,elem._1,"总房数")
        table.insertIntoData(elem._2.split("-")(1),xId,legendId,elem._1,"卧室数")
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
  def SingleBuildByYear(tableName: String): Unit = {
    var msconn: sql.Connection = DBUtil.getConn
    var singleMap = Map[String, Int]()
    //链接hbase
    val hconf = HBaseConfiguration.create()
    //val tableName = "2013"
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    //获取数据
    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    //获得需要得列
    val value= hbaseRDD.map(x => {
      ((Bytes.toString(x._2.getValue("info".getBytes(), "BUILT".getBytes()))).toInt,
        (Bytes.toString(x._2.getValue("info".getBytes(), "STRUCTURETYPE".getBytes()))).toInt)
    }).map{x=>{
      //独栋建筑
      if(x._2==1){
        if (x._1 >= 1900 && x._1 < 2000) {
          ("1900-2000", x._2)
        } else if (x._1 >= 2000 && x._1 <= 2010) {
          ("2000-2010", x._2)
        } else {
          ("2010-2018", x._2)
        }
      }else{
        //非独栋建筑
        if (x._1 >= 1900 && x._1 < 2000) {
          ("1900-2000;else", x._2)
        } else if (x._1 >= 2000 && x._1 <= 2010) {
          ("2000-2010;else", x._2)
        } else {
          ("2010-2018;else", x._2)
        }
      }
    }}.reduceByKey(_+_)
    val dfs = value.collect().foreach(singleMap+=(_))
    //val dfs = value.collect().foreach(singleMap+=(_))

    //将封装好的Map传入 SingleBuildToHtml 方法中，封装成网页需要的数据
    singleToHTML(singleMap,tableName);

  }

  /**
    * 封装到网页
    */
  def singleToHTML(singleMap: Map[String, Int], tableName: String): Unit = {
    var msconn: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      var reportID = 0;
      var digID = 0;
      var legID = 0;
      var xID = 0;
      var yID = 0;



      //插入report表
      val name = "独栋建筑图";
      val create = System.currentTimeMillis();
      val year = 2013
      val group = "111"
      val status = 0
      val url = "http://166.166.2.111/vacancy";
      reportID = table.insertIntoReport(name, create, year, group, status, url)

      //插入diagram表
      val dname = "独栋建筑图"
      val dtype = 1
      val dreportID = reportID
      val dsubtext = "独栋建筑图"
      digID = table.insertIntoDiagram(dname, dtype, dreportID, dsubtext)

      //插入X表   XAxis(String name,int diagramId,String dimGroupName)
      val xname = "年份区间"
      val xdig = digID
      val xdgn = "111"
      xID = table.insertIntoXAxis(xname, xdig, xdgn)

      //插入Y轴    insertIntoYAxis(String name,int diagramId)
      yID = table.insertIntoYAxis("建筑数量", digID)

      //插入dimention
      table.insertIntoDimension("房屋建成年份", "独栋建筑", "STRUCTURE")
      table.insertIntoDimension("房屋建成年份", "非独栋建筑", "STRUCTURE")

      //插入legend表    图例
      legID = table.insertIntoLegend("房屋独栋建筑", "独栋建筑", digID)


      //插入data表   String value,int xId,int legendId,String x,String legend
      for (elem <- singleMap) {
        if(elem._1.split(";").size>1){
          table.insertIntoData(elem._2.toString, xID, legID, elem._1.split(";")(0), "其他建筑")
        }else{
          table.insertIntoData(elem._2.toString, xID, legID, elem._1.split(";")(0), "独栋建筑")
        }
      }
      if(reportID>0&& digID>0&& legID>0&& xID>0&& yID>0){
        msconn.commit();
        return true;
      }

   }catch {
      case e: Exception => {
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
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = ???
}