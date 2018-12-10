package com.tecode.house.dengya.service.impl

import java.sql.{Connection, SQLException}

import com.tecode.house.dengya.bean._
import com.tecode.house.dengya.d01.service.Analysis
import com.tecode.house.dengya.dao.MySQLDao
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl
import com.tecode.house.dengya.utils.MySQLUtil
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

class NunitsSpark extends Analysis{

  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */

  override def analysis(tableName:String):Boolean ={
    val sparkconf = new SparkConf().setAppName("Hbase").setMaster("local[*]")
    val sc = new SparkContext(sparkconf)
    //调用读取数据的方法
    val unitRDD: RDD[Int] = read(tableName,"STRUCTURETYPE",sc)
    val unit: RDD[(String, String)] = UnitsSpark(unitRDD)
    //把单元数转换为list集合
    val unitbuffer: List[(String, String)] = unit.collect().toList
    toMysqlData(tableName,unitbuffer)
    sc.stop()
      true
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
  def UnitsSpark(rdd:RDD[Int]):RDD[(String,String)] ={
    val count: Long = rdd.count()
    println(count)
    val unit: RDD[(Int, Int)] = rdd.map((_,1)).reduceByKey(_+_)
    unit.foreach(println)
      val value =  unit.map( x => {
        val Units = x._2 / count.toDouble

        (x._1, Units.toString)

      }).map( x => {
        if(x._1 == 1){
          ("singleFamliy",x._2)
        }else if(x._1 == 2){
          ("2-4units",x._2)
        }else if(x._1 == 3){
          ("5-19units",x._2)
        }else if(x._1 == 4){
          ("20-49units",x._2)
        }else if(x._1 == 5){
          ("50+units",x._2)
        }else{
          ("MobileHome",x._2)
        }
      })
      value


  }

    def toMysqlData(tableName:String,unitList:List[(String,String)])= {
      var conn: Connection = null;
      val dao: MySQLDao = new MySQLDaoImpl();

      conn = MySQLUtil.getConn()
      conn.setAutoCommit(false)

      //插入报表表
      val report: Report = new Report()
      report.setName("建筑单元分布图");
      report.setCreate(System.currentTimeMillis());
      report.setYear(Integer.parseInt(tableName.split(":")(1)));
      report.setGroup("基础分析");
      report.setStatus(1);
      report.setUrl("http://166.166.4.20:8080/units_option");
      val reportId: Int = dao.putInTableReport(conn, report)
      println("插入中..........")

      //饼状图
      //建筑单元数饼状图
      val diagram: Diagram = new Diagram()
      diagram.setName("建筑单元分布图饼状图")
      diagram.setType(2)
      diagram.setReportId(reportId)
      diagram.setSubtext("饼状图")
      val diagramId: Int = dao.putInTableDiagram(conn, diagram)


      //插入x轴表
      val xaxis: Xaxis = new Xaxis()
      xaxis.setName("空维度")
      xaxis.setDiagramId(diagramId)
      xaxis.setDimGroupName("空维度")
      val xaxisId: Int = dao.putInTableXaxis(conn, xaxis)

      //插入y轴表
      val yaxis: Yaxis = new Yaxis()
      yaxis.setName("空维度")
      yaxis.setDiagramId(diagramId)
      val yaxisId: Int = dao.putInTableYaxis(conn, yaxis)


      //数据集表
      val legend: Legend = new Legend()
      legend.setName("建筑单元数统计")
      legend.setDimGroupName("建筑结构类型")
      legend.setDiagramId(diagramId)
      val legendId: Int = dao.putInTableLegend(conn, legend)

      //数据表
      //插入建筑单元数
      for (elem <- unitList) {
        val data: Data = new Data()
        data.setValue(elem._2.toString)
        data.setxId(xaxisId)
        data.setLegendId(legendId)
        data.setX("空维度")
        data.setLegend(elem._1.toString)
        dao.putInTableData(conn, data)
      }
      println("插入中.......")

      //搜索表
      val search: Search = new Search()
      search.setName("单元数")
      search.setDimGroupName("单元数")
      search.setReportId(reportId)
      val searchId: Int = dao.putInTableSearch(conn, search)
      println("正在插入.......")
      conn.commit()
      //回滚事务
      conn.rollback();
      MySQLUtil.close(conn)


    }

}
