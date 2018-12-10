package com.tecode.house.liaolian.hbase

import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.service.impl.InsertFromXml
import com.tecode.house.lijin.utils.{ConfigUtil, SparkUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


class RegionYear extends Analysis {

  import scala.collection.JavaConverters._

//  private val conf: SparkConf = new SparkConf().setAppName("RegionYear").setMaster(ConfigUtil.get("spark_master"))
  private val sc = SparkUtil.getSparkContext
  private val hBaseConf: Configuration = HBaseConfiguration.create()


  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    //读取Hbase表
    val hBaseRDD = readHBase(tableName)

    val array = getRegion(hBaseRDD)
    val map = getMap(array)

    //    new InsertRegionZsmhcNumServer().insert(map, Integer.parseInt(tableName.split(":")(1)))
    new InsertFromXml(ConfigUtil.get("mybatis-config2"), getClass.getResource("/report/basics-year.xml").getPath()).insert(map, Integer.parseInt(tableName.split(":")(1)))
    true
  }

  /**
    * 将统计结果组装成存储接口需要的格式
    *
    * @param arr 统计结果
    * @return 传递给存储接口的格式
    */
  def getMap(arr: Array[(Int, (Int, Int, Int, Int, Int))]): java.util.Map[String, java.util.Map[String, String]] = {
    val list1 = Array("1900-1940", "1940-1960", "1960-1980", "1980-2000", "2000+")
    var map = Map[String, java.util.Map[String, String]]()
    for(a <- arr) {
      val list2 = List(a._2._1 + "",a._2._2 + "",a._2._3 + "",a._2._4 + "",a._2._5 + "")
      val m = list1.zip(list2).toMap.asJava
      map += (("空维度", m))
    }
    map.asJava
  }

  def getRegion(rdd: RDD[(ImmutableBytesWritable, Result)]): Array[(Int, (Int, Int, Int, Int, Int))] = {
    rdd.map(row => {
      var BUILT = 0

      for (cell <- row._2.rawCells()) {
        // 列名
        val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
        // 值
        val value = Bytes.toString(CellUtil.cloneValue(cell))
        if ("BUILT".equals(qualifier)) {
          BUILT = Integer.parseInt(value)
        }
      }
      BUILT
    }).filter(_ > 0)
      // 年份分组
      .map(x => {
      if (x < 1940) {
        (1, (1, 0, 0, 0, 0))
      } else if (x < 1960) {
        (1, (0, 1, 0, 0, 0))
      } else if (x < 1980) {
        (1, (0, 0, 1, 0, 0))
      } else if (x < 2000) {
        (1, (0, 0, 0, 1, 0))
      } else {
        (1, (0, 0, 0, 0, 1))
      }
    }).reduceByKey(
      (x1, x2) => (x1._1 + x2._1, x1._2 + x2._2, x1._3 + x2._3, x1._4 + x2._4, x1._5 + x2._5)
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
    scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("BUILT"))
    // 将scan类转化成string类型  
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }


}

object RegionYear {
  def main(args: Array[String]): Unit = {
    new RegionYear().analysis("thads:2013")
  }
}
