package com.tecode.house.zouchao.dao;

import com.tecode.house.zouchao.bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface MySQLDao {


    //操作数据表
    public int putInTableData(Connection conn, Data data) throws SQLException;

    public Data getByTableData();

    //操作图表表
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException;

    public Diagram getByTableDiagram();

    //操作维度表
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException;

    public Dimension getByTableDimension();

    //操作数据集表
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException;

    public Legend getByTableLegend();

    //操作报表表
    public int putInTableReport(Connection conn, Report report) throws SQLException;

    public Report getByTableReport(Connection conn, String name);

    //操作搜索表
    public int putInTableSearch(Connection conn, Search search) throws SQLException;

    public Search getByTableSearch();

    //操作x轴表
    public int putInTableXaxis(Connection conn, Xaxis xaxis) throws SQLException;

    public Xaxis getByTableXaxis();

    //操作y轴表
    public int putInTableYaxis(Connection conn, Yaxis yaxis) throws SQLException;

    public Yaxis getByTableYaxis();

    //获取自增id
    public int getId(PreparedStatement ps) throws SQLException;
}
