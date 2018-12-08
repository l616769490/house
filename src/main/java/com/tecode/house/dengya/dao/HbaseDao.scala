package com.tecode.house.dengya.dao
import java.util
trait HbaseDao {
  /**
    * 查询建筑单元数报表的所有数据（没有过滤条件）
    *
    * @param tableName 表名
    * @param page      查询的页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForUnits(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]])

  /**
    * 根据过滤条件查询建筑单元数报表的数据（按建筑单元数过滤）
    *
    * @param tableName 表名
    * @param units    过滤条件
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForUnits(tableName: String, units: String, page: Int): (Int, util.List[util.ArrayList[String]])

  /**
    * 按城市规模统计价格查询所有数据(不过滤条件）
    *
    * @param tableName 表名
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getAllForValue(tableName: String, page: Int): (Int, util.List[util.ArrayList[String]])

  /**
    * 按城市规模统计价格过滤查询数据（三个条件都过滤）
    *
    * @param tableName 表名
    * @param rent     租金区间条件
    * @param city      城市规模条件
    * @param price     售价
    * @param page      查询页码
    * @return (Int,util.List[util.ArrayList[String]])：（符合条件的数据的总条数，查询页码的数据）
    **/
  def getForValue(tableName: String, rent: String,price:String, city: String, page: Int): (Int, util.List[util.ArrayList[String]])

  /**
    * 按建成年份统计价格过滤查询数据(只过滤租金区间)
    *
    * @param tableName 表名
    * @param build     租金区间条件
    * @param page      查询页码
    * @return
    */
  def getForValueByRent(tableName: String, rent: String, page: Int): (Int, util.List[util.ArrayList[String]])

  /**
    * 按建成年份统计价格过滤查询数据(只过滤城市规模)
    *
    * @param tableName 表名
    * @param city     城市规模条件
    * @param page      查询页码
    * @return
    */
  def getForValueByCity(tableName: String, citys: String, page: Int): (Int, util.List[util.ArrayList[String]])
  /**
    * 按建成年份统计价格过滤查询数据(只过滤价格区间规模)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param page      查询页码
    * @return
    */
  def getForValueByPrice(tableName:String,prices:String,page:Int):(Int, util.List[util.ArrayList[String]])

  /**
    * 按建成年份统计价格过滤查询数据(过滤租金和城市规模）
    *
    * @param tableName 表名
    * @param rent     租金区间条件
    * @param city     城市规模
    * @param page      查询页码
    * @return
    */
  def getForValueByCityAndRent(tableName:String,city:String,rent:String,page:Int):(Int, util.List[util.ArrayList[String]])

  /**
    * 按建成年份统计价格过滤查询数据(过滤城市规模和价格)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param city     城市规模
    * @param page      查询页码
    * @return
    */
  def getForValueByCityAndPrice(tableName:String,city:String,price:String,page:Int):(Int, util.List[util.ArrayList[String]])


  /**
    * 按建成年份统计价格过滤查询数据(过滤价格和租金)
    *
    * @param tableName 表名
    * @param price     价格区间条件
    * @param rent     租金条件
    * @param page      查询页码
    * @return
    */
  def getForValueByRentAndPrice(tableName:String,rent:String,price:String,page:Int):(Int, util.List[util.ArrayList[String]])









}
