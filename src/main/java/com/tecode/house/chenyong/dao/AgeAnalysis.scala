package com.tecode.house.chenyong.dao

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util

import com.tecode.house.chenyong.bean.Age
import com.tecode.house.chenyong.dao.impl.MySQLDaoImpl
import com.tecode.house.d01.service.Analysis
import com.tecode.house.lijin.utils.SparkUtil
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

class AgeAnalysis extends Analysis{

  def readData(tableName: String, sc: SparkContext): RDD[Int] = {

    //配置HBase链接
    val conf = HBaseConfiguration.create();
    //读取的表名
    conf.set(TableInputFormat.INPUT_TABLE,tableName)
    //读取的列族
    conf.set(TableInputFormat.SCAN_COLUMNS,"info")
    //获取数据
    val values:RDD[(ImmutableBytesWritable,Result)] = sc.newAPIHadoopRDD(conf,classOf[TableInputFormat],classOf[ImmutableBytesWritable],classOf[Result])
    val value:RDD[Result] = values.map(x => x._2)
    //取出Result结果中年龄列的值
    val ageRDD:RDD[Int] = value.map(x => {
      val cells: Array[Cell] = x.rawCells()
      var age:Int = 0
      for (elem <- cells) {
        if(Bytes.toString(CellUtil.cloneQualifier(elem)).equals("AGE1")){
          age = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        }
      }
      age
    })
    ageRDD
  }

  def packageAgeData(age: Age, tableName: String) = {
    //创建链接
    var conn:Connection = null
    val ps: PreparedStatement = null
    val rs: ResultSet = null
    val msDao: MySQLDao = new MySQLDaoImpl()


  }

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    val sc = SparkUtil.getSparkContext

    val ageRDD: RDD[Int] = readData(tableName,sc)

    //将年龄转换为在各年龄区间并进行计数
    val ageZone:RDD[(String,Int)] = ageRDD.map(x => {
      if(x < 18){
        ("18以下",1)
      }else if(x >= 18 && x <= 40){
        ("18-40",1)
      }else if(x >=41  && x < 65){
        ("41-65",1)
      }else{
        ("65以上",1)
      }
    })

    //统计各年龄区间的总数
    val value:RDD[(String,Int)] = ageZone.reduceByKey(_ + _ )
    //求最大年龄
    val maxAge:Int = ageRDD.max()
    //求最小年龄
    val minAge:Int = ageRDD.min()
    //统计总人数
    val count:Long = ageRDD.count()
    //统计总年龄
    val sumAge:Double = ageRDD.sum()
    val avgAge:Double = sumAge/count

    //将获得的数据转换为buffer
    val changeBuffer:mutable.Buffer[(String,Integer)] = value.map(x => (x._1,{
      Integer.valueOf(x._2)
    })).collect().toBuffer
    //将buffer转换为java的list类型
    val tuples: util.List[(String, Integer)] = scala.collection.JavaConversions.bufferAsJavaList(changeBuffer)

    //封装进Age对象
    val age : Age = new Age(maxAge,minAge,avgAge,tuples);

    //调用封装方法

    packageAgeData(age,tableName)
    return true
  }
}
