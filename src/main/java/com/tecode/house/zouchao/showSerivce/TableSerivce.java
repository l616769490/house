package com.tecode.house.zouchao.showSerivce;

import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.List;

public interface TableSerivce {
    /**
     * 带搜索条件的表格请求(租金)
     */
    Table getTableForRent(TablePost tablePost);


    /**
     * 带搜索条件的表格请求（按建成年份统计价格）
     *
     * @param tablePost
     * @return
     */
    Table getTableForPrice(TablePost tablePost);



    /**
     * 获取数据（按建成年份统计房屋数量）
     *
     * @param tablePost
     * @return
     */
    Table getTableForRoom(TablePost tablePost);

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
