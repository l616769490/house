package com.tecode.house.liuhao.dao

import java.util

/**
  * Created by Administrator on 2018/12/7.
  */
trait ReadHbaseDao {
  /**
    * 无条件查询
    * @param tablename 表名
    * @param page 查询页数
    * @param arr  查看的列的集合
    * @return
    */
  def readData(tablename:String,page:Int,,arr:String):(Integer,util.List[util.ArrayList[String]])


  /**
    * 按各线城市划分查询
    * @param tablename 表名
    * @param page 查看的页数
    * @param city 城市等级
    * @return
    */
  def readCityTaxData(tablename:String,page:Int,city:String):(Integer,util.List[util.ArrayList[String]])
}
