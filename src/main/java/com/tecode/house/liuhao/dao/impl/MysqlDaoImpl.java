package com.tecode.house.liuhao.dao.impl;

import com.tecode.house.liuhao.bean.*;
import com.tecode.house.liuhao.dao.MysqlDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Administrator on 2018/12/4.
 */
public class MysqlDaoImpl implements MysqlDao {
    /**
     *
     * @param conn
     * @param report 报表
     * @return
     */
    @Override
    public int putToTableReport(Connection conn, Report report) throws SQLException {
        String sql = "insert into report (name,`create`,year,`group`,status,url) value(?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});

        ps.setString(1,report.getName());
        ps.setLong(2,report.getCreate());
        ps.setInt(3,report.getYear());
        ps.setString(4,report.getGroup());
        ps.setInt(5,report.getStatus());
        ps.setString(6,report.getUrl());
        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
           ResultSet rs = ps.getGeneratedKeys();
           if(rs.next()){
               System.out.println("true");
              id =  rs.getInt(1);
            }
        }

        return id;
    }

    /**
     *
     * @param conn
     * @param diagram 图表表
     * @return
     */
    @Override
    public int putToTableDiagram(Connection conn, Diagram diagram) throws SQLException {
        String sql = "insert into diagram (`name`,`type`,reportId,subtext) value(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,diagram.getName());
        ps.setInt(2,diagram.getType());
        ps.setInt(3,diagram.getReportId());
        ps.setString(4,diagram.getSubtext());
        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    /**
     *
     * @param conn
     * @param x
     * @return
     */
    @Override
    public int putToTablexAxis(Connection conn, Xaxis x) throws SQLException {
        String sql = "insert into xaxis (name,diagramId,dimGroupName) value(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,x.getName());
        ps.setInt(2,x.getDiagramId());
        ps.setString(3,x.getDimGroupName());
        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    @Override
    public int putToTableYaxis(Connection conn, Yaxis y) throws SQLException {
        String sql = "insert into yaxis (name,diagramId) value(?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,y.getName());
        ps.setInt(2,y.getDiagramId());
        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    /**
     *
     * @param conn
     * @param dimension
     * @return
     */
    @Override
    public int putToTableDimension(Connection conn, Dimension dimension) throws SQLException {
        String sql = "insert into dimension (groupNmae,dimName,dimNameEN) value(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,dimension.getGroupName());
        ps.setString(2,dimension.getDimName());
        ps.setString(3,dimension.getDimNameEN());

        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    /**
     *
     * @param conn
     * @param legend
     * @return
     */
    @Override
    public int putToTableLegend(Connection conn, Legend legend) throws SQLException {
        String sql = "insert into legend (name,dimGroupName,diagramId) value(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,legend.getName());
        ps.setString(2,legend.getDimGroupName());
        ps.setInt(3,legend.getDiagramId());

        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    /**
     *
     * @param conn
     * @param data
     * @return
     */
    @Override
    public int putToTableData(Connection conn, Data data) throws SQLException {
        String sql = "insert into data (value,xId,legendId) value(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,data.getValue());
        ps.setInt(2,data.getxId());
        ps.setInt(3,data.getLegendId());

        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }

    /**
     *
     * @param conn
     * @param search
     * @return
     */
    @Override
    public int putToTablesearch(Connection conn, Search search) throws SQLException {
        String sql = "insert into search (name,dimGroupName,reportId) value(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});

        ps.setString(1,search.getName());
        ps.setString(2,search.getDimGroupName());
        ps.setInt(3,search.getReportId());

        //执行
        int len = ps.executeUpdate();
        int id = -1;
        if(len>0){
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                id =  rs.getInt(1);
            }
        }
        return id;
    }
}
