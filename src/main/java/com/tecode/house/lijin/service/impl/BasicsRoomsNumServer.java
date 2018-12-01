package com.tecode.house.lijin.service.impl;

import com.tecode.echarts.Option;
import com.tecode.house.lijin.service.MysqlServer;
import com.tecode.mysql.dao.DimensionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 基础-房间数统计-房间数
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */

public class BasicsRoomsNumServer implements MysqlServer {

    @Override
    public void insert(Map<String, String> datas) {

    }

    @Override
    public Option select(String reportName) {
        return null;
    }
}
