package com.tecode.house.lijun.sSerivce;

import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.List;

public interface TSerivce {
    /**
     * 带搜索条件的表格请求(房屋费用)
     */
    Table getTableForCost(TablePost tablePost);


    /**
     * 带搜索条件的表格请求（按建成年份统计价格）
     * @param
     * @return
     */

    Table getTablePrice(TablePost tablePost);

    /**
     * 获取数据（按建成年份统计房屋数量）
     * @param tablePost
     * @return
     */
    Table getTableForIncome(TablePost tablePost);


    List<Search> getSearch(Integer year, String name, String group);


}
