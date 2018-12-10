package com.tecode.house.dengya.dao.impl;

import com.tecode.house.dengya.bean.*;
import com.tecode.house.dengya.dao.MySQLDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLDaoImpl implements MySQLDao {
    private PreparedStatement ps ;
    private ResultSet rs;
    @Override
    public int putInTableData(Connection conn, Data data) throws SQLException {
        String dataSql = "insert into data(`value`,xId,legendId,x,legend) values(?,?,?,?,?)";
        //占位符赋值
        ps = conn.prepareStatement(dataSql,new String[]{"id"});
        ps.setString(1,data.getValue());
        ps.setInt(2,data.getxId());
        ps.setInt(3,data.getLegendId());
        ps.setString(4,data.getX());
        ps.setString(5,data.getLegend());
        //执行
        long len = ps.executeLargeUpdate();
        //获取新生成的主键的值
        int id = 0;
        if(len > 0){
            id = getId(ps);
            System.out.println(id);

        }
        return id;

    }

    @Override
    public List<Data> getByTableData(Connection conn, int legendId, int xId) throws SQLException {
        List<Data> list = new ArrayList<>();
        String sql = "select * from data where legendId = ? and xId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,legendId);
        ps.setInt(2,xId);
        rs = ps.executeQuery();
        while(rs.next()){
            /*  private int id;
             private String value;
             private int xId;
             private int legendId;
            private String x;
            private String legend;*/
            Data data = new Data(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5),rs.getString(6));
            list.add(data);
        }
        return list;
    }


    @Override
    public int putInTableDiagram(Connection conn, Diagram diagram) throws SQLException {
        String diagramSql = "insert into diagram(`name`,`type`,reportId,subtext) value(?,?,?,?)";
        //占位符赋值
        ps = conn.prepareStatement(diagramSql,new String[]{"id"});
        ps.setString(1,diagram.getName());
        ps.setInt(2,diagram.getType());
        ps.setInt(3,diagram.getReportId());
        ps.setString(4,diagram.getSubtext());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);

        }
        return id;
    }

    @Override
    public List<Diagram> getByTableDiagram(Connection conn, int reportId) throws SQLException {
        List<Diagram> list = new ArrayList<>();
        String sql = "select * from diagram where reportId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,reportId);
        rs = ps.executeQuery();
        while(rs.next()){
            /*private int id;
            private String name;
            private int type;
            private int reportId;
            private String subtext;*/
            Diagram diagram = new Diagram(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
            list.add(diagram);
        }
        return list;
    }


    @Override
    public int putInTableDimension(Connection conn, Dimension dimension) throws SQLException {
        String dimensionSql = "insert into dimension(groupName,dimName,dimNameEN) value(?,?,?)";
        //占位符赋值
        ps = conn.prepareStatement(dimensionSql,new String[] {"id"});
        ps.setString(1,dimension.getGroupName());
        ps.setString(2,dimension.getDimName());
        ps.setString(3,dimension.getGetDimNameEN());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len >0){
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public List<String> getByTableDimension(Connection conn, String groupName) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "select * from dimension where groupName = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1,groupName);
        rs = ps.executeQuery();
        while(rs.next()){
            list.add(rs.getString(3));
        }
        return list;
    }


    @Override
    public int putInTableLegend(Connection conn, Legend legend) throws SQLException {
        String legendSql = "insert into legend(`name`,dimGroupName,diagramId) value(?,?,?)";
        ps = conn.prepareStatement(legendSql,new String[]{"id"});
        //给占位符赋值
        ps.setString(1,legend.getName());
        ps.setString(2,legend.getDimGroupName());
        ps.setInt(3,legend.getDiagramId());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);
            System.out.println(id);
        }

        return id;
    }

    @Override
    public List<Legend> getByTableLegend(Connection conn, int diagramId) throws SQLException {
        List<Legend> list = new ArrayList<>();
        String sql = "select * from diagram where diagramId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        while(rs.next()){
        /* private int id;
           private String name;
           private String dimGroupName;
           private int diagramId;*/
            Legend legend = new Legend(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            list.add(legend);
        }
        return list;
    }


    @Override
    public int putInTableReport(Connection conn, Report report) throws SQLException {
        String reportSql = "insert into report(`name`,`create`,`year`,`group`,status,url) value(?,?,?,?,?,?)";
        ps = conn.prepareStatement(reportSql,new String[]{"id"});
        //给占位符赋值
        ps.setString(1,report.getName());
        ps.setLong(2,report.getCreate());
        ps.setInt(3,report.getYear());
        ps.setString(4,report.getGroup());
        ps.setInt(5,report.getStatus());
        ps.setString(6,report.getUrl());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public Report getByTableReport(Connection conn, String name, int year,String group) throws SQLException {
        Report report = null;
        String sql = "select * from report where name = ? and year = ? and `group` = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1,name);
        ps.setInt(2,year);
        ps.setString(3,group);
        rs = ps.executeQuery();
        if(rs.next()){
            /* private int id;
               private String name;
               private Long create;
               private int year;
               private String group;
               private int status;
               private String url;*/
            report = new Report(rs.getInt(1), rs.getString(2), rs.getLong(3), rs.getInt(4), rs.getString(5), rs.getInt(6), rs.getString(7));

        }
        return report;
    }


    @Override
    public int putInTableSearch(Connection conn, Search search) throws SQLException {
        String searchSql = "insert into search(`name`,dimGroupName,reportId) value(?,?,?)";
        ps = conn.prepareStatement(searchSql,new String[]{"id"});
        //给占位符赋值
        ps.setString(1,search.getName());
        ps.setString(2,search.getDimGroupName());
        ps.setInt(3,search.getReportId());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public List<Search> getByTableSearch(Connection conn, int reportId) throws SQLException {
        List<Search> list = new ArrayList<>();
        String sql = "select * from search where reportId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,reportId);
        rs = ps.executeQuery();
        while(rs.next()){
            /*private int id;
            private String name;
            private String dimGroupName;
            private int reportId;*/
            Search search = new Search(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            list.add(search);
        }
        return list;
    }


    @Override
    public int putInTableXaxis(Connection conn, Xaxis xaxis) throws SQLException {
        String xAaxisSql = "insert into xaxis(`name`,diagramId,dimGroupName) value(?,?,?)";
        ps = conn.prepareStatement(xAaxisSql,new String[]{"id"});
        //给占位符赋值
        ps.setString(1,xaxis.getName());
        ps.setInt(2,xaxis.getDiagramId());
        ps.setString(3,xaxis.getDimGroupName());
        //执行
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    @Override
    public Xaxis getByTableXaxis(Connection conn, int diagramId) throws SQLException {
        Xaxis xaxis = null;
        String sql = "select * from xaxis where diagramId = ?";
        ps =conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        if(rs.next()){
            /* private int id;
                private String name;
                private int diagramId;
                private String dimGroupName;*/
             xaxis = new Xaxis(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
        }
        return xaxis;
    }


    @Override
    public int putInTableYaxis(Connection conn, Yaxis yaxis) throws SQLException {
        String yAxisSql = "insert into yaxis(`name`,diagramId) value(?,?)";
        ps = conn.prepareStatement(yAxisSql,new String[]{"id"});
        //给占位符赋值
        ps.setString(1,yaxis.getName());
        ps.setInt(2,yaxis.getDiagramId());
        long len = ps.executeLargeUpdate();
        int id = 0;
        if(len > 0){
            id = getId(ps);

        }


        return id;
    }

    @Override
    public Yaxis getByTableYaxis(Connection conn, int diagramId) throws SQLException {
        Yaxis yaxis = null;
        String sql = "select * from yaxis where diagramId = ?";
        ps = conn.prepareStatement(sql);
        ps.setInt(1,diagramId);
        rs = ps.executeQuery();
        if(rs.next()){
            /* private int id;
            private String name;
            private int diagramId;*/
          yaxis = new Yaxis(rs.getInt(1),rs.getString(2),rs.getInt(3));
        }
        return yaxis;
    }


    @Override
    public int getId(PreparedStatement ps) throws SQLException {
        int id = -1;
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            id = rs.getInt(1);
        }
        return id;
    }
}
