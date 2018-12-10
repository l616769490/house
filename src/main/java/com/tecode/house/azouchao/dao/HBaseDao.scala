package com.tecode.house.azouchao.dao

import java.util

trait HBaseDao {

  /**
    * 根据过滤条件查询租金报表的数据
    *
    * @param tableName 表名
    * @param filter    过滤条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForRent(tableName: String, filter: String, page: Int): (Int, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计价格过滤查询数据
    *
    * @param tableName 表名
    * @param build     建成年份区间条件
    * @param city      城市规模条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForValue(tableName: String, build: String, city: String, page: Int): (Int, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计房屋数量过滤查询数据
    *
    * @param tableName 表名
    * @param build     建成年份区间条件
    * @param city      城市规模条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForRom(tableName: String, build: String, city: String, page: Int): (Int, util.List[util.ArrayList[String]])


}
