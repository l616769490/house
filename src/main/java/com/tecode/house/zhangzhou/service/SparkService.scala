package com.tecode.house.zhangzhou.service

import com.tecode.house.d01.service.Analysis
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
  def selectVacancyState(): Unit ={
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    val houseCount= hbaseRDD.count()
    println("houseCount:"+houseCount)
    val value: RDD[(String, Int)] = hbaseRDD.map(x => Bytes.toString(x._2.getValue(Bytes.toBytes("info"), Bytes.toBytes("VACANCY")))).map((_, 1)).reduceByKey(_ + _)
    val house = value

  }
  def setReport(rdd:RDD[(String, Int)]): Unit ={

  }

  /**
    * 按城市规模统计：独栋建筑比例
    */
  def selectSingleBuildingGroupByCity(): Unit ={
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市和建筑结构类型,得到每个城市规模中的每种房屋建筑结构的房屋的数量
    val cityAndBuldingRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("METRO3"))),(Bytes.toString(x._2.getValue(Bytes.toBytes("info"),
        Bytes.toBytes("STRUCTURETYPE")))))).map((x => (x._1+"-"+x._2,1))).reduceByKey(_+_)
    val singleBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => x._2._1.equals("1")).map(x => (x._1,x._2._2)).collect().foreach(println)
    val otherBuildingRDD = cityAndBuldingRDD.map(x => (x._1.split("-")(0),(x._1.split("-")(1),x._2)))
      .filter(x => !(x._2._1.equals("1"))).map(x => (x._1,x._2._2)).reduceByKey(_+_).collect().foreach(println)
  }

  /**
    * 按城市规模统计：平均房产税、最高房产税、最低房产税
    */
  import spark.implicits._
  def selectHouseDutyGroupByCity(): Unit ={
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    //得到城市规模和房产税的RDD
    val cityAndHouseDutyRDD = hbaseRDD.map(x => (Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("METRO3"))),
            Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("ZSMHC")))))
            .filter(_._2.toInt>=0).map(x => TmpClass(x._1,x._2.toDouble))

    val df = cityAndHouseDutyRDD.toDF()
    df.createOrReplaceTempView("tmp")
    spark.sql("select min(houseDuty),max(houseDuty),avg(houseDuty),count(houseDuty),city from tmp group by city order by city").show()
    //spark.sql("select city,houseDuty from tmp  order by houseDuty desc ").show()
    spark.stop()

  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    true
  }
}

object SparkService{
  def main(args: Array[String]): Unit = {
    val ss = new SparkService
    ss.selectHouseDutyGroupByCity()
    println("123ada")
  }
}


