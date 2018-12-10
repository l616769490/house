package com.tecode.house.azouchao.dao;

import com.tecode.house.azouchao.bean.*;

import java.sql.SQLException;
import java.util.List;

public interface MyBatisMySQLDao {


    //操作数据表
    public int putInTableData(Data data) throws SQLException;

    public List<Data> getByTableData( int legendId, int xId) throws SQLException;

    //操作图表表
    public int putInTableDiagram( Diagram diagram) throws SQLException;

    public List<Diagram> getByTableDiagram( int reportId) throws SQLException;

    //操作维度表
    public int putInTableDimension( Dimension dimension) throws SQLException;

    public List<String> getByTableDimension( String groupName) throws SQLException;

    //操作数据集表
    public int putInTableLegend( Legend legend) throws SQLException;

    public List<Legend> getByTableLegend( int diagramId) throws SQLException;

    //操作报表表
    public int putInTableReport( Report report) throws SQLException;

    public Report getByTableReport( String name, int year) throws SQLException;

    //操作搜索表
    public int putInTableSearch( Search search) throws SQLException;

    public List<Search> getByTableSearch( int reportId) throws SQLException;

    //操作x轴表
    public int putInTableXaxis(Xaxis xaxis) throws SQLException;

    public Xaxis getByTableXaxis(int diagramId) throws SQLException;

    //操作y轴表
    public int putInTableYaxis( Yaxis yaxis) throws SQLException;

    public Yaxis getByTableYaxis(int diagramId) throws SQLException;

}
