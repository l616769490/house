package com.tecode.house.jianchenfei.hbasedata

import java.sql.ResultSet

import com.mysql.jdbc.{Connection, PreparedStatement}
import com.tecode.house.d01.service.Analysis
import com.tecode.house.jianchenfei.jdbc.bean.Report
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao
import com.tecode.house.jianchenfei.jdbc.dao.impl.ReportImpl
import com.tecode.house.jianchenfei.utils.ConnSource
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
    }}.map(x=>{
      if(x.equals("1")){
        ("1",1)
      }else if(x.equals("2")){
        ("2",1)
      }else if(x.equals("3")){
        ("3",1)
      }else if(x.equals("4")){
        ("4",1)
      }else if(x.equals("5")){
        ("5",1)
      } else if(x.equals("6")){
        ("6",1)
      }else{
        ("6+",1)
      }
    }).reduceByKey(_+_).foreach(println)

    /**
      * 获取房产税数据，按建成年份划分
      */
    val RateRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val built = Bytes.toString(result.getValue("info".getBytes,"BUILT".getBytes))
      val zsmhc = Bytes.toString(result.getValue("info".getBytes,"ZSMHC".getBytes))
      (built,zsmhc)
      //转换成年份区间
    }}.map(x => {
      if ( Integer.parseInt(x._1)<2000 && Integer.parseInt(x._1)>1900 ) {
        ("1900-2000", x._2)
      } else if (2000<Integer.parseInt(x._1) && Integer.parseInt(x._1)<2010){
        ("2000-2010",x._2)
    }else {
        ("2010-2018",x._2)
      }
      })
      // (年份,(0-500， 500-1000， 1000-1500， 1500-2000， 2000-2500， 2500-3000， 3000-3500， 3500+))
      .map(
        x => {
          val ZSMHC = Integer.parseInt(x._2)
          if( ZSMHC < 500) {
            (x._1,(1,0,0,0,0,0,0,0))
          } else if(ZSMHC < 1000) {
            (x._1,(0,1,0,0,0,0,0,0))
          } else if(ZSMHC < 1500) {
            (x._1,(0,0,1,0,0,0,0,0))
          } else if(ZSMHC < 2000){
            (x._1,(0,0,0,1,0,0,0,0))
          } else if(ZSMHC < 2500){
            (x._1,(0,0,0,0,1,0,0,0))
          } else if(ZSMHC < 3000){
            (x._1,(0,0,0,0,0,1,0,0))
          } else if(ZSMHC < 3500){
            (x._1,(0,0,0,0,0,0,1,0))
          } else{
            (x._1,(0,0,0,0,0,0,0,1))
          }
        }
    ).reduceByKey( (x1, x2) => (x1._1 + x2._1, x1._2 + x2._2,x1._3 + x2._3,x1._4 + x2._4,x1._5 + x2._5,x1._6+x2._6,x1._7 + x2._7,x1._1 + x2._8))
      .foreach(println)





    /**
      * 获取独栋比例数据，按照区域划分
      */
      val SingleRDD = hBaseRDD.map{case (_,result) =>{
      //获取行键
      //val key = Bytes.toString(result.getRow)
      //通过列族和列名获取列
      val region = Bytes.toString(result.getValue("info".getBytes,"REGION".getBytes))
      val nunits = Bytes.toString(result.getValue("info".getBytes,"NUNITS".getBytes))
      (region,nunits)
    }}.map(x=>{
      if (Integer.parseInt(x._1)==1){
        (1,x._2)
      }else if(Integer.parseInt(x._1)==2){
        (2,x._2)
      }else if(Integer.parseInt(x._1)==3){
        (3,x._2)
      }else{
        (4,x._2)
      }
    }).map(x=>{
        val nunits = Integer.parseInt(x._2)
        if(nunits == 1){
          (x._1,(1,0))
        }else {
          (x._1,(0,1))
        }
      }).reduceByKey((x1,x2)=>(x1._1 + x2._1,x1._2+x2._2)).foreach(println)
    sc.stop()

    true
  }
  def packageDate(tableName: String) = {
    var conn: Connection = null;
    val ps: PreparedStatement = null;
    val rs: ResultSet = null;
    val dao: ReportImpl = new ReportImpl();
    try {
      //conn = ConnSource.getConnection();
      //事务控制，开启事务
      conn.setAutoCommit(false);
      //      插入报表表
      val report: Report = new Report()
      report.setName("家庭人数统计")
      report.setCreate(System.currentTimeMillis())
      report.setYear(Integer.valueOf(tableName.split(":")(1)))
      report.setGroup("年份统计")
      report.setStatus(1)
      val reportId: Int = dao.insert(report)

    }

  }}