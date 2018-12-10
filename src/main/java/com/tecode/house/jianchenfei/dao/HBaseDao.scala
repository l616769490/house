package com.tecode.house.jianchenfei.dao

import java.util

trait HBaseDao {

  /**
    * 根据过滤条件查询家庭人数报表的数据
    *
    * @param tableName 表名
    * @param filter    过滤条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getPer(tableName: String, filter: String, page: Int): (Int, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计房产税过滤查询数据
    *
    * @param tableName 表名
    * @param build     建成年份区间条件
    * @param rate      城市规模条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getRate(tableName: String, build: String, rate: String, page: Int): (Int, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计房屋数量过滤查询数据
    *
    * @param tableName 表名
    * @param region    地区条件
    * @param single      独栋比例条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getSingle(tableName: String, region: String, single: String, page: Int): (Int, util.List[util.ArrayList[String]])


}
