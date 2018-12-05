package com.tecode.house.jianchenfei.hbasedata

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/5.
  */
class Get_Zsmhc_built extends Analysis{
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("getzsmhc_built").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val hbaseconf = HBaseConfiguration.create

    hbaseconf.set(TableInputFormat.INPUT_TABLE, tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val RateRDD = hBaseRDD.map { case (_, result) => {
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val built = Bytes.toString(result.getValue("info".getBytes, "BUILT".getBytes))
      val zsmhc = Bytes.toString(result.getValue("info".getBytes, "ZSMHC".getBytes))
      (built, zsmhc)
      //转换成年份区间
    }
    }.map(x => {
      if (Integer.parseInt(x._1) < 2000 && Integer.parseInt(x._1) > 1900) {
        ("1900-2000", x._2)
      } else if (2000 < Integer.parseInt(x._1) && Integer.parseInt(x._1) < 2010) {
        ("2000-2010", x._2)
      } else {
        ("2010-2018", x._2)
      }
    })
      // (年份,(0-500， 500-1000， 1000-1500， 1500-2000， 2000-2500， 2500-3000， 3000-3500， 3500+))
      .map(
      x => {
        val ZSMHC = Integer.parseInt(x._2)
        if (ZSMHC < 500) {
          (x._1, (1, 0, 0, 0, 0, 0, 0, 0))
        } else if (ZSMHC < 1000) {
          (x._1, (0, 1, 0, 0, 0, 0, 0, 0))
        } else if (ZSMHC < 1500) {
          (x._1, (0, 0, 1, 0, 0, 0, 0, 0))
        } else if (ZSMHC < 2000) {
          (x._1, (0, 0, 0, 1, 0, 0, 0, 0))
        } else if (ZSMHC < 2500) {
          (x._1, (0, 0, 0, 0, 1, 0, 0, 0))
        } else if (ZSMHC < 3000) {
          (x._1, (0, 0, 0, 0, 0, 1, 0, 0))
        } else if (ZSMHC < 3500) {
          (x._1, (0, 0, 0, 0, 0, 0, 1, 0))
        } else {
          (x._1, (0, 0, 0, 0, 0, 0, 0, 1))
        }
      }
    ).reduceByKey((x1, x2) => (x1._1 + x2._1, x1._2 + x2._2, x1._3 + x2._3, x1._4 + x2._4, x1._5 + x2._5, x1._6 + x2._6, x1._7 + x2._7, x1._1 + x2._8))
      .foreach(println)
    sc.stop()
    true
  }
}
