package com.tecode.house.zouchao.dao.impl

import com.tecode.house.zouchao.dao.HBaseScalaDao
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ArrayBuffer

class HBaseScalaDaoImpl extends HBaseScalaDao {
  /**
    * 获取公平市场租金
    *
    * @param tableName 表名
    * @return
    */
  override def getAllRent(tableName: String): ArrayBuffer[Int] = {
    val rents = new ArrayBuffer[Int]()
//    //获得配置文件的对象
//    val conf = HBaseConfiguration.create
//    //新的API
//    val conn = ConnectionFactory.createConnection(conf)
//    val table = conn.getTable(TableName.valueOf(tableName))
//    //    添加过滤器，获取info列族下的FMR列
//    val scan: Scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("FMR"))
//    val resultScanner: ResultScanner = table.getScanner(scan)
//    for (result: Result <- resultScanner) {
//      val cells: Array[Cell] = result.rawCells()
//      for (elem <- cells) {
//        //        将结果添加到数组中
//        rents.+=(Integer.valueOf(Bytes.toString(CellUtil.cloneValue(elem))))
//      }
//    }
    rents
  }

  /**
    * 获取建成年份及传入的列的值
    *
    * @param tableName     表名
    * @param qualifiername 列名
    * @return
    */
  override def getAllByCreateYear(tableName: String, qualifiername: String): ArrayBuffer[(Int, Int)] = {
    val results = new ArrayBuffer[(Int, Int)]()
//    val conf = HBaseConfiguration.create
//    //新的API
//    val conn = ConnectionFactory.createConnection(conf)
//    val table = conn.getTable(TableName.valueOf(tableName))
//    //    添加过滤器，获取info列族下的FMR列
//    val scan: Scan = new Scan().addColumn(Bytes.toBytes("info"), Bytes.toBytes("BUILT"))
//      .addColumn(Bytes.toBytes("info"), Bytes.toBytes(qualifiername))
//    val resultScanner: ResultScanner = table.getScanner(scan)
//    for (result: Result <- resultScanner) {
//      var year: Int = 0
//      var value: Int = -1
//      val cells: Array[Cell] = result.rawCells()
//      for (elem <- cells) {
//        //        将结果添加到数组中
//        val name = CellUtil.cloneQualifier(elem).toString
//        name match {
//          case "BUILT" => year = Integer.valueOf(Bytes.toString(CellUtil.cloneValue(elem)))
//          case qualifiername => value = Integer.valueOf(Bytes.toString(CellUtil.cloneValue(elem)))
//        }
//      }
//      results += ((year, value))
//    }
    results
  }
}
