package com.tecode.house.zouchao.dao.impl;

import com.tecode.house.zouchao.bean.*;
import com.tecode.house.zouchao.dao.MySQLDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDaoImpl implements MySQLDao {
    private PreparedStatement ps;


    @Override
    public int putInTableData(Connection conn, Data data) throws SQLException {
        String dataSql = "insert into `data`(`value`,xId,legendId) values(?,?,?)";
        ps = conn.prepareStatement(dataSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, data.getValue());
        ps.setInt(2, data.getxId());
        ps.setInt(3, data.getLegendId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableData() {
        String sql = "select * from data where id = ?";
    }

    @Override
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException {

      /*
      `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `type` int(1) NOT NULL,
  `reportId` int(11) NOT NULL,
  `subtext` varchar(100) DEFAULT NULL,
       */
        String diagramSql = "insert into diagram(name,`type`,reportId,subtext) values(?,?,?,?)";
        ps = conn.prepareStatement(diagramSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, diagram.getName());
        ps.setInt(2, diagram.getType());
        ps.setInt(3, diagram.getReportId());
        ps.setString(4, diagram.getSubtext());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableDiagram() {

    }

    @Override
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException {
       /*
        `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupName` varchar(50) NOT NULL,
  `dimName` varchar(50) NOT NULL,
  `dimNameEN` varchar(50) DEFAULT NULL,
        */
        String dimensionSql = "insert into dimension(groupName,dimName,dimNameEN) values(?,?,?)";
        ps = conn.prepareStatement(dimensionSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, dimension.getGroupName());
        ps.setString(2, dimension.getDimName());
        ps.setString(3, dimension.getDimNameEN());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableDimension() {

    }

    @Override
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException {
/*
 `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `dimGroupName` varchar(50) NOT NULL,
  `diagramId` int(11) NOT NULL,
 */
        String legendSql = "insert into legend(name,dimGroupName,diagramId) values(?,?,?)";
        ps = conn.prepareStatement(legendSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, legend.getName());
        ps.setString(2, legend.getDimGroupName());
        ps.setInt(3, legend.getDiagramId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }


    @Override
    public void getByTableLegend() {

    }

    @Override
    public int putInTableReport(Connection conn, Report report) throws SQLException {
        /*
        `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `create` bigint(20) NOT NULL,
  `year` int(4) NOT NULL,
  `group` varchar(50) NOT NULL,
  `status` int(1) NOT NULL,

  "insert into report(name,create,year,group,status) values(?,?,?,?,?,)";
         */
        String reportSql = "insert into report(name,`create`,year,`group` ,status) values (?,?,?,?,?)";
        ps = conn.prepareStatement(reportSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, report.getName());
        ps.setLong(2, report.getCreate());
        ps.setInt(3, report.getYear());
        ps.setString(4, report.getGroup());
        ps.setInt(5, report.getStatus());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableReport() {

    }

    @Override
    public int putInTableSearch(Connection conn, Search search) throws SQLException {
      /*
      `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `dimGroupName` varchar(50) NOT NULL,
  `reportId` int(11) NOT NULL,
       */
        String searchSql = "insert into search(name,dimGroupName,reportId) values(?,?,?)";
        ps = conn.prepareStatement(searchSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, search.getName());
        ps.setString(2, search.getDimGroupName());
        ps.setInt(3, search.getReportId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableSearch() {

    }

    @Override
    public int putInTableXaxis(Connection conn, Xaxis xaxis) throws SQLException {
       /*
        `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `diagramId` int(11) NOT NULL,
  `dimGroupName` varchar(50) NOT NULL,
        */
        String xaxisSql = "insert into xaxis(name,diagramId,dimGroupName) values(?,?,?)";
        ps = conn.prepareStatement(xaxisSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, xaxis.getName());
        ps.setInt(2, xaxis.getDiagramId());
        ps.setString(3, xaxis.getDimGroupName());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableXaxis() {

    }

    @Override
    public int putInTableYaxis(Connection conn, Yaxis yaxis) throws SQLException {
      /*
 `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `diagramId` int(11) NOT NULL,
       */
        String dataSql = "insert into yaxis(`name`,diagramId) values(?,?)";
        ps = conn.prepareStatement(dataSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, yaxis.getName());
        ps.setInt(2, yaxis.getDiagramId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public void getByTableYaxis() {

    }

    @Override
    public int getId(PreparedStatement ps) throws SQLException {
        int id = -1;
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        }
        return id;
    }

}
