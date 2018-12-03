package com.tecode.house.lijin.test.db;

import com.tecode.echarts.Option;
import com.tecode.house.d01.service.Analysis;
import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.service.impl.InsertBasicsRoomsNumServer;
import com.tecode.house.lijin.service.impl.SelectBasicsRoomsNumServer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试用
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class TestAnalysis implements Analysis {


    @Override
    public boolean analysis(String tableName) {
        InsertMysqlServer server = new InsertBasicsRoomsNumServer();
        // 房间数
        Map<String, String> roomMap = new LinkedHashMap<>(11);
        roomMap.put("1", "100");
        roomMap.put("2", "200");
        roomMap.put("3", "300");
        roomMap.put("4", "400");
        roomMap.put("5", "500");
        roomMap.put("6", "600");
        roomMap.put("7", "700");
        roomMap.put("8", "800");
        roomMap.put("9", "900");
        roomMap.put("10", "1000");
        roomMap.put("10+", "2000");

        // 卧室数
        Map<String, String> bedrmMap = new LinkedHashMap<>(11);
        bedrmMap.put("1", "10");
        bedrmMap.put("2", "20");
        bedrmMap.put("3", "30");
        bedrmMap.put("4", "40");
        bedrmMap.put("5", "50");
        bedrmMap.put("6", "60");
        bedrmMap.put("7", "70");
        bedrmMap.put("8", "80");
        bedrmMap.put("9", "90");
        bedrmMap.put("10", "101");
        bedrmMap.put("10+", "202");

        Map<String, Map<String, String>> map = new HashMap<>(2);
        map.put("房间数", roomMap);
        map.put("卧室数", bedrmMap);

        server.insert(map, 2013);
        return false;
    }

    public static void main(String[] args) {
        // 插入数据
//        new TestAnalysis().analysis("thads:2013");
        // 读取数据
        SelectBasicsRoomsNumServer select = new SelectBasicsRoomsNumServer();
        Option option = select.select(2013, "基础-房间数分析");
        System.out.println(option);
    }
}
