package com.tecode.house.dengya.service.impl

import java.sql.Connection

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

class PriceByCity extends Analysis {



  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    val sparkconf = new SparkConf().setAppName("Hbase").setMaster("local[*]")
    val sc = new SparkContext(sparkconf)
    val rentRDD: RDD[(Int, Int)] = read(tableName,"FMR",sc)
    val priceRDD: RDD[(Int, Int)] = read(tableName,"VALUE",sc)
    val rent: RDD[(String, Double)] = UnitsSpark(rentRDD)
    val rentBuffer = rent.collect().toList
    val price: RDD[(String, Double)] = UnitsSpark(priceRDD)
    val priceBuffer: List[(String, Double)] = price.collect().toList
    toMysqlData(tableName,rentBuffer,priceBuffer)
    sc.stop()
    true
  }

  //租金价格
  def read(tableName: String, qualifiername: String, sc: SparkContext): RDD[(Int, Int)] = {
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

    val valueRDD = v.map(x => {
      //      遍历每个Result对象，获取数据
      val cells: Array[Cell] = x.rawCells()

      var city = 0
      var fmr = 0
      for (elem <- cells) {
        //        获取传入的租金列的值
        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals(qualifiername))
          fmr = Bytes.toString(CellUtil.cloneValue(elem)).toInt

        if (Bytes.toString(CellUtil.cloneQualifier(elem)).equals("METRO3"))
          city = Bytes.toString(CellUtil.cloneValue(elem)).toInt

      }


      (city, fmr)

    })
    //    去除无效数据（value小于0的数据）
    val value = valueRDD.filter(_._2 > 0)


    value
  }

  //按城市规模统计租金
  def UnitsSpark(rdd: RDD[(Int, Int)]): RDD[(String, Double)] = {

    // val rentByCity: RDD[(Int, Int)] = value.reduceByKey(_+_)
    // val countByCity: RDD[(Int, Int)] = value.map{ x => (x._1,1)}.reduceByKey(_+_)

    val result: RDD[(Int, Double)] = rdd.map(x => {
      (x._1, x._2.toDouble)
    })

    val value: RDD[(Int, Iterable[Double])] = result.groupByKey()

    val v = value.map(x => {
      val list: List[Double] = x._2.toList
      val sum = list.sum
      val size = list.size
      val avg: Double = sum / size
      (x._1, avg)

    })
    val finalrdd = v.map(x => {
      (x._1.toString, x._2)
    }).map(x => {
      if (x._1 == "1") {
        ("一级城市", x._2)
      } else if (x._1 == "2") {
        ("二级城市", x._2)
      } else if (x._1 == "3") {
        ("三级城市", x._2)
      } else if (x._1 == "4") {
        ("四级城市", x._2)
      } else {
        ("五级城市", x._2)
      }
    })

    finalrdd

  }

  def toMysqlData(tableName: String,rentList: List[(String, Double)], priceList: List[(String, Double)]) = {
    var conn: Connection = null;
    val dao: MySQLDao = new MySQLDaoImpl();

    conn = MySQLUtil.getConn()
    conn.setAutoCommit(false)

    //插入报表表
    val report: Report = new Report()
    report.setName("市场价格分布图");
    report.setCreate(System.currentTimeMillis());
    report.setYear(Integer.parseInt(tableName.split(":")(1)));
    report.setGroup("城市规模统计");
    report.setStatus(1);
    report.setUrl("http://166.166.4.20:8080/price_city");
    val reportId: Int = dao.putInTableReport(conn, report)
    println("插入中..........")

    //柱状图
    //建筑单元数柱状图
    val diagram: Diagram = new Diagram()
    diagram.setName("按城市规模统计住房价格平均值")
    diagram.setType(1)
    diagram.setReportId(reportId)
    diagram.setSubtext("统计各城市规模住房售价和住房租金平均值")
    val diagramId: Int = dao.putInTableDiagram(conn, diagram)


    //插入x轴表
    val xaxis: Xaxis = new Xaxis()
    xaxis.setName("城市规模")
    xaxis.setDiagramId(diagramId)
    xaxis.setDimGroupName("城市规模")
    val xaxisId: Int = dao.putInTableXaxis(conn, xaxis)

    //插入y轴表
    val yaxis: Yaxis = new Yaxis()
    yaxis.setName("美元")
    yaxis.setDiagramId(diagramId)
    val yaxisId: Int = dao.putInTableYaxis(conn, yaxis)


    //数据集表
    val legend: Legend = new Legend()
    legend.setName("价格统计")
    legend.setDimGroupName("价格统计")
    legend.setDiagramId(diagramId)
    val legendId: Int = dao.putInTableLegend(conn, legend)

    //数据表
    //插入建筑单元数
    //插入房屋平均租金
    for (elem <- rentList) {
      val data: Data = new Data()
      data.setValue(elem._2.toString)
      data.setxId(xaxisId)
      data.setLegendId(legendId)
      data.setX(elem._1)
      data.setLegend("平均租金")
      dao.putInTableData(conn, data)
    }
    //插入房屋平均价格
    for (elem <- priceList) {
      val data: Data = new Data()
      data.setValue(elem._2.toString)
      data.setxId(xaxisId)
      data.setLegendId(legendId)
      data.setX(elem._1)
      data.setLegend("平均价格")
      dao.putInTableData(conn, data)
    }
    println("插入中.......")

    //搜索表
    val citySearch: Search = new Search()
    citySearch.setName("城市规模搜索")
    citySearch.setDimGroupName("城市规模")
    citySearch.setReportId(reportId)
    dao.putInTableSearch(conn, citySearch)
    val rentSearch: Search = new Search()
    rentSearch.setName("住房租金区间搜索")
    rentSearch.setDimGroupName("租金")
    rentSearch.setReportId(reportId)
    dao.putInTableSearch(conn, rentSearch)
    val priceSearch: Search = new Search()
    priceSearch.setName("住房售价区间搜索")
    priceSearch.setDimGroupName("价格")
    priceSearch.setReportId(reportId)
    dao.putInTableSearch(conn, priceSearch)
    println("正在插入.......")
    conn.commit()
    //回滚事务
    conn.rollback();
    MySQLUtil.close(conn)


  }

}
