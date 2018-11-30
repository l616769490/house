package com.tecode.house.zouchao.dao;

public interface MySQLDao {
    //操作数据表
    public void putInTableData();

    public void getByTableData();

    //操作图表表
    public void putInTableDiagram();

    public void getByTableDiagram();

    //操作维度表
    public void putInTableDimension();

    public void getByTableDimension();

    //操作数据集表
    public void putInTableLegend();

    public void getByTableLegend();

    //操作报表表
    public void putInTableReport();

    public void getByTableReport();

    //操作搜索表
    public void putInTableSearch();

    public void getByTableSearch();

    //操作x轴表
    public void putInTableXaxis();

    public void getByTableXaxis();

    //操作y轴表
    public void putInTableYaxis();

    public void getByTableYaxis();


}
