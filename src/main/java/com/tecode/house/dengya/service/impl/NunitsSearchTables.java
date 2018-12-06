package com.tecode.house.dengya.service.impl;

import com.tecode.house.dengya.bean.Page;
import com.tecode.house.dengya.utils.HbaseUtil;
import com.tecode.table.Table;
import org.apache.hadoop.hbase.TableName;
import scala.actors.threadpool.Arrays;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NunitsSearchTables {

    //过滤搜索条件
    public static void filterData(String tableName) throws IOException {
        List<List<String[]>> lists = HbaseUtil.getAll(tableName);
        List<List<String[]>> searchByUnitslist = SearchByUnits(lists);
        List<List<String[]>> noSearchlist = NoSearch(lists);
        PageLimit.limit(new Page(),searchByUnitslist);
        PageLimit.limit(new Page(),noSearchlist);


    }

    //通过建筑单元数过滤搜索
    public static List<List<String[]>> SearchByUnits(List<List<String[]>> dataLists){
        List<List<String[]>> list = new ArrayList<>();
        List<String[]> datalist = new ArrayList<>();
        List<List<String[]>> resultList = dataLists;
        for (List<String[]> strings : resultList) {
            for (String[] string : strings) {
                int units = Integer.valueOf(string[3]);
                if(units == 1){
                    datalist.add(string);
                }
                if(units > 1 && units <=200){
                    datalist.add(string);
                }
                if(units >200 && units <= 400){
                    datalist.add(string);
                }
                if(units > 400 && units <= 600){
                    datalist.add(string);
                }
                if(units >600){
                    datalist.add(string);
                }
            }
            list.add(datalist);
        }
        return list;
    }

    //没有过滤条件的显示表格数据
    public static List<List<String[]>> NoSearch(List<List<String[]>> dataLists){
        List<List<String[]>> resultList = dataLists;
        return  resultList;
    }



}
