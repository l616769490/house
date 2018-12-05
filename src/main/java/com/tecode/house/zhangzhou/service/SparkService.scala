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

class SparkService extends Analysis{
  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = new SparkContext(sparkConf)
  val spark = SparkSession.builder().config(sparkConf).getOrCreate()
  var msconn:Connection =MySQLUitl.getConn

  val tableName = "thads:2013"
  val hbaseConf = HBaseConfiguration.create()
  //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
  hbaseConf.set("hbase.zookeeper.quorum","hadoop111,hadoop112,hadoop113")
  //设置zookeeper连接端口，默认2181
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName)

  /**获得空置状态
    * 1：一线城市空置房屋数量
    * 2：二线城市空置房屋数量
    * 3：三线城市空置房屋数量
    * 4：四线城市空置房屋数量
    * 5：五线城市空置房屋数量
    * -6：不是空置房屋的数量
    */
  def selectVacancyState(tableName:String): Unit ={
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    val houseCount= hbaseRDD.count()
    println("houseCount:"+houseCount)
    var vacancyMap = Map[String,Int]()
    val value: RDD[(String, Int)] = hbaseRDD.map(x => Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VACANCY"))))
      .map((_, 1)).reduceByKey(_ + _)
    value.collect().foreach(vacancyMap+=(_))
    setIntoMysqlTableVacancy(vacancyMap,tableName)
  }

  /**
    * 将空置状态数据导入mysql
    * @param mmap
    * @param tableName
    */
  def setIntoMysqlTableVacancy(mmap:Map[String,Int],tableName:String): Unit ={
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      //向report表中插入数据，并返回report表中的id
      val rName: String = "空置状态"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "basic"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/vacancy"
      val reportId: Int = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //饼状图
      val dName = tableName
      val dText = "统计房屋的空置状态情况"
      val diagramId: Int = table.insertIntoDiagram(dName, 2, reportId, dText)

      //图例

      val legendId:Int = table.insertIntoLegend("空置状态图例","空置",diagramId)

      //插入X轴
      val xName: String = "空置状态"
      val xId: Int = table.insertIntoXAxis(xName, diagramId, "111")

      //插入Y轴
      val yId: Int = table.insertIntoYAxis("222", diagramId)

      //插入数据集表
      val ddId: Int = table.insertIntoDimension("basic", "空置状态", "VACANCY")

      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1,"空置状态图例")
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
  def selectSingleBuildingGroupByCity(): Unit ={
    var mmap = Map[String,Int]()
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市和建筑结构类型,得到每个城市规模中的每种房屋建筑结构的房屋的数量
    val cityAndBuldingRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("METRO3"))),(Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("STRUCTURETYPE")))))).map((x => (x._1+"-"+x._2,1))).reduceByKey(_+_)
    val singleBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => x._2._1.equals("1")).map(x => (x._1,x._2._2)).map(x => (x._1+"-"+"独栋建筑",x._2))
    singleBuildingRDD.collect().foreach(mmap+=(_))
    val otherBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => !(x._2._1.equals("1"))).map(x => (x._1,x._2._2)).reduceByKey(_+_).map(x => (x._1+"-"+"建筑总数",x._2))
    otherBuildingRDD.collect().foreach(mmap+=(_))

    setIntoMysqlTableSingleBuilding(mmap,"独栋建筑比例柱状图")
  }

  def setIntoMysqlTableSingleBuilding(mmap:Map[String,Int],tableName:String): Unit ={
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      //向report表中插入数据，并返回report表中的id
      val rName: String = "独栋建筑比例"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "城市规模"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/singleBuilding"
      val reportId: Int = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //柱状图，图表表
      val dName = tableName
      val dText = "统计不同城市规模中的独栋建筑比例"
      val diagramId: Int = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例

      val legendId:Int = table.insertIntoLegend("建筑类型图例","城市规模",diagramId)

      //插入X轴
      val xName: String = "城市规模"
      val xId: Int = table.insertIntoXAxis(xName, diagramId, "城市规模")

      //插入Y轴
      val yId: Int = table.insertIntoYAxis("建筑数量", diagramId)

      //插入数据集表（维度表）
      val ddId1: Int = table.insertIntoDimension("城市规模", "一线城市", "METRO3")
      val ddId2: Int = table.insertIntoDimension("城市规模", "二线城市", "METRO3")
      val ddId3: Int = table.insertIntoDimension("城市规模", "三线城市", "METRO3")
      val ddId4: Int = table.insertIntoDimension("城市规模", "四线城市", "METRO3")
      val ddId5: Int = table.insertIntoDimension("城市规模", "五线城市", "METRO3")
      val ddId6: Int = table.insertIntoDimension("是否独栋", "独栋建筑数", "STRUCTURETYPE")
      val ddId7: Int = table.insertIntoDimension("是否独栋", "总建筑数", "STRUCTURETYPE")
      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.toString,xId,legendId,elem._1.split("-")(0),elem._1.split("-")(1))
      }
      //提交事务
      msconn.commit()
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
  def selectHouseDutyGroupByCity(): Unit ={
    var cmap = Map[String,String]()
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市规模和房产税的RDD
    val cityAndHouseDutyRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("METRO3"))),
            Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("ZSMHC")))))
            .filter(_._2.toInt>0).map(x => TmpClass(x._1,x._2.toDouble))

    val df = cityAndHouseDutyRDD.toDF()
    df.createOrReplaceTempView("tmp")
    val dataFrame = spark.sql("select min(houseDuty),max(houseDuty),avg(houseDuty),count(houseDuty),city from tmp group by city order by city")
    val rdd1 = dataFrame.rdd.map(x => (x.get(4).toString,x.get(0)+"-"+x.get(1)+"-"+x.get(2)))
    rdd1.collect().foreach(cmap+=(_))
    //spark.sql("select city,houseDuty from tmp  order by houseDuty desc ").show()
    spark.stop()
    setIntoMysqlTableHouseDuty(cmap,"房产税柱状图")
  }


  def setIntoMysqlTableHouseDuty(mmap:Map[String,String],tableName:String): Unit ={
    val table = new MysqlDaoImpl
    try {
      msconn.setAutoCommit(false)
      //向report表中插入数据，并返回report表中的id
      val rName: String = "房产税"
      val rtime: Long = System.currentTimeMillis()
      val rYear: Int = 2013
      val rGroup: String = "城市规模"
      val rStatus: Int = 0
      val url: String = "http://166.166.2.111/houseDuty"
      val reportId: Int = table.insertIntoReport(rName, rtime, rYear, rGroup, rStatus, url)

      //柱状图，图表表
      val dName = tableName
      val dText = "统计不同城市规模中的平均房产税、最高房产税、最低房产税"
      val diagramId: Int = table.insertIntoDiagram(dName, 1, reportId, dText)

      //图例

      val legendId:Int = table.insertIntoLegend("房产税图例","城市规模",diagramId)

      //插入X轴
      val xName: String = "城市规模"
      val xId: Int = table.insertIntoXAxis(xName, diagramId, "城市规模")

      //插入Y轴
      val yId: Int = table.insertIntoYAxis("房产税", diagramId)

      //插入数据集表（维度表）
      val ddId1: Int = table.insertIntoDimension("房产税", "平均房产税", "ZSMHC")
      val ddId2: Int = table.insertIntoDimension("房产税", "最高房产税", "ZSMHC")
      val ddId3: Int = table.insertIntoDimension("房产税", "最低房产税", "ZSMHC")
      //插入数据表
      for (elem <- mmap) {
        table.insertIntoData(elem._2.split("-")(0),xId,legendId,elem._1,"最低房产税")
        table.insertIntoData(elem._2.split("-")(1),xId,legendId,elem._1,"最高房产税")
        table.insertIntoData(elem._2.split("-")(2),xId,legendId,elem._1,"平均房产税")
      }
      //提交事务
      msconn.commit()
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
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    //将查询分析得到的数据写入mysql数据库
    selectVacancyState(tableName)
    //将数据从数据库取出，在页面显示图形

    true
  }
}

object SparkService{
  def main(args: Array[String]): Unit = {
    val ss = new SparkService
   // ss.analysis("空置状态饼图")
    ss.selectHouseDutyGroupByCity
    println("123ada")
  }
}


