package com.tecode.house.liuhao.dao;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public interface ReadMyqslDao {
    List ReadTableData(Connection conn);
}
