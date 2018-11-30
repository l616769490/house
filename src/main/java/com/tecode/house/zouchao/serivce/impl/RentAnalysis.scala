package com.tecode.house.zouchao.serivce.impl


import java.util

import com.tecode.house.d01.service.Analysis
import com.tecode.house.zouchao.bean.Rent
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


class RentAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("rentAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    调用读取数据的方法
    val rentsRDD: RDD[Int] = read(tableName, sc)
    //将具体的租金转换为租金区间并计数
    val rents: RDD[(String, Int)] = rentsRDD.map(x => {
      if (x < 1000) {
        ("(0,1000)", 1)
      } else if (x < 1500) {
        ("(1000,1500)", 1)
      } else if (x < 2000) {
        ("(1500,2000)", 1)
      } else if (x < 2500) {
        ("(2000,2500)", 1)
      } else if (x < 3000) {
        ("(2500,3000)", 1)
      } else {
        ("(3000,)", 1)
      }
    })
    //    统计各租金区间的总数
    val value: RDD[(String, Int)] = rents.reduceByKey(_ + _)
    //    求最大租金
    val max: Int = rentsRDD.max()
    //    求最小租金
    val min: Int = rentsRDD.min()
    //    求平均租金
    val count: Long = rentsRDD.count()
    val sum: Double = rentsRDD.sum()
    val avg: Double = sum / count
    //    println("max: " + max)
    //    println("min: " + min)
    //    println("avg: " + avg)
    //    value.collect().foreach(println)
    //    将int类型封装为Integer类型，并将RDD变成Buffer
    val buffer: mutable.Buffer[(String, Integer)] = value.map(x => (x._1, {
      Integer.valueOf(x._2)
    })).collect().toBuffer
    //    将Buffer类型转换为java的List类型
    val list: util.List[(String, Integer)] = scala.collection.JavaConversions.bufferAsJavaList(buffer)
    //    封装对象
    val rent: Rent = new Rent(max, min, avg, list)
    //    调用封装方法
    packageDate(rent)
    sc.stop()
    true
  }

  /**
    * 从HBase读取数据
    *
    * @param tableName 表名
    * @param sc        SparkContext
    * @return 租金的RDD
    */
  def read(tableName: String, sc: SparkContext): RDD[Int] = {
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
    val rentsRDD: RDD[Int] = v.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0;
      for (elem <- cells) {
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("FMR"))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      value
    })
    rentsRDD
  }


  def packageDate(rent: Rent) = {
    //      插入报表表



    //    插入图表表
    //    插入x轴表
    //    插入y轴表
    //    插入数据集表
    //    插入数据表
    //    插入搜索表
  }
}
