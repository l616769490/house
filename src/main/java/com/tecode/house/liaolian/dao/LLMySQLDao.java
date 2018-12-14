package com.tecode.house.liaolian.dao;

import scala.Tuple2;
import scala.collection.Iterator;

import java.util.Map;


public interface LLMySQLDao {

    boolean into(String name, String reportType, String url, String x, String y, String tpye, Iterator<Tuple2<String, Object>> it, String scrip, String year);

    Map<String, Integer> get(int year);

    Map<String, Integer> getIncome(int year);

    Map<String, Integer> getPerson(int year);
}
