package com.tecode.house.liaolian.dao

trait LLHbaseDao {

  def getIncome(tableName: String, city: Int,page:Int): List[(Int, (String, Int, Int))]


  def getIncome(tableName: String, income: String,page:Int): List[(Int, (String, Int, Int))]


  def getIncome(tablName: String, page: Int):  List[(String, (String, Int, Int))]


  def getIncome(tableName: String, income: String, city: String,p:Int): List[(Int, (String, Int, Int))]


  def getPerson(tableName: String,city:Int, page: Int): List[(Int, (String, Int, Int))]

  def getPerson(tableName: String, person: String,page:Int): List[(Int, (String, Int, Int))]


  def getPerson(tableName: String, page: Int): List[(String, (String, Int, Int))]

  def getPerson(tableName: String, person: String, city: String,p:Int): List[(Int, (String, Int, Int))]


  def getValue(tableName:String,page:Int):List[(String, (String, Int, Int))]


  def getValue(tableName: String, value: String,p:Int): List[(Int, (String, Int, Int))]


  def getValueDistribution(tableName: String): Map[String, Int]


  def getPersonDistribution(tableName: String): Map[String, Int]


  def getIncomeDistributionByCity(tableName: String): Map[String, Int]


}
