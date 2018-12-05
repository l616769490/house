package com.tecode.house.lijin.hbase

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.service.impl.InsertFromXml
import com.tecode.house.lijin.utils.ConfigUtil
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.util.Bytes

/**
  * 基础-房间数
  */
class BasicsHouseNum extends Analysis {
  private val conf = new SparkConf().setAppName("BasicsHouseNum").setMaster(ConfigUtil.get("spark_master"))
  private val sc = new SparkContext(conf)
  private val hBaseConf = HBaseConfiguration.create

  /**
    * 读取HBase数据
    *
    * @param tableName HBase的表名(带命名空间)
    * @return HBaseRDD
    */
  def readHBase(tableName: String): RDD[(ImmutableBytesWritable, Result)] = {
    hBaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
    val scan = new Scan()
    scan.setCacheBlocks(false)
    // 设置读取的列族
    scan.addFamily(Bytes.toBytes("info"))
    // 卧室数
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BEDRMS"))
    // 房间数
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ROOMS"))
    // 将scan类转化成string类型  
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    // 读取数据
    val hbaseRDD = readHBase(tableName).map(
      row => {
        var BEDRMS, ROOMS = 0

        for (cell <- row._2.rawCells()) {
          // 列族
          // val family = Bytes.toString(CellUtil.cloneFamily(cell))
          // 列名
          val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
          // 值
          val value = Bytes.toString(CellUtil.cloneValue(cell))
          if ("BEDRMS".equals(qualifier)) {
            BEDRMS = Integer.parseInt(value)
          } else if ("ROOMS".equals(qualifier)) {
            ROOMS = Integer.parseInt(value)
          }
        }
        (BEDRMS, ROOMS)
      }

    )

    // 卧室数
    val bedrmsRDD = hbaseRDD.map(_._1).filter(_ > 0)

    // 房间数
    val roomsRDD = hbaseRDD.map(_._2).filter(_ > 0)

    import scala.collection.JavaConverters._
    val map = Map("卧室数" -> getMap(bedrmsRDD).asJava, "房间数" -> getMap(roomsRDD).asJava)

//    new InsertBasicsRoomsNumServer().insert(map.asJava, Integer.parseInt(tableName.split(":")(1)))
    val path = this.getClass.getResource("/report/basics-rooms.xml").getPath
    val conf = ConfigUtil.get("mybatis-config2")
    new InsertFromXml(conf, path).insert(map.asJava, Integer.parseInt(tableName.split(":")(1)))
    true
  }

  def getMap(rdd: RDD[Int]): Map[String, String] = {
    var map = Map[String, String]()
    rdd.map(n => {
      if (n > 10) {
        ("10+", 1)
      } else {
        (n + "", 1)
      }
    }).reduceByKey(_ + _).map(t => (t._1, t._2 + "")).collect().foreach(t => map += ((t._1, t._2)))
    map
  }


}

object BasicsHouseNum {
  def main(args: Array[String]): Unit = {
    val basicsHouseNum = new BasicsHouseNum()
    basicsHouseNum.analysis("thads:2013")
  }
}
