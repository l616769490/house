package com.tecode.house.zouchao.showSerivce;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface TableSerivce {
    /**
     * 带搜索条件的表格请求(租金)
     */
    Table getTableForRent(TablePost tablePost);

    /**
     * 不带搜索条件的表格请求（租金）
     */
    Table getTableForRent(Integer page,Integer year);

    /**
     * 带搜索条件的表格请求（按建成年份统计价格）
     * @param tablePost
     * @return
     */
    Table getTableForPrice(TablePost tablePost);

    /**
     * 不带搜索条件的表格请求（按建成年份统计价格）
     * @param page  页码
     * @param year  年份
     * @return
     */
    Table getTableForPrice(Integer page,Integer year);

    /**
     * 获取数据（按建成年份统计房屋数量）
     * @param tablePost
     * @return
     */
    Table getTableForRoom(TablePost tablePost);


}
