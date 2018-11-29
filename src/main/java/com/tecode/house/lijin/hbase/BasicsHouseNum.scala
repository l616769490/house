package com.tecode.house.lijin.hbase

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.util.Bytes

/**
  * 基础-房间数
  */
class BasicsHouseNum extends Analysis {
  private val conf = new SparkConf().setAppName("BasicsHouseNum").setMaster("local[*]")
  private val sc = new SparkContext(conf)
  private val hBaseConf = HBaseConfiguration.create

  // 读取数据
  def readHBase(tableName: String): RDD[(ImmutableBytesWritable, Result)] = {
    hBaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val hBaseRDD = readHBase(tableName)
    hBaseRDD.foreach( s => {
      println(Bytes.toString(s._2.getRow) )
    }  )
//    { case (_, result) =>
//      val key = Bytes.toInt(result.getRow)
//      val cells = result.rawCells()
//      for (cell <- cells) {
//        // 列族
//        val family = Bytes.toString(CellUtil.cloneFamily(cell))
//        // 列名
//        val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
//        // 值
//        val value = Bytes.toString(CellUtil.cloneValue(cell))
//        print("key" + key)
//      }
//    }
    false
  }
}

object BasicsHouseNum {
  def main(args: Array[String]): Unit = {
    val basicsHouseNum = new BasicsHouseNum()
    basicsHouseNum.analysis("thads:2013")
  }
}
