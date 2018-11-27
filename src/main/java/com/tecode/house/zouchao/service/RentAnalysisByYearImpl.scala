package com.tecode.house.zouchao.service

import com.tecode.house.d01.service.Analysis
import com.tecode.house.zouchao.dao.DaoImpl.HBaseScalaDaoImpl
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.parser.SqlBaseParser.QualifiedNameContext
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

class RentAnalysisByYearImpl extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    val dao = new HBaseScalaDaoImpl()
    val conf = new SparkConf().setAppName("analysisRentByYear").setMaster("local[*]")
    val sc = new SparkContext(conf)
    //    获取各年份区间的平均租金
    val rentResult: Array[(String, Double)] = analysis(dao, sc, tableName, "FMR")
    //    获取各年份区间的平均价格
    val valueResult: Array[(String, Double)] = analysis(dao, sc, tableName, "VALUE")
    true
  }

  /**
    * 按建成年份统计存入的数据
    *
    * @param dao           HBaseScalaDaoImpl
    * @param sc            SparkContext
    * @param tableName     表名
    * @param qualifiedname 要统计的列
    * @return
    */
  def analysis(dao: HBaseScalaDaoImpl, sc: SparkContext, tableName: String, qualifiedname: String): Array[(String, Double)] = {
    val tuple: ArrayBuffer[(Int, Int)] = dao.getAllByCreateYear(tableName, qualifiedname)
    //    将数组转换为RDD=>(年份，值)
    val tuples: ArrayBuffer[(Int, Int)] = ArrayBuffer[(Int, Int)]()
    //    过滤，去除无效数据（值小于0）
    for (elem <- tuple) {
      if (elem._2 > 0) {
        //        将有效数据添加到一个新的数组中
        tuples += elem
      }
    }
    //    创建RDD
    val RDD: RDD[(Int, Int)] = sc.makeRDD(tuples)
    //    将具体的年份转换为年份区间
    val value: RDD[(String, Int)] = RDD.map(x => {
      if (x._1 < 2000) {
        ("(1900,2000)", x._2)
      } else if (x._1 < 2010) {
        ("(2000,2010)", x._2)
      } else {
        ("(2010,2018)", x._2)
      }
    })
    //    根据年份区间分组
    val group: RDD[(String, Iterable[Int])] = value.groupByKey()
    //    求各年份区间的平均值
    val re: RDD[(String, Double)] = group.map(x => {
      val list: List[Int] = x._2.toList
      val count = list.size
      val sum: Int = list.sum
      val avg: Double = sum.toDouble / count
      (x._1, avg)
    })
    val result: Array[(String, Double)] = re.collect()
    result
  }

  def packageRent(tuples: Array[(String, Int)], average: Double, max: Int, min: Int): Boolean = {


    true
  }
}
