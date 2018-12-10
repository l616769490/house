package com.tecode.house.dengya.dao
import java.util
trait HbaseDao {
  /**
    * 查询建筑单元数报表的所有数据
    *
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForUnits(tableName: String,filter:String, page: Int): (Int, util.List[util.ArrayList[String]])


  /**
    * 按城市规模统计价格过滤查询数据
    *
    * @param tableName 表名
    * @param rent     租金区间条件
    * @param city      城市规模条件
    * @param price     售价
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForValue(tableName: String, rent: String,price:String, city: String, page: Int): (Int, util.List[util.ArrayList[String]])









}
