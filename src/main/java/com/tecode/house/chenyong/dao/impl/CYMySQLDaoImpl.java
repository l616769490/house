package com.tecode.house.chenyong.dao.impl;

import com.tecode.house.chenyong.bean.*;
import com.tecode.house.chenyong.dao.CYMySQLDao;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CYMySQLDaoImpl implements CYMySQLDao {

    private PreparedStatement ps;
    private ResultSet rs;
    @Override
    public int putInTableData(Connection conn, Data data) throws SQLException {
        String dataSql = "insert into `data`(`value`,xId,legendId,x,legend) values(?,?,?,?,?)";
        ps = conn.prepareStatement(dataSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, data.getValue());
        ps.setInt(2, data.getxId());
        ps.setInt(3, data.getLegendId());
        ps.setString(4,data.getX());
        ps.setString(5,data.getLegend());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
        }
        return id;
    }

    @Override
    public List<Data> getByTableData(Connection conn, int legendId, int xId ) throws SQLException {
        List<Data> list = new ArrayList<>();
        String sql = "select * from data where legendId = ? and xId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1, legendId);
        ps.setInt(2, xId);

        rs = ps.executeQuery();
        while (rs.next()) {
            Data data = new Data(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getString(6));
            list.add(data);
        }
        return list;

    }

    @Override
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException {
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
        }
        return id;
    }

    @Override
    public List<Diagram> getByTableDiagram(Connection conn,int reportId) throws SQLException {
        String sql = "select * from diagram where reportId = ?";
        List<Diagram> list = new ArrayList<>();
        ps = conn.prepareStatement(sql);
        ps.setInt(1,reportId);
        rs = ps.executeQuery();
        while(rs.next()){
            Diagram diagram = new Diagram(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5));
            list.add(diagram);
        }
        return list;
    }

    @Override
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException {
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
        }
        return id;
    }

    @Override
    public List<String> getByTableDimension(Connection conn, String groupName) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select dimName from dimension where groupName = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1,groupName);
        rs = ps.executeQuery();
        while (rs.next()){
            list.add(rs.getString(1));
        }
        return list;
    }

    @Override
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException {
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
        }
        return id;
    }

    @Override
    public List<Legend> getByTableLegend(Connection conn,int diagramId) throws SQLException {
        List<Legend> list = new ArrayList<>();
        String sql = "select * from legend where diagramId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        while (rs.next()){
            Legend legend = new Legend(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
            list.add(legend);
        }
        return list;
    }

    @Override
    public int putInTableReport(Connection conn, Report report) throws SQLException {
        String reportSql = "insert into report(name,`create`,year,`group` ,status, url) values (?,?,?,?,?,?)";
        ps = conn.prepareStatement(reportSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, report.getName());
        ps.setLong(2, report.getCreate());
        ps.setInt(3, report.getYear());
        ps.setString(4, report.getGroup());
        ps.setInt(5, report.getStatus());
        ps.setString(6, report.getUrl());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
        }
        return id;
    }

    @Override
    public Report getByTableReport(Connection conn,String name,String group,int year) throws SQLException {
        Report report = null;
        String sql = "select  * from report where name = ? and `group` = ? and year = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2,group);
        ps.setInt(3,year);
        rs = ps.executeQuery();
        if (rs.next()){
            report = new Report(rs.getInt(1),rs.getString(2),rs.getLong(3),rs.getInt(4),rs.getString(5),rs.getInt(6),rs.getString(7));
        }

        return report;

    }

    @Override
    public int putInTableSearch(Connection conn, Search search) throws SQLException {
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
        }
        return id;
    }

    @Override
    public List<Search> getByTableSearch(Connection conn,int reportId) throws SQLException {
        List<Search> list = new ArrayList<>();
        String sql = "select * from search where reportId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,reportId);
        rs = ps.executeQuery();
        while (rs.next()){
            Search search = new Search(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
            list.add(search);
        }
        return list;
    }

    @Override
    public int putInTableXaxis(Connection conn, XAxis xAxis) throws SQLException {
        String xAxisSql = "insert into xaxis(name,diagramId,dimGroupName) values(?,?,?)";
        ps = conn.prepareStatement(xAxisSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, xAxis.getName());
        ps.setInt(2, xAxis.getDiagramId());
        ps.setString(3, xAxis.getDimGroupName());
        //执行
        int len1 = ps.executeUpdate();
        //System.out.println(len1);
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
        }
        return id;
    }

    @Override
    public XAxis getByTableXaxis(Connection conn,int diagramId) throws SQLException {
        XAxis xAxis = null;
        String sql = "select * from xAxis where diagramId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        if (rs.next()){
            xAxis = new XAxis(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4));
        }
        return xAxis;
    }

    @Override
    public int putInTableYaxis(Connection conn, YAxis yAxis) throws SQLException {
        String dataSql = "insert into yaxis(`name`,diagramId) values(?,?)";
        ps = conn.prepareStatement(dataSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, yAxis.getName());
        ps.setInt(2, yAxis.getDiagramId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
        }
        return id;
    }

    @Override
    public YAxis getByTableYaxis(Connection conn,int diagramId) throws SQLException {
        YAxis yAxis = null;
        String sql = "select * from yAxis where diagramId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        if(rs.next()){
            yAxis = new YAxis(rs.getInt(1),rs.getString(2),rs.getInt(3));
        }
        return yAxis;
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
