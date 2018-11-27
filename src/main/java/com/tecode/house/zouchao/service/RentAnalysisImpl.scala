package com.tecode.house.zouchao.service

import com.tecode.house.d01.service.Analysis
import com.tecode.house.zouchao.dao.DaoImpl.HBaseScalaDaoImpl
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

class RentAnalysisImpl extends Analysis {

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    val dao = new HBaseScalaDaoImpl()
    val conf = new SparkConf().setAppName("analysisRent").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    调用HBaseScalaDaoImpl的方法获取公平市场租金
    val rents: ArrayBuffer[Integer] = dao.getAllRent(tableName)
    //    将数组转换为RDD
    val rentRDD: RDD[Int] = sc.makeRDD(rents).map(_.toInt)
    //    将int转换为元祖
    val values: RDD[(String, Int)] = rentRDD.map(x => {
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
        ("3000+", 1)
      }
    })
    //    求各区间的数量
    val value: RDD[(String, Int)] = values.reduceByKey(_ + _)
    val tuples: Array[(String, Int)] = value.collect()
    //求总数量
    val num = rentRDD.count()
    //    求总和
    val sum: Double = rentRDD.sum()
    //    求平均租金
    val average = sum / num
    //    求最大租金
    val max: Int = rentRDD.max()
    //    求最小租金
    val min: Int = rentRDD.min()
    //封装数据
    val bool = packageRent(tuples, average, max, min)
    bool

  }

  def packageRent(tuples: Array[(String, Int)], average: Double, max: Int, min: Int): Boolean = {




    true
  }
}
