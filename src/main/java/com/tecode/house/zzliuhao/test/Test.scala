package com.tecode.house.zzliuhao.test

import java.util

import com.tecode.house.zzliuhao.control.PackageTable
import com.tecode.house.zzliuhao.dao.impl.ReadHbaseDaoImpl
import com.tecode.house.zzliuhao.server.Tax
import com.tecode.table.{Search, TablePost}
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Administrator on 2018/12/6.
  */


object Tax1 {
  def main(args: Array[String]): Unit = {
   /*   val impl = new ReadHbaseDaoImpl
    impl.readData("2013",6,"STRUCTURETYPE")*/

   /* val tax = new Tax()
    println("读取税务相关数据...")
    val read: RDD[(Int, Double, Double, Double)] = tax.readTax("2013")
      read.collect().foreach(println)
    println("读取数据完成，开始分析税务相关的数据...")
    val analy: ArrayBuffer[(String, Int)] = tax.analyTax(read)
    println("分析完成，开始插入...")
    tax.packageTaxBean("2013", analy)
    println("插入完成！完成阶段一")*/


    /*println("开始读取建筑结构类型数据...")
  val stru = new StructureType()
  val read2 = stru.read("2013", "STRUCTURETYPE")
  println("数据读取完成，开始分析...")
  val analy2 = stru.analyStructureType(read2)

  println("分析完成，开始导入建筑结构类型的分析结果...")
  stru.packageBean("2013", analy2)
  println("导入完成，完成阶段二")*/
  }
}





