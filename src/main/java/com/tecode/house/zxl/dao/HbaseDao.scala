package com.tecode.house.zxl.dao

trait HbaseDao {

  /**
    * 查询家庭收入
    *
    * @param year 要查询的年份
    * @return 查询结果
    */
  def getIncome(): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 查询家庭收入，根据收入区间查询
    *
    * @param year   要查询的年份
    * @param income 收入区间
    * @return 查询结果
    */
  def getIncome(year: String, income: String): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 查询家庭收入，根据城市等级查询
    *
    * @param year 要查询的年份
    * @param city 要查询的城市等级
    * @return 查询结果
    */
  def getIncome(year: String, city: Int): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 查询家庭收入，根据城市等级和收入区间查询
    *
    * @param year   要查询的年份
    * @param income 要查询的收入区间
    * @param city   要查询的城市等级
    * @return 查询结果
    */
  def getIncome(tableName: String, income: String, city: String,p:Int): List[(Int, (String, Int, Int))]


  /**
    * 获取家庭人数
    *
    * @param year 要查询的年份
    * @return 查询结果
    */
  def getPerson(): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 获取家庭人数,根据人数进行查询
    *
    * @param year   要查询的年份
    * @param person 要查询的人数
    * @return 查询结果
    */
  def getPerson(year: String, person: String): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 获取家庭人数,根据城市等级进行查询
    *
    * @param year 要查询的年份
    * @param city 要查询的城市等级
    * @return 查询结果
    */
  def getPerson(year: String, city: Int): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 获取家庭人数,根据城市等级和家庭人数进行查询
    *
    * @param year   要查询的年份
    * @param city   要查询的城市等级
    * @param person 要查询的年份
    * @return 查询结果
    */
  def getPerson(tableName: String, person: String, city: String,p:Int): List[(Int, (String, Int, Int))]

  /**
    * 查询市场价
    *
    * @param year 要查询的年份
    * @return 查询结果
    */
  def getValue(): Map[String, Iterable[(String, Int, Int)]]

  /**
    * 查询市场价，根据价格区间查询
    *
    * @param year  要查询的年份
    * @param value 要查询的价格区间
    * @return 查询结果
    */
  def getValue(tableName: String, value: String,p:Int): List[(Int, (String, Int, Int))]


  /**
    * 统计市场价的分布情况
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  def getValueDistribution(tableName: String): Map[String, Int]

  /**
    * 统计家庭人数的分布情况
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  def getPersonDistribution(tableName: String): Map[String, Int]

  /**
    * 按照城市统计家庭收入
    *
    * @param tableName 要统计的表
    * @return 统计结果
    */
  def getIncomeDistributionByCity(tableName: String): Map[String, Int]


}
