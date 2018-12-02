package com.tecode.house.lijin.test.db;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.lijin.service.InsertMysqlServer;
import com.tecode.house.lijin.service.impl.BasicsRoomsNumServer;

/**
 * 测试用
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class TestAnalysis implements Analysis {


    @Override
    public boolean analysis(String tableName) {
        InsertMysqlServer server = new BasicsRoomsNumServer();
        server.insert(null,2013);
        return false;
    }

    public static void main(String[] args) {
        new TestAnalysis().analysis("");
    }
}
