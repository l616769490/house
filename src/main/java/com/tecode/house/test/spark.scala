package com.tecode.house.test

import com.tecode.house.dengya.service.impl.{NunitsSpark, PriceByCity}


object spark {
  def main(args: Array[String]): Unit = {
    val spark = new PriceByCity
    spark.analysis("thads:2013")






  }
}
