package com.tecode.house.zouchao.dao

import java.util
import java.util.List

import com.tecode.table.Row

trait HBaseDao {
  /**
    * 查询租金报表的所有数据
    *
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForRent(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]])

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
    * 按建成年份统计价格查询所有数据
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForValue(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]])

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

  /**
    * 按建成年份统计房屋数量查询所有数据
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForRom(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]])

}
