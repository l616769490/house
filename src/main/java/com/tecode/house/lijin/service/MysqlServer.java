package com.tecode.house.lijin.service;

import com.tecode.echarts.Option;
import com.tecode.table.Table;

import java.util.Map;

/**
 * 操作数据库
 * 版本：2018/12/1 V1.0
 * 成员：李晋
 */
public interface MysqlServer {
    /**
     * 将数据写入到数据库中
     *
     * @param datas 数据集
     */
    void insert(Map<String, String> datas);

    /**
     * 查询报表信息
     *
     * @param reportName 报表名
     * @return 查询结果集
     */
    Option select(String reportName);
}
