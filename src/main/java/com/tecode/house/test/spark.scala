package com.tecode.house.test

import com.tecode.house.dengya.service.impl.{NunitsSpark, PriceByCity}

object spark {
  def main(args: Array[String]): Unit = {
    val spark = new NunitsSpark
  //  spark.analysis("thads:2013")
    val city = new PriceByCity
  city.analysis("thads:2013")
  }
}
