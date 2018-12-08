package com.tecode.house.liuhao.dao;

import com.tecode.house.liuhao.bean.City;
import scala.Int;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public interface ReadMyqslDao {
    List ReadTableData(Connection conn, String where);
    City ReadCityTax(Connection conn );


}
