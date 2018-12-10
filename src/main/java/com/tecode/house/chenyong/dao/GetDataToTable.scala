package com.tecode.house.chenyong.dao
import java.util
/**
  * Created by Administrator on 2018/12/7.
  */
abstract class GetDataToTable {

  /**
    *
    * @param tableName 表名
    * @param ageInterval  查询的年龄区间
    * @param page  查询页数
    * @return  查询的总数，查询的数据
    */
  //从HBase获得数据到以年龄查询的年龄分布的表格中
  def getDataToAge(tableName:String,ageInterval:String,page:Int):(Int,util.List[util.ArrayList[String]])

  /**
    *
    * @param tableName  表名
    * @param ageInterval  查询的年龄区间
    * @param page  查询页数
    * @return  查询的总数，查询的数据
    */
  //从HBase获取数据到以年龄查询的房间分布的表格中
  def getDataToRooms(tableName:String,ageInterval:String,page:Int):(Int,util.List[util.ArrayList[String]])

  /**
    *
    * @param tableName  表名
    * @param ageInterval  查询的年龄区间
    * @param page  查询页数
    * @return  查询的总数，查询的数据
    */
  //从HBase获取原始数据到以年龄查询的水电费分布的表格中
  def getDataToUtility(tableName:String,ageInterval:String,page:Int):(Int,util.List[util.ArrayList[String]])

}
