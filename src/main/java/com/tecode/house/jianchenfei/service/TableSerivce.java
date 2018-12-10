package com.tecode.house.jianchenfei.service;

import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.List;

public interface TableSerivce {
    /**
     * 带搜索条件的表格请求(家庭人数)
     */
    Table getTablePer(TablePost tablePost);


    /**
     * 带搜索条件的表格请求（按建成年份统计房产税）
     *
     * @param tablePost
     * @return
     */
    Table getTableRate(TablePost tablePost);



    /**
     * 获取数据（按区域统计独栋比例）
     *
     * @param tablePost
     * @return
     */
    Table getTableSingle(TablePost tablePost);

    /**
     * 获取搜索条件列表
     *
     * @param year 年份
     * @param name 报表名
     * @return 搜索条件列表
     */
    /**
     * 获取搜索条件列表
     *
     * @param year  年份
     * @param name  报表名
     * @param group 报表组
     * @return 搜索条件列表
     */
    List<Search> getSearch(Integer year, String name, String group);

}
