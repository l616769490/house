package com.tecode.house.lijun.sSerivce;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

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

    Table getTablePrice(Integer page, Integer year);

    /**
     * 获取数据（按建成年份统计房屋数量）
     * @param tablePost
     * @return
     */
    Table getTableForIncome(TablePost tablePost);


}
