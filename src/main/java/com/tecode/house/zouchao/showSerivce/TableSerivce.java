package com.tecode.house.zouchao.showSerivce;

import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.List;

public interface TableSerivce {
    /**
     * 带搜索条件的表格请求
     */
    Table getTable(TablePost tablePost);

    /**
     * 不带搜索条件的表格请求
     */
    Table getTable(Integer page,Integer year);
}
