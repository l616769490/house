package com.tecode.house.lijin.hbase

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.service.impl.{InsertFromXml}
import com.tecode.house.lijin.utils.ConfigUtil
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConverters.getClass

/**
  * 按区域-房产税统计
  * 版本：2018/12/4 V1.0
  * 成员：李晋
  */
class RegionZsmhcNum extends Analysis {

  import scala.collection.JavaConverters._

  private val conf: SparkConf = new SparkConf().setAppName("RegionZinc2Num").setMaster(ConfigUtil.get("spark_master"))
  private val sc = new SparkContext(conf)
  private val hBaseConf: Configuration = HBaseConfiguration.create()


  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val hBaseRDD = readHBase(tableName)

    val array = getRegion(hBaseRDD)
    val map = getMap(array)

    val conf = ConfigUtil.get("mybatis-config2")
    val path = getClass.getResource("/report/region-zsmhc.xml").getPath
    new InsertFromXml(conf, path).insert(map, Integer.parseInt(tableName.split(":")(1)))
    true
  }

  /**
    * 将统计结果组装成存储接口需要的格式
    *
    * @param arr 统计结果
    * @return 传递给存储接口的格式
    */
  def getMap(arr: Array[(Int, (Int, Int, Int, Int, Int, Int, Int, Int))]): java.util.Map[String, java.util.Map[String, String]] = {
    val list1 = List("0-500", "500-1000","1000-1500", "1500-2000", "2000-2500", "2500-3000", "3000-35000", "3500+")
    var map = Map[String, java.util.Map[String, String]]()
    for(a <- arr) {
      val list2 = List(a._2._1 + "",a._2._2 + "",a._2._3 + "",a._2._4 + "",a._2._5 + "",a._2._6 + "",a._2._7 + "", a._2._8 + "")
      val m = list1.zip(list2).toMap.asJava
      map += ((a._1 + "", m))
    }
    map.asJava
  }

  def getRegion(rdd: RDD[(ImmutableBytesWritable, Result)]): Array[(Int, (Int, Int, Int, Int, Int, Int, Int, Int))] = {
    rdd.map(row => {
      var REGION, ZSMHC = 0

      for (cell <- row._2.rawCells()) {
        // 列名
        val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
        // 值
        val value = Bytes.toString(CellUtil.cloneValue(cell))
        if ("REGION".equals(qualifier)) {
          REGION = Integer.parseInt(value)
        } else if ("ZSMHC".equals(qualifier)) {
          ZSMHC = Integer.parseInt(value)
        }
      }
      (REGION, ZSMHC)
    }).filter(_._2 > 0)
      // (地区, (房产税范围, 1))
      .map(x => {
      val ZSMHC = x._2
      if( ZSMHC < 500) {
        (x._1,(1,0,0,0,0,0,0,0))
      } else if(ZSMHC < 1000) {
        (x._1,(0,1,0,0,0,0,0,0))
      } else if(ZSMHC < 1500) {
        (x._1,(0,0,1,0,0,0,0,0))
      } else if(ZSMHC < 2000){
        (x._1,(0,0,0,1,0,0,0,0))
      } else if(ZSMHC < 2500){
        (x._1,(0,0,0,0,1,0,0,0))
      } else if(ZSMHC < 3000){
        (x._1,(0,0,0,0,0,1,0,0))
      } else if(ZSMHC < 3500){
        (x._1,(0,0,0,0,0,0,1,0))
      } else{
        (x._1,(0,0,0,0,0,0,0,1))
      }
    }).reduceByKey(
      (x1, x2) => (x1._1 + x2._1, x1._2 + x2._2, x1._3 + x2._3, x1._4 + x2._4, x1._5 + x2._5, x1._6 + x2._6, x1._7 + x2._7, x1._1 + x2._8)
    ).collect()
  }


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
    // 区域
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("REGION"))
    // 家庭收入
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZSMHC"))
    // 将scan类转化成string类型  
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }



}

object RegionZsmhcNum {
  def main(args: Array[String]): Unit = {
    new RegionZsmhcNum().analysis("thads:2013")
  }
}
