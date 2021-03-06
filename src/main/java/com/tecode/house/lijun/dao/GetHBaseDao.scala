package com.tecode.house.lijun.dao

import java.util

trait GetHBaseDao {
  /**
    * 查询租金报表的所有数据
    *
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForCOST(tableName: String, filter: String ,page: Int): (Long, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计价格查询所有数据
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String])：（符合条件的数据的总条数，查询页码的数据）
    **/

  def getPrice(tableName: String, age:String, value:String, page: Int): (Long, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计房屋数量过滤查询数据(只过滤城市规模)
    *
    * @param tableName 表名
    * @param page      查询页码
    */
  def getForIncome(tableName: String, ages:String,incomes:String,page: Int): (Long, util.List[util.ArrayList[String]])


}
