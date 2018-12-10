package com.tecode.house.jianchenfei.test

import com.tecode.house.jianchenfei.service.serviceImpl.{BasicAnalysis, Get_Single_Region, Get_Zsmhc_built}

/**
  * Created by Administrator on 2018/12/10.
  */
object TestAns {
  def main(args: Array[String]): Unit = {
    val b = new BasicAnalysis()
    val s = new Get_Single_Region
    val g = new Get_Zsmhc_built
    s.analysis("thads:2013")
    b.analysis("thads:2013")
    g.analysis("thads:2013")
  }

}
