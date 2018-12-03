package com.tecode.house.dengya.service

import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{HBaseAdmin, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object NunitsSpark {

  val sparkconf = new SparkConf().setAppName("Hbase").setMaster("local[*]")
  val sc = new SparkContext(sparkconf)

  def main(args: Array[String]): Unit = {

  // read("thads:2013","STRUCTURETYPE",sc)
    UnitsSpark()

}

  def read(tableName: String, qualifiername: String, sc: SparkContext): RDD[Int] = {
    //    配置HBase参数
    val hconf = HBaseConfiguration.create()
    //    配置读取的表名
    hconf.set(TableInputFormat.INPUT_TABLE, tableName)
    //    配置读取的列族
    hconf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //    获取数据
    val valuess: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(hconf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])

    val v: RDD[Result] = valuess.map(x => x._2)
    //    取出Result结果中的建筑类型的列
   // v.foreach(println).toString
    val valueRDD =  v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()
      var value: Int = 0

      for (elem <- cells) {
        //        获取传入的列的值
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          value = Bytes.toString(CellUtil.cloneValue(elem)).toInt

      }
       value

    })
    //    去除无效数据（value小于0的数据）
    val value = valueRDD.filter(_ > 0)
      // value.foreach(println)

    value
  }

//分析建筑单元数
  def UnitsSpark(): Unit ={
    val value:RDD[Int] = read("thads:2013","STRUCTURETYPE",sc)
    val count: Long = value.count()
    val unit: RDD[(Int, Int)] = value.map((_,1)).reduceByKey(_+_)
      for(i <- unit){
          val Units = i._2 / count.toDouble
        println(i._1,Units)
      }
  }


}



