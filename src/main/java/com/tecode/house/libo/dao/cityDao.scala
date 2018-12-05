package com.tecode.house.libo.dao

import com.tecode.house.d01.service.Analysis
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{HTable, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class cityDao extends Analysis{

  /**
    * 自住\租赁
    */
//  def selfOrRent():Unit={
//    //val table = new HTable(conf,"thads:201352");
//    //读取数据转化为RDD
//
//    hBaseRDD.map{case (_,result) =>{
//      //val key = Bytes.toString(result.getRow)
//      //通过列族和列名获取列
//     Bytes.toString(result.getValue("info".getBytes,"TENURE".getBytes))
//    }}.map(x=>(x,1)).reduceByKey(_+_).foreach(println)
//    sc.stop()
//  }


  /**
    *城市规模统计：房间总数和卧室数
    */
  def ROOMS(): Unit ={

    //取出有效的数据
//     val as = hBaseRDD.map{case (_,result) =>{
//       val key = Bytes.toString(result.getRow)
//      // Bytes.toString(result.getValue("info".getBytes(),"METRO3".getBytes()));
//       Bytes.toString(result.getValue("info".getBytes(),"ROOMS".getBytes()));
//       Bytes.toString(result.getValue("info".getBytes(),"BEDRMS".getBytes()))
//    val as = hBaseRDD.map{x =>(
//
//    ) }}.filter(x=>(x.toInt>0))


  }

  /**
    * 在hbase读取数据
    */
  def read(tableName:String,sc:SparkContext): RDD[Int] ={
    val sparkConf = new SparkConf().setAppName("HbaseApp").setMaster("local[*]");
    val sc = new SparkContext(sparkConf);

    val conf = HBaseConfiguration.create();
    conf.set(TableInputFormat.INPUT_TABLE,tableName)
    conf.set(TableInputFormat.SCAN_COLUMNS, "info")
    //获取数据
    val hBaseRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    val value = hBaseRDD.map(x => x._2)
    val rentsRDD: RDD[Int]=value.map(x =>{
      val cells = x.rawCells()
      var v :Int = 0;
      var w :Int = 0;
      for(elem <- cells){
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("ROOMS"))
          v = Bytes.toString(CellUtil.cloneValue(elem)).toInt
        else (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("BEDRMS"))
        w = Bytes.toString(CellUtil.cloneValue(elem)).toInt
      }
      v
    })
    rentsRDD
  }

//  /**
//    * 总卧室数
//    */
//  def BEDRMS(): Unit ={
//    import spark.implicits._
//    val as = hBaseRDD.map{case (_,result) =>{
//    //  Bytes.toString(result.getValue("info".getBytes(),"ROOMS".getBytes()));
//      Bytes.toString(result.getValue("info".getBytes(),"BEDRMS".getBytes()))
//    }}.filter(x=>(x.toInt>0)).toDF("b")
//
//    as.createOrReplaceTempView("bb")
//    spark.sql("select sum(b) from bb").show()
//
//  }
//
//  /**
//    * 按照建成年份：独栋建筑比例
//    */
//
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val conf = new SparkConf().setAppName("rentAnalysis").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val rentsRDD: RDD[Int] = read(tableName, sc)


    true
  }
}
