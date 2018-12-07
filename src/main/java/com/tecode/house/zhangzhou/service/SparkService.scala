package com.tecode.house.zhangzhou.service

import java.sql.{Connection, SQLException}

import com.tecode.house.d01.service.Analysis
import com.tecode.house.zhangzhou.mysqlDao.impl.MysqlDaoImpl
import com.tecode.house.zhangzhou.zzUtil.MySQLUitl
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 样例类
  * @param city 城市规模
  * @param houseDuty 房屋费用
  */
case class TmpClass(city:String ,houseDuty:Double)

class SparkService{
  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = new SparkContext(sparkConf)
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()

  //val tableName = "thads:2013"
  //val hbaseConf = HBaseConfiguration.create()
  //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
  //hbaseConf.set("hbase.zookeeper.quorum","hadoop111,hadoop112,hadoop113")
  //设置zookeeper连接端口，默认2181
  //hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  //hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName)

  /**获得空置状态
    * 1：一线城市空置房屋数量
    * 2：二线城市空置房屋数量
    * 3：三线城市空置房屋数量
    * 4：四线城市空置房屋数量
    * 5：五线城市空置房屋数量
    * -6：不是空置房屋的数量
    */
  def selectVacancyState(tableName:String,year:Int): Unit ={
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //获得info列族下"VACANCY"列的RDD对象并聚合
    val value: RDD[(String, Int)] = hbaseRDD.map(x => Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VACANCY"))))
      .map((_, 1)).reduceByKey(_ + _)
    var vacancyMap = Map[String,Int]()
    //将RDD的值写入Map集合中
    value.collect().foreach(vacancyMap+=(_))
    //调用将数据写入mysql的方法，将统计结果写入mysql
    setIntoMysqlTableVacancy(vacancyMap,year)
  }

  /**
    * 将空置状态数据导入mysql
    * @param mmap
    * @param tableName
    */
  def setIntoMysqlTableVacancy(mmap:Map[String,Int],year:Int): Unit ={
    var msconn:Connection =MySQLUitl.getConn
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      var reportId = 0
      var diagramId = 0
      var legendId = 0
      var xId = 0
      var yId = 0
      var dId1 = 0
      var dId2 = 0
      var dataId= 0
      var searchId= 0
      //向report表中插入数据，并返回report表中的id
      val rName: String = "空置状态"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = year
      val rGroup: String = "基础查询"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      reportId = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)
      //饼状图（图表表）
      val dName = "空置状态"
      val dText = "统计房屋的空置状态情况"
      diagramId = table.insertIntoDiagram(dName, 2, reportId, dText)
      //图例
      legendId = table.insertIntoLegend("空置状态图例","空置",diagramId)
      //插入X轴
      val xName: String = "-1"
      xId = table.insertIntoXAxis(xName, diagramId, "-1")
      //插入Y轴
      yId = table.insertIntoYAxis("空置状态", diagramId)
      //插入数据集表（维度表）
      dId1 = table.insertIntoDimension("空置", "是", "VACANCY")
      dId2 = table.insertIntoDimension("空置", "否", "VACANCY")
      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1,"空置状态图例")
        dataId+=1
      }
      //插入搜索表
      searchId = table.insertIntoSearch("空置状态","空置",reportId)
      if(reportId>0 && diagramId>0 && legendId>0 && xId>0 && yId>0 && dId1>0 && dId2>0 && dataId==mmap.size && searchId>0){
        //提交事务
        msconn.commit()
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
      MySQLUitl.close(msconn);
    }
  }


  /**
    * 按城市规模统计：独栋建筑比例
    */
  def selectSingleBuildingGroupByCity(tableName:String,year:Int): Unit ={
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE,tableName)
    var mmap = Map[String,Int]()
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市和建筑结构类型,得到每个城市规模中的每种房屋建筑结构的房屋的数量
    val cityAndBuldingRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("METRO3"))),(Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("STRUCTURETYPE")))))).filter(_._2.toInt>0).map((x => (x._1+"-"+x._2,1))).reduceByKey(_+_)
    //得到独栋建筑的RDD对象
    val singleBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => x._2._1.equals("1")).map(x => (x._1,x._2._2)).map(x => (x._1+"-"+"独栋建筑",x._2))
    singleBuildingRDD.collect().foreach(mmap+=(_))
    val otherBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => !(x._2._1.equals("1"))).map(x => (x._1,x._2._2)).reduceByKey(_+_).map(x => (x._1+"-"+"其他建筑",x._2))
    otherBuildingRDD.collect().foreach(mmap+=(_))
    //调用将数据写入mysql的方法
    setIntoMysqlTableSingleBuilding(mmap,year)
  }

  /**
    * 将独栋建筑的统计结果写入mysql
    * @param mmap 统计得到的结果
    */
  def setIntoMysqlTableSingleBuilding(mmap:Map[String,Int],year:Int): Unit ={
    var msconn:Connection =MySQLUitl.getConn
    val table = new MysqlDaoImpl
    try {
      var reportId = 0
      var diagramId = 0
      var legendId = 0
      var xId = 0
      var yId = 0
      var dId1 = 0
      var dId2 = 0
      var dId3 = 0
      var dId4 = 0
      var dId5 = 0
      var dId6 = 0
      var dId7 = 0
      var dataId= 0
      var searchId= 0
      msconn.setAutoCommit(false)
      //向report表中插入数据，并返回report表中的id
      val rName: String = "独栋建筑比例"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = year
      val rGroup: String = "城市规模"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/singleBuilding"
      reportId = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //柱状图，图表表
      val dName = "独栋建筑比例"
      val dText = "统计不同城市规模中的独栋建筑比例"
      diagramId = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例

      legendId = table.insertIntoLegend("建筑类型图例","城市规模",diagramId)

      //插入X轴
      val xName: String = "城市规模"
      xId = table.insertIntoXAxis(xName, diagramId, "城市规模")

      //插入Y轴
      yId = table.insertIntoYAxis("建筑数量", diagramId)

      //插入数据集表（维度表）
      dId1 = table.insertIntoDimension("城市规模", "一线城市", "METRO3")
      dId2 = table.insertIntoDimension("城市规模", "二线城市", "METRO3")
      dId3 = table.insertIntoDimension("城市规模", "三线城市", "METRO3")
      dId4 = table.insertIntoDimension("城市规模", "四线城市", "METRO3")
      dId5 = table.insertIntoDimension("城市规模", "五线城市", "METRO3")
      dId6 = table.insertIntoDimension("独栋", "是", "STRUCTURETYPE")
      dId7 = table.insertIntoDimension("独栋", "否", "STRUCTURETYPE")
      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1.split("-")(0),elem._1.split("-")(1))
        dataId+=1
      }
      //插入搜索表
      searchId = table.insertIntoSearch("独栋建筑比例","独栋",reportId)

      if(reportId>0 && diagramId>0 && legendId>0 && xId>0 && yId>0
        && dId1>0 && dId2>0 &&  dId3>0 && dId4>0 && dId5>0 && dId6>0
        && dId7>0 && dataId==mmap.size && searchId>0){
        //提交事务
        msconn.commit()
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
      MySQLUitl.close(msconn);
    }

  }

  /**
    * 按城市规模统计：平均房产税、最高房产税、最低房产税
    */
  import spark.implicits._
  def selectHouseDutyGroupByCity(tableName:String,year:Int): Unit ={
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE,tableName)
    var cmap = Map[String,String]()
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市规模和房产税的RDD
    val cityAndHouseDutyRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("METRO3"))),
            Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("ZSMHC")))))
            .filter(_._2.toInt>0).map(x => TmpClass(x._1,x._2.toDouble))
    //得到dataFream对象
    val df = cityAndHouseDutyRDD.toDF()
    df.createOrReplaceTempView("tmp")
    val dataFrame = spark.sql("select min(houseDuty),max(houseDuty),avg(houseDuty),count(houseDuty),city from tmp group by city order by city")
    val rdd1 = dataFrame.rdd.map(x => (x.get(4).toString,x.get(0)+"-"+x.get(1)+"-"+x.get(2)))
    rdd1.collect().foreach(cmap+=(_))
    //spark.sql("select city,houseDuty from tmp  order by houseDuty desc ").show()
    spark.stop()
    //调用将数据插入mysql的方法
    setIntoMysqlTableHouseDuty(cmap,year)
  }

  /**
    * 将房产税的统计结果插入mysql数据库
    * @param mmap 统计得到的结果
    */
  def setIntoMysqlTableHouseDuty(mmap:Map[String,String],year:Int): Unit ={
    var msconn:Connection =MySQLUitl.getConn
    val table = new MysqlDaoImpl
    try {
      var reportId = 0
      var diagramId = 0
      var legendId = 0
      var xId = 0
      var yId = 0
      var dId1 = 0
      var dId2 = 0
      var dId3 = 0
      var dataId= 0
      var searchId= 0
      msconn.setAutoCommit(false)
      //向report表中插入数据，并返回report表中的id
      val rName: String = "房产税"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = year
      val rGroup: String = "城市规模"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/houseDuty"
      reportId = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //柱状图，图表表
      val dName = "房产税"
      val dText = "统计不同城市规模中的平均房产税、最高房产税、最低房产税"
      diagramId = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例

      legendId = table.insertIntoLegend("房产税图例","城市规模",diagramId)

      //插入X轴
      val xName: String = "城市规模"
      xId = table.insertIntoXAxis(xName, diagramId, "城市规模")

      //插入Y轴
      yId = table.insertIntoYAxis("房产税", diagramId)

      //插入数据集表（维度表）
      dId1 = table.insertIntoDimension("数学", "avg", "ZSMHC")
      dId2 = table.insertIntoDimension("数学", "max", "ZSMHC")
      dId3 = table.insertIntoDimension("数学", "min", "ZSMHC")
      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.split("-")(0),xId,legendId,elem._1,"min")
        table.insertIntoData(elem._2.split("-")(1),xId,legendId,elem._1,"max")
        table.insertIntoData(elem._2.split("-")(2),xId,legendId,elem._1,"avg")
        dataId+=1
      }
      //插入搜索表
      table.insertIntoSearch("房产税","数学",reportId)
      if(reportId>0 && diagramId>0 && legendId>0 && xId>0 && yId>0 && dId1>0 && dId2>0 && dId3>0 && dataId==mmap.size && searchId>0){
        //提交事务
        msconn.commit()
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
      MySQLUitl.close(msconn);
    }
  }


  def selectVacancyTable(tableName:String,search:String): Map[String,Iterable[String]] ={
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE,tableName)
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    var clumnRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("METRO3"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("BUILT"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("AGE1"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("VACANCY"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("ASSISTED")))))
    if(search.equals("空置")){
      clumnRDD = clumnRDD.filter(!_._5.equals("-6"))
    }
    if(search.equals("居住")){
      clumnRDD = clumnRDD.filter(_._5.equals("-6"))
    }

    val mapRDD = clumnRDD.map{x => val tmp=x._5
      if(tmp.equals("-6")){
        ("居住",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+"居住"+"_"+x._6)
      }else{
        ("空置",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+"空置"+"_"+x._6)
      }
    }.groupByKey()
    var mmap = Map[String,Iterable[String]]()
    mapRDD.collect().foreach(mmap+=(_))
    spark.close()
    return mmap

  }

  def selectSingleBuildTable(tableName:String,search:String,search2:String): Map[String,Iterable[String]] ={
    val hbaseConf = HBaseConfiguration.create()
    hbaseConf.set(TableInputFormat.INPUT_TABLE,tableName)
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    var clumnRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("CONTROL"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("METRO3"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("BUILT"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("STRUCTURETYPE"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("BEDRMS"))),
      Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("ROOMS"))))).filter(_._4.toInt>0)
    if(search.equals("一线城市")){
      clumnRDD = clumnRDD.filter(_._2.equals("1"))
    }
    if(search.equals("二线城市")){
      clumnRDD = clumnRDD.filter(_._2.equals("2"))
    }
    if(search.equals("三线城市")){
      clumnRDD = clumnRDD.filter(_._2.equals("3"))
    }
    if(search.equals("四线城市")){
      clumnRDD = clumnRDD.filter(_._2.equals("4"))
    }
    if(search.equals("五线城市")){
      clumnRDD = clumnRDD.filter(_._2.equals("5"))
    }
    if(search2.equals("独栋")){
      clumnRDD = clumnRDD.filter(_._4.equals("1"))
    }
    if(search2.equals("其他")){
      clumnRDD = clumnRDD.filter(!_._4.equals("1"))
    }

    val mapRDD = clumnRDD.map{x => val tmp=x._2
      if(tmp.equals("1")){
        ("1",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+x._5+"_"+x._6)
      }else if(tmp.equals("2")){
        ("2",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+x._5+"_"+x._6)
      }else if(tmp.equals("3")){
        ("3",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+x._5+"_"+x._6)
      }else if(tmp.equals("4")){
        ("4",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+x._5+"_"+x._6)
      }else{
        ("5",x._1+"_"+x._2+"_"+x._3+"_"+x._4+"_"+x._5+"_"+x._6)
      }
    }.groupByKey()
    var mmap = Map[String,Iterable[String]]()
    mapRDD.collect().foreach(mmap+=(_))
    spark.close()
    return mmap

  }
}

object SparkService{
  def main(args: Array[String]): Unit = {
    val ss = new SparkService
   // ss.analysis("空置状态饼图")

    println("123ada")
  }
}


