package com.tecode.house.jianchenfei.hbasedata

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Administrator on 2018/12/3.
  */
class BasicAnalysis extends Analysis{

  val conf = new SparkConf().setAppName("BasicAnalysis").setMaster("local[*]")
  val sc = new SparkContext(conf)
  val hbaseconf = HBaseConfiguration.create
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {

    hbaseconf.set(TableInputFormat.INPUT_TABLE,tableName)
    val hBaseRDD = sc newAPIHadoopRDD(hbaseconf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])


    /**
      * 获取家庭人数数据
      */
    val PERRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      Bytes.toString(result.getValue("info".getBytes,"PER".getBytes))
    }}.map(x=>(x,1)).reduceByKey(_+_).groupBy(_._1).foreach(println)

    /**
      * 获取房产税数据，按建成年份划分
      */
    val RateRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      Bytes.toString(result.getValue("info".getBytes,"ZSMHC".getBytes))
    }}.map(x=>(x,1)).reduceByKey(_+_).foreach(println)


    /**
      * 获取独栋比例数据，按照区域划分
      */
    val SingleRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      Bytes.toString(result.getValue("info".getBytes,"NUNITS".getBytes))
    }}.map(x=>(x,1)).reduceByKey(_+_).foreach(println)

    sc.stop()

    true
  }
}
object BasicAnalysis {
  def main(args: Array[String]): Unit = {
    val basicAnalysis = new BasicAnalysis()
    basicAnalysis.analysis("thads:2013")
  }
}
