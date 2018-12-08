package com.tecode.house.jianchenfei.service

import java.io.File


import com.tecode.table._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.dom4j.Element
import org.dom4j.io.SAXReader

import scala.collection.mutable.ArrayBuffer

/**
  * 从HBase中读取详细数据
  *
  * @param path       配置文件地址
  * @param reportName 报表名
  * @param tablePost  前端传过来的搜索条件
  */
class HBaseServer(path: String, reportName: String, tablePost: TablePost) {

  import scala.collection.JavaConverters._

  private val pageNum = 5
  private val conf: SparkConf = new SparkConf().setAppName("HBaseServer" + reportName).setMaster("local[*]")
  private val sc = new SparkContext(conf)
  private val hBaseConf: Configuration = HBaseConfiguration.create()
  private val saxReader = new SAXReader
  private val document = saxReader.read(new File(path))

  /**
    * 查询HBase
    *
    * @return 查询结果
    */
  def select(): Table = {
    // 读取字段
    val fields = loadFields()

    // 获得HBaseRDD
    val hBaseRDD = readHBase(fields)

    // 获取需要的数据
    getTable(hBaseRDD, fields)
  }

  /**
    * 获取Table
    *
    * @param hBaseRDD 清洗后的数据流
    * @param fields   Table显示的字段值
    * @return Table
    */
  def getTable(hBaseRDD: RDD[(ImmutableBytesWritable, Result)], fields: Array[(String, String)]): Table = {

    val thisPage = tablePost.getPage
    val year = tablePost.getYear
    val fieldsNum = fields.length

    val table = new Table()
    // 设置年
    table.setYear(year)

    // 用于临时存储字段顺序
    var filedMap = Map[String, Int]()
    // 设置表头
    for (i <- 0 until fieldsNum) {
      // 设置表头
      table.addTop(fields(i)._2)
      // 保存表头的顺序
      filedMap += ((fields(i)._1, i))
    }

    // 页码
    val page = new Page().setThisPage(thisPage)
    // 当前页码前面加两页
    for (i <- -2 to -1) {
      if (thisPage + i > 0) {
        page.addData(thisPage + i)
      }
    }
    // 页码数加够5
    for (i <- 0 to (4 - page.getData.size())) {
      page.addData(thisPage + i)
    }
    table.setPage(page)

    // 计算当前页最后一条数据是第几条
    val thisPageLastNum = pageNum * thisPage

    // 缓存页码最后一页
    val allPageLastNum = pageNum * page.getData.get(page.getData.size() - 1)

    val list = hBaseRDD.map(row => {
      var map = Map[String, String]()
      for (cell <- row._2.rawCells()) {
        // 列名
        val qualifier = Bytes.toString(CellUtil.cloneQualifier(cell))
        // 值
        val value = Bytes.toString(CellUtil.cloneValue(cell))
        map += ((qualifier, value))
      }
      map
    }).take(allPageLastNum)
      .map(m => {
        val arr = new ArrayBuffer[String]
        for (i <- 0 until fieldsNum) {
          arr += m(fields(i)._1)
        }
        arr.toList.asJava
      }).toList

    // 插入数据到table
    for (i <- 1 to pageNum) {
      val n = thisPageLastNum - i
      if (n < list.size) {
        val row = new Row()
        row.setRow(list(n))
        table.addData(row)
      }
    }
    table
  }

  /**
    * 读取HBase数据
    *
    * @param fields 需要哪些字段
    * @return HBaseRDD
    */
  def readHBase(fields: Array[(String, String)]): RDD[(ImmutableBytesWritable, Result)] = {
    val year = tablePost.getYear
    val tableName = "thads:" + year

    hBaseConf.set(TableInputFormat.INPUT_TABLE, tableName)
    val scan = new Scan()
    scan.setCacheBlocks(false)

    // 设置读取的列族
    scan.addFamily(Bytes.toBytes("info"))
    for (field <- fields) {
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes(field._1))
    }
    // 将scan类转化成string类型
    val scan_str = TableMapReduceUtil.convertScanToString(scan)
    hBaseConf.set(TableInputFormat.SCAN, scan_str)
    sc.newAPIHadoopRDD(hBaseConf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
  }

  /**
    * 从配置文件中获取字段信息
    *
    * @return 字段信息(列名， 别名)
    */
  def loadFields(): Array[(String, String)] = {
    // 获取根节点
    val rootElement = document.getRootElement
    // 获取所有的title
    val titles = rootElement.element("titles").elements("title").asScala
    val array = ArrayBuffer[(String, String)]()
    titles.foreach {
      case title: Element => {
        val field = title.attribute("field").getText
        if (field == null) {
          throw new NullPointerException("field 字段不能为空！")
        }
        val alias = title.attribute("alias").getText
        if (alias == null) {
          array += ((field, field))
        } else {
          array += ((field, alias))
        }
      }
    }
    array.toArray
  }
}