package com.tecode.house.zouchao.serivce.impl

import java.util

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

import scala.collection.mutable


class PriceByBuildAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("PriceByBuildAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    调用读取数据的方法
    val rentsRDD: RDD[(Int, Int)] = read(tableName, "FMR", sc)
    val valueRDD: RDD[(Int, Int)] = read(tableName, "VALUE", sc)
    //    调用分析方法，求各区间的均值
    //    租金
    val rent: RDD[(String, Double)] = calculation(rentsRDD)
    //    println("租金===========")
    //    rent.collect().foreach(println)
    //    println("价格==================")
    //    value.collect().foreach(println)

    //    将Buffer类型转换为java的List类型
    val rentbuffer: mutable.Buffer[(String, Double)] = rent.collect().toBuffer
    val rentlist: util.List[(String, Double)] = scala.collection.JavaConversions.bufferAsJavaList(rentbuffer)



    //    价格
    val value: RDD[(String, Double)] = calculation(valueRDD)

    //    将Buffer类型转换为java的List类型
    val valuebuffer: mutable.Buffer[(String, Double)] = value.collect().toBuffer
    val valuelist: util.List[(String, Double)] = scala.collection.JavaConversions.bufferAsJavaList(valuebuffer)
    sc.stop()
    true
  }

  def read(tableName: String, qualifiername: String, sc: SparkContext): RDD[(Int, Int)] = {
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的租金列的值
    val rentsRDD: RDD[(Int, Int)] = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0
      var year: Int = 0
      for (elem <- cells) {
        //        获取传入的列的值
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        //        获取建成年份
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BUILT"))
          year = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      (year, value)
    })
    //    去除无效数据（value小于0的数据）
    val value: RDD[(Int, Int)] = rentsRDD.filter(_._2 > 1)
    //    value.collect().foreach(println)
    value
  }

  /**
    * 分析数据
    *
    * @param rdd
    * @return 包含建成年份区间及其平均值的RDD
    */
  def calculation(rdd: RDD[(Int, Int)]): RDD[(String, Double)] = {
    //    val d = rdd.map(_._2).sum()
    //    val l = rdd.map(_._2).count()
    //    将具体的建成年份转换成建成年份区间
    val result: RDD[(String, Double)] = rdd.map(x => {
      if (x._1 < 2000) {
        ("(1900,2000)", x._2)
      } else if (x._1 < 2010) {
        ("(2000,2010)", x._2)
      } else {
        ("(2010,2018)", x._2)
      }
    })
    //按建成年份区间分组
    val value: RDD[(String, Iterable[Double])] = result.groupByKey()
    //    计算每个建成年份区间的平均值
    val v: RDD[(String, Double)] = value.map(x => {
      val list: List[Double] = x._2.toList
      val sum: Double = list.sum
      val size: Int = list.size
      val avg: Double = sum / size
      (x._1, avg)
    })
    v
  }
}
