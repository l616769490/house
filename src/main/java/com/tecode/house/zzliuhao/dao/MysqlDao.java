package com.tecode.house.zzliuhao.dao;

import com.tecode.house.zzliuhao.bean.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/12/4.
 */
public interface MysqlDao {
    /**
     * 数据插入report表
     * @param conn
     * @param report 报表
     * @return 报表id
     */
    int putToTableReport(Connection conn , Report report) throws SQLException;

    /**
     * 数据插入图表表
     * @param conn
     * @param diagram 图表表
      * @return 图表ID
     */
    int putToTableDiagram(Connection conn , Diagram diagram) throws SQLException;

    /**
     * 数据插入x表
     * @param conn
     * @param x
     * @return x表ID
     */
    int putToTablexAxis(Connection conn , Xaxis x) throws SQLException;

    int putToTableYaxis(Connection conn ,Yaxis y) throws SQLException;


    /**
     * 插入维度表
     * @param conn
     * @param dimension
     * @return 维度表ID
     */
    int putToTableDimension(Connection conn , Dimension dimension) throws SQLException;

    /**
     * 插入图列表
     * @param conn
     * @param legend
     * @return
     */
    int putToTableLegend(Connection conn , Legend legend) throws SQLException;

    /**
     *操作数据表
     * @param conn
     * @param data
     * @return
     */
    int putToTableData(Connection conn , Data data) throws SQLException;

    /**
     * 操作搜索表
     * @param conn
     * @param search
     * @return
     */
    int putToTablesearch(Connection conn , Search search) throws SQLException;



}
