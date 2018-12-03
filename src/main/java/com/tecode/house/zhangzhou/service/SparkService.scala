package com.tecode.house.zhangzhou.service

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkConf, SparkContext}

class SparkService {
  val sparkConf = new SparkConf().setMaster("local").setAppName("selectData")
  val sc = new SparkContext(sparkConf)
  val tableName = "thads:2013"
  val hbaseConf = HBaseConfiguration.create()
  //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
  hbaseConf.set("hbase.zookeeper.quorum","hadoop111,hadoop112,hadoop113")
  //设置zookeeper连接端口，默认2181
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
  //获得空置状态
  def selectVacancyState(): Unit ={
    val hbaseRDD = sc.newAPIHadoopRDD(hbaseConf,classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val houseCount= hbaseRDD.count()
    println("houseCount:"+houseCount)
    var vacancyCount:Int = 0
    var checkInCount:Int = 0
    var unknownCount:Int = 0
    /*hbaseRDD.foreach { case (_, result) =>
      val vac = Bytes.toString(result.getValue("info".getBytes(), "VACANCY".getBytes()))
      vac
      if (vac.equals("1")) {
        vacancyCount += 1
        println(vacancyCount + "a")
      }
      if (vac.toString.equals("-6")) {
        unknownCount += 1
        println(unknownCount + "b")
      }
      if (vac.equals("3")) {
        checkInCount += 1
        println(checkInCount + "c")
      }
      // println("vacancy:"+vac)
    }

    println("-----------")
    println("vacancyCount:"+vacancyCount)
    println("checkInCount:"+checkInCount)
    println("unknownCount:" + unknownCount)
    sc.stop()*/

    val sss = hbaseRDD.map(x => Bytes.toString(x._2.getValue(Bytes.toBytes("info"),Bytes.toBytes("VACANCY")))).map((_,1)).reduceByKey(_+_).collect().foreach(println)



  }


}

object SparkService{
  def main(args: Array[String]): Unit = {
    val ss = new SparkService
    ss.selectVacancyState()
  }
}
