package com.tecode.house.dengya.service.impl;

import com.tecode.house.dengya.utils.HbaseUtil;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NunitsSearchTable {
    //过滤无效数据
    public static List<String> filterData(String tableName) throws IOException {
        List<String> list = new ArrayList<>();
        List<String> lists = HbaseUtil.getAll(tableName);
        for (String s : lists) {
            //INCRELPOVCAT_1_5_732992580648
            String[] split = s.split("_");


        }

        return  list;
    }

    public static void main(String[] args) {
        try {
            filterData("thads:2013");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
