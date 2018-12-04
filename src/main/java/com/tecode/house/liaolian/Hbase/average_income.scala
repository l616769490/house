package com.tecode.house.liaolian.Hbase


import com.tecode.house.d01.service.Analysis
import com.tecode.house.liaolian.service.impl.InsertBasicsRoomsNumServer
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.hadoop.hbase.util.Bytes
/**
  * Created by Administrator on 2018/12/4.
  */
class average_income extends Analysis{
    private val conf = new SparkConf().setAppName("average_income").setMaster("local[*]")
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
      // 家庭人数
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("PER"))
      // 家庭收入
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("ZINC2"))
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
    val PERRDD = hbaseRDD.map(_._1)

    // 房间数
    val ZINC2RDD = hbaseRDD.map(_._2)

    import scala.collection.JavaConverters._

    val map = Map("卧室数" -> getMap(PERRDD).asJava, "房间数" -> getMap(ZINC2RDD).asJava)

    new InsertBasicsRoomsNumServer().insert(map.asJava, Integer.parseInt(tableName.split(":")(1)))

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