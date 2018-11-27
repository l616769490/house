package com.tecode.house.lijun.test;

import com.tecode.table.TablePost;
import com.tecode.table.Table;

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
