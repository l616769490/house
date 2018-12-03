package com.tecode.house.chenyong.dao;

import com.tecode.house.chenyong.bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface MySQLDao {

    //操作数据表
    public int putInTableData(Connection conn, Data data) throws SQLException;

    public void getByTableData();

    //操作图表表
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException;

    public void getByTableDiagram();

    //操作维度表
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException;

    public void getByTableDimension();

    //操作数据集表
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException;

    public void getByTableLegend();

    //操作报表表
    public int putInTableReport(Connection conn, Report report) throws SQLException;

    public void getByTableReport();

    //操作搜索表
    public int putInTableSearch(Connection conn, Search search) throws SQLException;

    public void getByTableSearch();

    //操作x轴表
    public int putInTableXaxis(Connection conn, XAxis xAxis) throws SQLException;

    public void getByTableXaxis();

    //操作y轴表
    public int putInTableYaxis(Connection conn, YAxis yAxis) throws SQLException;

    public void getByTableYaxis();

    //获取自增id
    public int getId(PreparedStatement ps) throws SQLException;
}
