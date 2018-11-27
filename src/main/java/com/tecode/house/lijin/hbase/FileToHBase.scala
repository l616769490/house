package com.tecode.house.lijin.hbase

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object FileToHBase {
  val conf = new SparkConf().setAppName("FileToHBase").setMaster("local[*]")
  val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

  def main(args: Array[String]): Unit = {
    upload("D:\\2013.csv")
  }

  def upload(path: String, overWrite: String = "null"): Unit = {
    sparkSession.read.csv(path).show()
  }
}
