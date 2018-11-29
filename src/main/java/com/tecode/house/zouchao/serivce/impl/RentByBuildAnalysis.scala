package com.tecode.house.zouchao.serivce.impl

import com.tecode.house.d01.service.Analysis

class RentByBuildAnalysis extends Analysis {
  /**
    * 数据分析接口
    *
    * @param tableName HBase数据库名字
    * @return 成功/失败
    */
  override def analysis(tableName: String): Boolean = {
    true
  }
}
