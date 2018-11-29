package com.tecode.house.zouchao.dao

import scala.collection.mutable.ArrayBuffer

trait HBaseScalaDao {
  /**
    * 获取公平市场租金
    *
    * @param tableName 表名
    * @return
    */
  def getAllRent(tableName: String): ArrayBuffer[Int]

  /**
    * 获取建成年份及传入的列的值
    *
    * @param tableName      表名
    * @param qualifiername 列名
    * @return
    */
  def getAllByCreateYear(tableName: String, qualifiername: String): ArrayBuffer[(Int, Int)]


}
