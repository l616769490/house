package com.tecode.house.chenyong.dao.impl;

import com.tecode.house.chenyong.bean.*;
import com.tecode.house.chenyong.dao.MySQLDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLDaoImpl implements MySQLDao {
    @Override
    public int putInTableData(Connection conn, Data data) throws SQLException {


        return 0;
    }

    @Override
    public void getByTableData() {

    }

    @Override
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableDiagram() {

    }

    @Override
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableDimension() {

    }

    @Override
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableLegend() {

    }

    @Override
    public int putInTableReport(Connection conn, Report report) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableReport() {

    }

    @Override
    public int putInTableSearch(Connection conn, Search search) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableSearch() {

    }

    @Override
    public int putInTableXaxis(Connection conn, XAxis xAxis) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableXaxis() {

    }

    @Override
    public int putInTableYaxis(Connection conn, YAxis yAxis) throws SQLException {
        return 0;
    }

    @Override
    public void getByTableYaxis() {

    }

    @Override
    public int getId(PreparedStatement ps) throws SQLException {
        return 0;
    }
}
