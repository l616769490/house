package com.tecode.house.lijin.hbase

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.service.impl.InsertFromXml
import com.tecode.house.lijin.utils.{ConfigUtil, SparkUtil}
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
  * 按区域-收入统计
  * 版本：2018/12/4 V1.0
  * 成员：李晋
  */
class RegionZinc2Num extends Analysis {
  import scala.collection.JavaConverters._

  private val sc = SparkUtil.getSparkContext
  private val hBaseConf: Configuration = HBaseConfiguration.create()

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val hBaseRDD = readHBase(tableName)

    // (地区, (地区收入平均值, 地区贫困线人数, 地区L30人数, 地区L50人数, 地区L80人数, 地区L80以上人数, 地区中位数之上人数))
    val region = getRegion(hBaseRDD)

    val map = getMap(region)

    val path = this.getClass.getResource("/report/region-zinx2.xml").getPath
    val conf = ConfigUtil.get("mybatis-config2")
    new InsertFromXml(conf, path).insert(map, Integer.parseInt(tableName.split(":")(1)))
    true
  }

  /**
    * 将统计结果组装成存储接口需要的格式
    *
    * @param arr 统计结果
    * @return 传递给存储接口的格式
    */
  def getMap(arr: Array[(Int, (Int, Int, Int, Int, Int, Int, Int))]): java.util.Map[String, java.util.Map[String, String]] = {
    val list1 = List("家庭平均收入", "贫困线","L30", "L50", "L80", "L80+", "中位数")
    var map = Map[String, java.util.Map[String, String]]()
    for(a <- arr) {
      val list2 = List(a._2._1 + "",a._2._2 + "",a._2._3 + "",a._2._4 + "",a._2._5 + "",a._2._6 + "",a._2._7 + "")
      val m = list1.zip(list2).toMap.asJava
      map += ((a._1 + "", m))
    }
    map.asJava
  }

  /**
    * 统计数据
    *
    * @param rdd 从HBase中读取到的rdd
    * @return 统计结果
    */
  def getRegion(rdd: RDD[(ImmutableBytesWritable, Result)]): Array[(Int, (Int, Int, Int, Int, Int, Int, Int))] = {
    rdd.map(
      row => {
        var REGION, ZINC2, L30, L50, L80, IPOV, LMED = 0

        for (cell <- row._2.rawCells()) {
          // 列名
          val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
          // 值
          val value = Bytes.toString(CellUtil.cloneValue(cell))
          if ("REGION".equals(qualifier)) {
            REGION = Integer.parseInt(value)
          } else if ("ZINC2".equals(qualifier)) {
            ZINC2 = Integer.parseInt(value)
          } else if ("L30".equals(qualifier)) {
            L30 = Integer.parseInt(value)
          } else if ("L50".equals(qualifier)) {
            L50 = Integer.parseInt(value)
          } else if ("L80".equals(qualifier)) {
            L80 = Integer.parseInt(value)
          } else if ("IPOV".equals(qualifier)) {
            IPOV = Integer.parseInt(value)
          } else if ("LMED".equals(qualifier)) {
            LMED = Integer.parseInt(value)
          }
        }
        (REGION, (ZINC2, IPOV, L30, L50, L80, LMED))
      })
      // 过滤非法字段
      .filter(t => t._2._1 > 0 && t._2._2 > 0 && t._2._3 > 0 && t._2._4 > 0 && t._2._5 > 0 && t._2._6 > 0)
      // 是否属于贫困线
      // (区域, (家庭收入, 计数, 是否贫困线, 是否L30, 是否L50, 是否L80, 是否L80以上, 是否中位数之上))
      .map(t => {
      val ZINC2 = t._2._1
      val IPOV = t._2._2
      val L30 = t._2._3
      val L50 = t._2._4
      val L80 = t._2._5
      val LMED = t._2._6
      val b = if (ZINC2 > LMED) 1 else 0
      if (ZINC2 < IPOV) {
        (t._1, (ZINC2, 1, 1, 0, 0, 0, 0, b))
      } else if (ZINC2 < L30) {
        (t._1, (ZINC2, 1, 0, 1, 0, 0, 0, b))
      } else if (ZINC2 < L50) {
        (t._1, (ZINC2, 1, 0, 0, 1, 0, 0, b))
      } else if (ZINC2 < L80) {
        (t._1, (ZINC2, 1, 0, 0, 0, 1, 0, b))
      } else {
        (t._1, (ZINC2, 1, 0, 0, 0, 0, 1, b))
      }
    })
      // 统计(家庭收入, 计数, 是否贫困线, 是否L30, 是否L50, 是否L80, 是否L80以上, 是否中位数之上)
      .reduceByKey((t1, t2) => {
      (t1._1 + t2._1, t1._2 + t2._2, t1._3 + t2._3, t1._4 + t2._4, t1._5 + t2._5, t1._6 + t2._6, t1._7 + t2._7, t1._8 + t2._8)
    })
      // 计算平均值
      // (地区, (地区收入平均值, 地区贫困线人数, 地区L30人数, 地区L50人数, 地区L80人数, 地区L80以上人数, 地区中位数之上人数))
      .map(t => (t._1, (t._2._1 / t._2._2, t._2._3, t._2._4, t._2._5, t._2._6, t._2._7, t._2._8)))
      .collect()
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
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))
    // L30
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L30"))
    // L50
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L50"))
    // L50
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("L80"))
    // 贫困线
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("IPOV"))
    // 中位数
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("LMED"))
    // 将scan类转化成string类型  
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }


}

object RegionZinc2Num {
  def main(args: Array[String]): Unit = {
    new RegionZinc2Num().analysis("thads:2013")
  }
}
