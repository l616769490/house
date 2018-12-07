package com.tecode.house.zouchao.dao;

import com.tecode.house.zouchao.bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface MySQLDao {


    //操作数据表
    public int putInTableData(Connection conn, Data data) throws SQLException;

    public List<Data> getByTableData(Connection conn, int legendId,int xId) throws SQLException;

    //操作图表表
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException;

    public List<Diagram> getByTableDiagram(Connection conn, int reportId) throws SQLException;

    //操作维度表
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException;

    public List<String> getByTableDimension(Connection conn,String groupName) throws SQLException;

    //操作数据集表
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException;

    public List<Legend> getByTableLegend(Connection conn, int diagramId) throws SQLException;

    //操作报表表
    public int putInTableReport(Connection conn, Report report) throws SQLException;

    public Report getByTableReport(Connection conn, String name,int year) throws SQLException;

    //操作搜索表
    public int putInTableSearch(Connection conn, Search search) throws SQLException;

    public List<Search> getByTableSearch(Connection conn,int reportId) throws SQLException;

    //操作x轴表
    public int putInTableXaxis(Connection conn, Xaxis xaxis) throws SQLException;

    public Xaxis getByTableXaxis(Connection conn, int diagramId) throws SQLException;

    //操作y轴表
    public int putInTableYaxis(Connection conn, Yaxis yaxis) throws SQLException;

    public Yaxis getByTableYaxis(Connection conn, int diagramId) throws SQLException;

    //获取自增id
    public int getId(PreparedStatement ps) throws SQLException;
}
