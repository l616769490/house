package com.tecode.house.zouchao.test

import java.util

import scala.collection.JavaConverters._
object STest extends App {
  val a = List(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)
   val java: util.List[Int] = a.asJava
//  println(java.subList(10,20))
  val s:String = "2013-2015"
   val strings: Array[String] = s.split("-")
  for (elem <- strings) {

    println(elem.substring(0,4))
  }
}
