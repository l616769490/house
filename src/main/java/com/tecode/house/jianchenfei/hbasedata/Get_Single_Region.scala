package com.tecode.house.jianchenfei.hbasedata

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/5.
  */
class Get_Single_Region extends Analysis{
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("getSingle_region").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hbaseconf = HBaseConfiguration.create

    hbaseconf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])
    val SingleRDD = hBaseRDD.map { case (_, result) => {
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val region = Bytes.toString(result.getValue("info".getBytes, "REGION".getBytes))
      val nunits = Bytes.toString(result.getValue("info".getBytes, "NUNITS".getBytes))
      (region, nunits)
    }
    }.map(x => {
      if (Integer.parseInt(x._1) == 1) {
        (1, x._2)
      } else if (Integer.parseInt(x._1) == 2) {
        (2, x._2)
      } else if (Integer.parseInt(x._1) == 3) {
        (3, x._2)
      } else {
        (4, x._2)
      }
    }).map(x => {
      val nunits = Integer.parseInt(x._2)
      if (nunits == 1) {
        (x._1, (1, 0))
      } else {
        (x._1, (0, 1))
      }
    }).reduceByKey((x1, x2) => (x1._1 + x2._1, x1._2 + x2._2)).foreach(println)
    sc.stop()
    true
  }
}
