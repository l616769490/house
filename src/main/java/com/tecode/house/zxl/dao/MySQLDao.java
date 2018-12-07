package com.tecode.house.zxl.dao;

import scala.Tuple2;
import scala.collection.Iterator;

import java.util.Map;


public interface MySQLDao {

    boolean into(String name,String reportType,String url,String x, String y, String tpye, Iterator<Tuple2<String, Object>> it,String scrip);

    Map<String, Integer> get();
}
