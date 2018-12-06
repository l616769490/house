package com.tecode.house.lijin.service;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

/**
 * 获取Table
 * 版本：2018/12/6 V1.0
 * 成员：李晋
 */
public interface TableServer {
    /**
     * 获取表格
     *
     * @param reportName 报表名
     * @param tablePost  搜索条件
     * @param path       表格配置文件路径
     * @return 表格原始数据
     */
    Table getTable(String reportName, TablePost tablePost, String path);
}
