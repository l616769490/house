package com.tecode.house.libo.Service.ServiceImpl

import java.{sql, util}
import java.sql.{Connection, SQLException}

import scala.collection.JavaConverters._
import com.google.protobuf.SingleFieldBuilder
import com.tecode.house.d01.service.Analysis
import com.tecode.house.libo.dao.impl.MysqlDaoImpl
import com.tecode.house.libo.util.DBUtil
import com.tecode.house.lijin.utils.SparkUtil
import com.tecode.table
import com.tecode.table.{Page, Search, Table}
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer

object RoomByCity02 {
  def main(args: Array[String]): Unit = {
    val room = new RoomByCity02
    //room.RoomAndBedRom("2013")
    room.selfOrRent("thads:2013",2013)
    room.RoomAndBedRom("thads:2013",2013)
    room. SingleBuildByYear("thads:2013",2013);
    //print("asdcvc".split(";")(0))
    //room.selfRentTable("2013","租赁")
    //room.roomsTable("2013","5","6")
    //room.singleTable("2013","1999","否",5)
  // room. RoomAndBedRom("2013")
  }


}

case class Rooms(city: Int, room: Int, bedrom: Int)


class RoomByCity02 extends Analysis {

  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = SparkUtil.getSparkContext
  val spark = SparkSession.builder().config(sc.getConf).getOrCreate()





  /**
    * 自住\租赁   RoomAndBedRom  selfOrRent
    */
  def selfOrRent(tableName: String,year:Int): Unit = {
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
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
    setIntoMysqlTable(selfOrRentMap, year)
  }

  /**
    * 自住\租赁     插入mysql
    */
  def setIntoMysqlTable(tenureMap: Map[String, Int], year: Int): Unit = {

    val msconn1: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl
    try {
      //定义下面方法要返回的变量    下方用于判断
      var reportId = 0;
      var diagramId = 0;
      var legendId = 0;
      var xId = 0;
      var yId = 0;
      var searchId = 0;

      //开启事物
      msconn1.setAutoCommit(false)

      //插入report表
      val rName: String = "住房自住租赁分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = year
      val rGroup: String = "基础分析"
      val rStatus: Int = 0
      val url: String = "/libo_self"
      reportId = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = "自住租赁"
      val dText = "居住状态图"
      diagramId = table.insertIntoDiagram(dName, 2, reportId, dText)

      //图例
      legendId = table.insertIntoLegend("自住租赁图例", "自住", diagramId)

      //插入X轴
      val xName: String = "自住租赁"
      xId = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      yId = table.insertIntoYAxis("222", diagramId)
      val yId1 = -1;


      //插入数据集表
      val ddId: Int = table.insertIntoDimension("basic", "自住租赁状态", "TENURE")

      //数据表   tenureMap：Map
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.toString, xId, legendId, elem._1, "自住租赁图例")
      }
      //插入搜索表
      searchId = table.insertIntoSearch("居住状态","自住",reportId)
      //判断是否插入成功
      if (reportId > 0 && diagramId > 0 && legendId > 0 && xId > 0 && yId > 0) {
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

  def RoomAndBedRom(tableName: String,year:Int): Unit = {
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
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
    }.filter(_._2._1.toInt > 0).filter(_._2._2.toInt > 0).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2)).map { x =>
      (x._1, x._2._1, x._2._2)
    }.map(x => Rooms(x._1, x._2, x._3))
    val frame = value.toDF()
    frame.createOrReplaceTempView("tmp")
    val dataFrame = spark.sql("select city,room,bedrom from tmp")
    val rdd1 = dataFrame.rdd.map(x => (x.get(0).toString, x.get(1) + "_" + x.get(2)))

    rdd1.collect().foreach(RoomAndBedRom += (_))
    //    for (elem <- RoomAndBedRom) {
    //      println(elem)
    //    }

    //导入mysql
    RoomAndBedRomToMysql(RoomAndBedRom, year)

  }

  /**
    * 房间数，卧室数   封装插入mysql
    */

  def RoomAndBedRomToMysql(tenureMap: Map[String, String], year: Int): Unit = {
    var msconn: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl

    try {
      var reportId = 0;
      var diagramId = 0;
      var searchId= 0
      var legendId = 0;
      var xId = 0;
      var yId = 0;
      var ddId = 0;
      msconn.setAutoCommit(false)
      //插入report表
      val rName: String = "房间数卧室数分析"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = year
      val rGroup: String = "城市规模"
      val rStatus: Int = 0
      val url: String = "/libo_room"
      reportId = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼图
      val dName = "房间数卧室数"
      val dText = "房间状态图"
      diagramId = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例
      legendId = table.insertIntoLegend("总房间数卧室数", "总房间数", diagramId)

      //插入X轴
      val xName: String = "房屋类型"
      xId = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      yId = table.insertIntoYAxis("222", diagramId)


      //插入数据集表
      ddId = table.insertIntoDimension("基础分析", "总房数、卧室数分布", "ROOMS")

      searchId = table.insertIntoSearch("房间卧室数","房间数",reportId)
      table.insertIntoSearch("城市等级","1",reportId)
      //数据表
      for (elem <- tenureMap) {
        table.insertIntoData(elem._2.split("_")(0), xId, legendId, elem._1, "总房数")
        table.insertIntoData(elem._2.split("_")(1), xId, legendId, elem._1, "卧室数")
      }

    } catch {
      case e: Exception => {
        //println("=====b=====")
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
  def SingleBuildByYear(tableName: String,year:Int): Unit = {
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    var msconn: sql.Connection = DBUtil.getConn
    var singleMap = Map[String, Int]()
    //    //链接hbase
    //    val hconf = HBaseConfiguration.create()
    //    //val tableName = "2013"
    //    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")



    //获得需要得列
    val value = hbaseRDD.map(x => {
      ((Bytes.toString(x._2.getValue("info".getBytes(), "BUILT".getBytes()))).toInt,
        (Bytes.toString(x._2.getValue("info".getBytes(), "STRUCTURETYPE".getBytes()))).toInt)
    }).map { x => {
      //独栋建筑
      if (x._2 == 1) {
        if (x._1 >= 1900 && x._1 < 2000) {
          ("1900-2000", x._2)
        } else if (x._1 >= 2000 && x._1 <= 2010) {
          ("2000-2010", x._2)
        } else {
          ("2010-2018", x._2)
        }
      } else {
        //非独栋建筑
        if (x._1 >= 1900 && x._1 < 2000) {
          ("1900-2000;else", x._2)
        } else if (x._1 >= 2000 && x._1 <= 2010) {
          ("2000-2010;else", x._2)
        } else {
          ("2010-2018;else", x._2)
        }
      }
    }
    }.reduceByKey(_ + _)
    val dfs = value.collect().foreach(singleMap += (_))
    //val dfs = value.collect().foreach(singleMap+=(_))

    //将封装好的Map传入 SingleBuildToHtml 方法中，封装成网页需要的数据
    singleToHTML(singleMap, year);
  }

  /**
    * 独栋建筑    封装到网页
    */
  def singleToHTML(singleMap: Map[String, Int], year1: Int): Unit = {
    var msconn: sql.Connection = DBUtil.getConn
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      var reportID = 0;
      var digID = 0;
      var legID = 0;
      var xID = 0;
      var yID = 0;
      var searchId= 0


      //插入report表
      val name = "独栋建筑图";
      val create = System.currentTimeMillis();
      val year = year1
      val group = "建成年份"
      val status = 0
      val url = "/libo_single";
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
      val xdgn = "建成年份"
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
        if (elem._1.split(";").size > 1) {
          table.insertIntoData(elem._2.toString, xID, legID, elem._1.split(";")(0), "其他建筑")
        } else {
          table.insertIntoData(elem._2.toString, xID, legID, elem._1.split(";")(0), "独栋建筑")
        }
      }

      searchId = table.insertIntoSearch("独栋建筑比例","独栋",reportID)
      if (reportID > 0 && digID > 0 && legID > 0 && xID > 0 && yID > 0) {
        msconn.commit();
        return true;
      }

    } catch {
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
  //============================================插入表格数据==============================================

  /**
    * 查询表格中需要的数据
    * -----自住、租赁-----
    * ID  城市等级  简称年份  建筑结构  自住、租赁
    */
  def selfRentTable(tableName:String,select:String,page:Int,table: Table):List[(String,String)] = {

    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    var values = hbaseRDD.map { x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "CONTROL".getBytes())),
        (Bytes.toString(x._2.getValue("info".getBytes(), "METRO3".getBytes()))),
        (Bytes.toString(x._2.getValue("info".getBytes(), "BUILT".getBytes()))),
        (Bytes.toString(x._2.getValue("info".getBytes(), "STRUCTURETYPE".getBytes()))),
        (Bytes.toString(x._2.getValue("info".getBytes(), "OWNRENT".getBytes()))))
    }
    }.filter(x => x._2.toInt>0&&x._3.toInt>0&&x._4.toInt>0&&x._5.toInt>0)

    if(select==null){
      values
    }else{
      //过滤自住、租赁
      if(select.equals("自住")){
        //("自住",x._1+"_"+x._2+"_"+x._3+"_"+x._4)
        values =  values.filter(_._5.equals("1"))
      }else if (select.equals("租赁")){
        values = values.filter(_._5.equals("2"))
      }else{
        values
      }
    }



    val v = values.map{x=>{
      if(x._5.toInt==1){
        //Map中   相同key 会覆盖  取房屋编号为key:(x._1)
        (x._1,x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+"自住")
      }else{
        (x._1,x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+"租赁")
      }
    }}
    /**
      * 转成javalist
      * 分页   传入页数，总数，
      */
    //import sqlContext.implicits._
    var list:List[(String,String)] = List()


    val java: util.List[(String, String)] = v.take(page*10).toList.asJava
    val l: Long = v.count()
    val tuples= subString(page,l.toInt,java)
    val value = tuples.iterator()


    //获取所有页码
    val p = new Page;


    if (page == null) {
      val page1 = 1
      p.setThisPage(page1)
    }
    //table.setPage(p)

    // var lll:List[Integer] = List()

    //总页数
    val ppage = l.toInt/10+1
    //判断输入页码的条数是否大于总页数
    //先输入页数 > 总页数判断
    if(page>ppage){
      //总页数大于10
      if(ppage>10){
        for(i <- ppage-9 to ppage-1){
          p.addData(i)
        }
      //  p.addData(ppage)
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
      //输入页数 < 总页数
    }else{
      if(ppage>10){
        if(page<5){
          var count = 0
          while(count + page < 10){
            p.addData(count + page)
            count += 1
          }
        }else{
          for(i <- page-4 to page +4){
            p.addData(i)
          }
         // p.addData(ppage)
        }
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
    }
    table.setPage(p)

//    if(l.toInt==0){
//      p.addData(0)
//    }else{
//      if(page>0&&page<10){
//        var count = 0
//        while(count + page < 10){
//          p.addData(page + count)
//          count += 1
//        }
////        for (i <- 1 to page){
////          p.addData(i)
////        }
//      }else if(page>= 10 && page <= ppage -10){
//        for(i <- page-4 to page+4){
//          p.addData(i)
//        }
//        p.addData(ppage)
//      }else{
//        for(i <- l.toInt/10-9 to l.toInt/10){
//          p.addData(i)
//        }
//      }
//    }

//    if(page*10+10>l.toInt){
//      //总条数/10+1=页数
//      for(i <- l.toInt/10-10 to l.toInt/10 ){
//        p.addData(i)
//      }
//    }else{
//      for(i <- page to page+10){
//        //sb.append(i)
//        p.addData(i)
//      }
//    }



//    for(i <- 1 to l.toInt){
//      if(i<=10){
//        p.addData(i)
//      }else{
//      }
//    }
   //table.setPage(p).addSearchs(search)

    while(value.hasNext){
      val str = value.next()
      list:+=(l.toString,str._2)
    }
    list
  }

  /**
    * 截取方法
    */
  def subString(page:Int,count:Int,list:util.List[(String, String)]): util.List[(String, String)] ={
    if(count <10){
      list.subList(0,count)
    }else{
      if (page*10>count){
        list.subList(count-10,count)
      }else{
        list.subList((page-1)*10,page*10)
      }
    }

  }




  /**
    * 查询表格中需要的数据      一个方法里面做判断
    * ------房间数：卧室数 ------
    *       条件：城市等级  房间数最大15
    *       ID  城市等级  房间数   卧室数
    */
  def roomsTable(tableName:String,city:String,rooms:String,page:Int,table :Table):List[(String,String)]  = {
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    var roomsMap = Map[String, String]();
    //取数据
    var value = hbaseRDD.map(x => {
      (Bytes.toString(x._2.getValue("info".getBytes(), "CONTROL".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(), "METRO3".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(), "ROOMS".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(), "BEDRMS".getBytes())))
    }).filter(x => x._4.toInt > 0)
    //(499434810242,499434810242_2_5_3)
    //过滤输入的城市等级

    if (city != null) {
      if (city.equals("1")) {
        value = value.filter(_._2.equals("1"))
      } else if (city.equals("2")) {
        value = value.filter(_._2.equals("2"))
      } else if (city.equals("3")) {
        value = value.filter(_._2.equals("3"))
      } else if (city.equals("4")) {
        value = value.filter(_._2.equals("4"))
      } else if (city.equals("5")) {
        value = value.filter(_._2.equals("5"))
      }
    }

    if (rooms != null) {
      if (rooms.equals("1")) {
        value = value.filter(_._3.equals("1"))
      } else if (rooms.equals("2")) {
        value = value.filter(_._3.equals("2"))
      } else if (rooms.equals("3")) {
        value = value.filter(_._3.equals("3"))
      } else if (rooms.equals("4")) {
        value = value.filter(_._3.equals("4"))
      } else if (rooms.equals("5")) {
        value = value.filter(_._3.equals("5"))
      } else if (rooms.equals("6")) {
        value = value.filter(_._3.equals("6"))
      } else if (rooms.equals("7")) {
        value = value.filter(_._3.equals("7"))
      } else if (rooms.equals("8")) {
        value = value.filter(_._3.equals("8"))
      } else if (rooms.equals("9")) {
        value = value.filter(_._3.equals("9"))
      } else if (rooms.equals("10+")) {
        value = value.filter(_._3.toInt >= 10)
      }
    }


      val v = value.map { x => {
        (x._1, x._1 + "_" + x._2 + "_" + x._3 + "_" + x._4)
      }
    }


    var jlist: List[(String, String)] = List()
    val l1: Long = v.count()


    //val l111: util.List[Integer]  =  v.take(l1.toInt).toList.asJava
    val l: util.List[(String, String)] = v.take(page * 10).toList.asJava;

    val tuples = subString(page, l1.toInt, l)
    val value1 = tuples.iterator()


    //获取所有页码
    val p = new Page;


    if (page == null) {
      val page1 = 1
      p.setThisPage(page1)
    }
    //table.setPage(p)

    // var lll:List[Integer] = List()

    //总页数
    val ppage = l1.toInt/10+1
    //判断输入页码的条数是否大于总页数
    //先输入页数 > 总页数判断
    if(page>ppage){
      //总页数大于10
      if(ppage>10){
        for(i <- ppage-9 to ppage-1){
          p.addData(i)
        }
        //p.addData(ppage)
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
      //输入页数 < 总页数
    }else{
      if(ppage>10){
        if(page<5){
          var count = 0
          while(count + page < 10){
            p.addData(count + page)
            count += 1
          }
        }else{
          for(i <- page-4 to page +4){
            p.addData(i)
          }
         // p.addData(ppage)
        }
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
    }

      table.setPage(p)

      while (value1.hasNext) {
        val str = value1.next()
        jlist :+= (l.toString, str._2)
      }
      jlist


  }
  /**
    *按建成年份：统计独栋比例
    *   条件：年份区间    是否独栋
    * CONTROL METRO3 BUILT STRUCTURETYPE
    */
  def singleTable(tableName:String,year:String,ifSingleBuild:String,page:Int,table:Table):List[(String,String)]  ={
    //获取hbase配置信息
    val hconf = HBaseConfiguration.create()
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")

    val hbaseRDD = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    //RDD操作
    var singleMap = Map[String,String]()
    var values = hbaseRDD.map{x=>{
      //获取列的数据    一行一行
      (Bytes.toString(x._2.getValue("info".getBytes(),"CONTROL".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(),"METRO3".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(),"BUILT".getBytes())),
        Bytes.toString(x._2.getValue("info".getBytes(),"STRUCTURETYPE".getBytes())))
    }}

    //条件筛选  年份判断
    if(year ==null){
      values
    }else{
      if(year.equals("1900-2000")){
        values = values.filter(x=>{x._3.toInt>=1900&&x._3.toInt<=2000})
      }else if(year.equals("2000-2010")){
        values = values.filter(x=>{x._3.toInt>2000&&x._3.toInt<=2010})
      }else if(year.equals("2010-2018")){
        values = values.filter(x=>{x._3.toInt>2010&&x._3.toInt<=2018})
      }
    }



    //独栋判断
    if(ifSingleBuild!=null){
      if(ifSingleBuild.equals("是")){
        values = values.filter(_._4.toInt==1)
      }else if(ifSingleBuild.equals("否")){
        values = values.filter(_._4.toInt==2)
      }
    }

    //拼接成Map,自定义组装
    val v=values.map(x=>{
      if(x._4.toInt==1){
        (x._1,x._1+"_"+x._2+"_"+x._3+"_"+"是")
      }else{
        (x._1,x._1+"_"+x._2+"_"+x._3+"_"+"否")
      }
    })


    var jlist:List[(String,String)] = List()
    //获得符合条件的数据的数量，既总条数
    val len= v.count()
    //val ppage = len.toInt/10+1

    //取到page页*10，装换成java的list    截取     asJava需要导包
    val l: util.List[(String, String)]  = v.take(page*10).toList.asJava;

    val tuples = subString(page,len.toInt,l)
    val value1 = tuples.iterator()



    val p = new Page;


    if(page == null){
      p.setThisPage(1)
    }

    //判断输入页码的条数是否大于总页数

//总页数
    val ppage = len.toInt/10+1
    //判断输入页码的条数是否大于总页数
    //先输入页数 > 总页数判断
    if(page>ppage){
      //总页数大于10
      if(ppage>10){
        for(i <- ppage-9 to ppage-1){
          p.addData(i)
        }
       // p.addData(ppage)
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
      //输入页数 < 总页数
    }else{
      if(ppage>10){
        if(page<5){
          var count = 0
          while(count + page < 10){
            p.addData(count + page)
            count += 1
          }
        }else{
          for(i <- page-4 to page +4){
            p.addData(i)
          }
        // p.addData(ppage)
        }
      }else{
        for(i <- 1 to page){
          p.addData(i)
        }
      }
    }

    table.setPage(p)

    while(value1.hasNext){
      val str = value1.next()
      jlist:+=(l.toString,str._2)
    }

    jlist

  }

  def insertIntoMysql(tableName:String): Unit ={
    selfOrRent(tableName, tableName.split(":")(1).toInt);
    RoomAndBedRom(tableName, tableName.split(":")(1).toInt)
    SingleBuildByYear(tableName, tableName.split(":")(1).toInt)
  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    insertIntoMysql(tableName:String)
    return true

  }
}
