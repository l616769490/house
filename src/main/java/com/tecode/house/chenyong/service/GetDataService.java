package com.tecode.house.chenyong.service;

import com.tecode.house.chenyong.dao.*;
import com.tecode.house.chenyong.dao.impl.GetDataToTableImpl;
import com.tecode.house.chenyong.dao.impl.ImportIntoDataImpl;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/8.
 */

public class GetDataService {

    AgeAnalysis ageAnalysis = new AgeAnalysis();
    RoomsAnalysisByAge roomsAnalysisByAge = new RoomsAnalysisByAge();
    UtilityAnalysisByAge utilityAnalysisByAge = new UtilityAnalysisByAge();
    GetDataToTable getDataToTable = new GetDataToTableImpl();
    ImportIntoData intoHBase = new ImportIntoDataImpl();

    //创建HBase表
    public void createHBaseTable(String tableName){
        try {
            intoHBase.createTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //从HBase读取数据
    public void readDataFromHBase(String tableName){
        intoHBase.readData(tableName);
    }


    //获取分析年龄分布的方法
    public boolean getFromAgeAnalysis(String tableName) {
        return ageAnalysis.analysis(tableName);
    }

    //获取通过年龄分析得到房间数和卧室数的方法
    public boolean getFromRoomsAnalysisByAge(String tableName) {
        return roomsAnalysisByAge.analysis(tableName);
    }

    //获取通过年龄分析得到水电费的方法
    public boolean getFromUtilityAnalysisByAge(String tableName) {
        return utilityAnalysisByAge.analysis(tableName);
    }

    //通过年龄分析相关得到HBase表中年龄分布相关原始数据的方法
    public Tuple2<Object, List<ArrayList<String>>> getFromHbaseToTableAge(String tableName,String ageInterval,int page) {
        return getDataToTable.getDataToAge(tableName, ageInterval, page);

    }
    //通过年龄分析相关得到HBase表中房间卧室相关原始数据的方法
    public Tuple2<Object, List<ArrayList<String>>> getFromHbaseToTableRooms(String tableName,String ageInterval,int page) {
        return getDataToTable.getDataToRooms(tableName, ageInterval, page);

    }
    //通过年龄分析相关得到HBase表中水电费相关原始数据的方法
    public Tuple2<Object, List<ArrayList<String>>> getFromHbaseToTableUtility(String tableName,String ageInterval,int page){
        return getDataToTable.getDataToUtility(tableName, ageInterval, page);

    }

}
