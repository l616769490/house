package com.tecode.house.liaolian.test;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface TestService {
    /**
     * 带搜索条件的表格请求
     */
    Table getTable(TablePost tablePost);

    /**
     * 不带搜索条件的表格请求
     */
    Table getTable();
}
