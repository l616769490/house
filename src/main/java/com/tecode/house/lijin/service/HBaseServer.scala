package com.tecode.house.lijin.service

import java.io.File

import com.tecode.house.lijin.filter.{FilterBean, FilterFactory}
import com.tecode.house.lijin.utils.ConfigUtil
import com.tecode.table._
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.filter.PageFilter
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableMapReduceUtil}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.dom4j.Element
import org.dom4j.io.SAXReader

import scala.collection.mutable.ArrayBuffer

/**
  * 从HBase中读取详细数据
  * 版本：2018/12/5 V1.0
  * 成员：李晋
  */
class HBaseServer(path: String, reportName: String, tablePost: TablePost) {

  import scala.collection.JavaConverters._

  private val conf: SparkConf = new SparkConf().setAppName("HBaseServer").setMaster(ConfigUtil.get("spark_master"))
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
    val searches = tablePost.getSearches

    // 读取字段
    val fields = loadFields()

    // 获得HBaseRDD
    val hBaseRDD = readHBase(fields)

    // 获取搜索条件
    val filterBean: FilterBean = loadFilter()

    // 获取过滤器
    val filter = FilterFactory.getFilter(filterBean.getType)

    // 过滤数据
    val rdd = hBaseRDD.filter(row => filter.filter(row._2, filterBean, searches))

    getTable(rdd, fields)
  }

  /**
    * 获取Table
    *
    * @param rdd    清洗后的数据流
    * @param fields Table显示的字段值
    * @return
    */
  def getTable(rdd: RDD[(ImmutableBytesWritable, Result)], fields: Array[(String, String)]): Table = {

    val thisPage = tablePost.getPage
    val year = tablePost.getYear

    val table = new Table()
    // 设置年
    table.setYear(year)
    // 设置表头
    fields.foreach(t => table.addTop(t._2))

    // 页码
    val page = new Page().setThisPage(thisPage)
    // 当前页码前面加两页
    for (i <- 1 to 2) {
      if (thisPage - i > 0) {
        page.addData(thisPage - i)
      }
    }
    page.addData(thisPage)
    // 页码数加够5
    for(i <- page.getData.size() to 5) {
      page.addData(thisPage + i)
    }
    table.setPage(page)



    return new Table().setYear(2011)
      .setPage(new Page().setThisPage(5).addData(2).addData(3).addData(4).addData(5).addData(6))
      .addTop("数据1").addTop("数据2")
      .addSearchs(new Search().setTitle("按地区").addValue("东部地区").addValue("西部地区"))
      .addData(new Row().addRow("asa").addRow("sds")).addData(new Row().addRow("123").addRow("232"))
      .addData(new Row().addRow("dfd").addRow("234"))
  }

  /**
    * 从配置文件中读取搜索条件
    *
    * @return FilterBean
    */
  def loadFilter(): FilterBean = {
    val filterBean = new FilterBean()
    val search = document.getRootElement.element("search")

    val searchType = search.element("type").getText
    filterBean.setType(searchType)

    val fieldElement = search.element("field")
    if (fieldElement != null) {
      val field = fieldElement.getText
      filterBean.setField(field)

      val searchElement = search.element("items")
      if (searchElement != null) {
        searchElement.elements("item").asScala.foreach {
          case item: Element => {
            val nameAttribute = item.attribute("name")
            if (nameAttribute != null) {
              val name = nameAttribute.getText
              val minElement = item.element("min")
              val maxElement = item.element("max")
              var min = ""
              if (minElement != null) {
                min = minElement.getText
              }
              var max = ""
              if (maxElement != null) {
                max = maxElement.getText
              }
              filterBean.addItem(name, min, max)
            }
          }
        }
      }
    }
    filterBean
  }

  /**
    * 从redis上读取距离当前页面最近的前面页面的最后一个rowkey
    *
    * @return 前面页面的页码和rowkey，页码在前
    */
  def getPreviousPage: (String, String) = {
    ("0", "0")
  }

  /**
    * 获取分页过滤器
    *
    * @param previousPage 前面页的信息（页码， 最后一项的rowKey）
    * @return 分页过滤器
    */
  def pageFilter(previousPage: (String, String)): PageFilter = {

    null
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

    // 获取缓存的前面一页的数据
    val previousPage = getPreviousPage
    // 设置分页过滤器
    val pageFilter = pageFilter(previousPage)
    scan.setFilter(pageFilter)

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
    * @return 字段信息
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

object HBaseServer {
  def main(args: Array[String]): Unit = {
    val tablePost = new TablePost
    tablePost.setYear(2013)
    tablePost.setPage(5)
    new HBaseServer(HBaseServer.getClass.getResource("/table/region-zinx2.xml").getPath, "按区域-家庭收入分析", tablePost).select()
  }
}