package com.tecode.house.zouchao.dao.DaoImpl

import java.io.IOException

import com.tecode.house.zouchao.dao.HBaseScalaDao
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Result, ResultScanner, Scan}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ArrayBuffer

class HBaseScalaDaoImpl extends HBaseScalaDao {
  /**
    * 获取公平市场租金
    *
    * @param tableName 表名
    * @return
    * @throws IOException
    */
  override def getAllRent(tableName: String): ArrayBuffer[Integer] = {
    val rents = ArrayBuffer[Integer]()
    val conf = HBaseConfiguration.create()
    val conn = ConnectionFactory.createConnection(conf)
    val table = conn.getTable(TableName.valueOf(tableName))
    //添加过滤器，只获取“info”列族下的公平市场租金列
    val scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"))
    val scanner: ResultScanner = table.getScanner(scan)
    for (result: Result <- scanner) {
      val cells: Array[Cell] = result.rawCells()
      for (elem <- cells) {
        //将结果添加到集合中
        rents += Integer.valueOf(CellUtil.cloneValue(elem) toString)
      }
    }
    rents
  }

  override def getAllByCreateYear(tableName: String, qualifiername: String): ArrayBuffer[(Int, Int)] = {
    val list = ArrayBuffer[(Int, Int)]()
    val conf = HBaseConfiguration.create()
    val conn = ConnectionFactory.createConnection(conf)
    val table = conn.getTable(TableName.valueOf(tableName))
    //添加过滤器，只获取“info”列族下的建成年份、及传入的列
    val scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes(qualifiername))
      .addColumn(Bytes.toBytes("info"), Bytes.toBytes("BUILT"))
    val scanner: ResultScanner = table.getScanner(scan)
    for (result: Result <- scanner) {
      val cells: Array[Cell] = result.rawCells()
      var year: Int = 0
      var value = 0
      for (elem <- cells) {
        val name = CellUtil.cloneQualifier(elem).toString
        name match {
          case qualifiername => value = Integer.valueOf(CellUtil.cloneValue(elem) toString).toInt
          case "BUILT" => year = Integer.valueOf(CellUtil.cloneValue(elem) toString).toInt
        }
      }
      val tuple: (Int, Int) = (year, value)
      list += tuple
    }
    list
  }
}
