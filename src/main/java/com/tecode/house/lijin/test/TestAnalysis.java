package com.tecode.house.lijin.test;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.lijin.service.MysqlServer;
import com.tecode.house.lijin.service.impl.BasicsRoomsNumServer;

import javax.annotation.Resource;

/**
 * 测试用
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public class TestAnalysis implements Analysis {
    @Resource(name = "BasicsRoomsNumServer")
    MysqlServer server;
    @Override
    public boolean analysis(String tableName) {
//        MysqlServer server = new BasicsRoomsNumServer();
        server.insert(null);
        return false;
    }
}
