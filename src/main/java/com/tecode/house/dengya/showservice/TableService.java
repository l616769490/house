package com.tecode.house.dengya.showservice;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface TableService {
    /**
     * 带搜索条件的表格请求（建筑单元数）
     * @param tablePost
     * @return
     */
    Table getTableForUnits(TablePost tablePost);

    /**
     * 不带搜索条件的表格请求（建筑单元数）
     */
    Table getTableForUnits(Integer page,Integer year);
    /**
     * 带搜索条件的表格请求（按城市规模统计价格）
     * @param tablePost
     * @return
     */
    Table getTableForPrice(TablePost tablePost);

    /**
     * 带搜索条件的表格请求（按城市规模统计价格）
     * @param
     * @return
     */
    Table getTableForPrice(Integer page,Integer year);


}
