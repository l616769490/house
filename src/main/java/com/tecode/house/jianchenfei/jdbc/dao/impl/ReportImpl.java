package com.tecode.house.jianchenfei.jdbc.dao.impl;


import com.tecode.house.jianchenfei.jdbc.bean.Diagram;
import com.tecode.house.jianchenfei.jdbc.bean.Dimension;
import com.tecode.house.jianchenfei.jdbc.bean.Legend;
import com.tecode.house.jianchenfei.jdbc.bean.Report;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class ReportImpl implements MysqlDao<Report> {

    @Override
    public List<Report> findAll() {
        List<Report> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Report";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Report report = null;
            while ((report = getReport(rs)) != null) {
                list.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return list;
    }

    private Report getReport(ResultSet rs) {
        try {
            if (rs.next()) {

                String name = rs.getString("name");
                long create= rs.getLong("create");
                int year = rs.getInt("year");
                String group = rs.getString("group");
                int status = rs.getInt("status");
                String url = rs.getString("url");

                Report report = new Report();
                report.setName(name);
                report.setCreate(create);
                report.setYear(year);
                report.setGroup(group);
                report.setStatus(status);
                report.setUrl(url);

                return report;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Report> findByColumn(String column, String value) {
        return null;
    }

    @Override
    public List<Report> findByColums(String[] columns, String[] values) {
        if (columns.length != columns.length) {
            return null;
        }
        List<Report> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM report WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") || columns[i].toLowerCase().equals("year")
                    || columns[i].toLowerCase().equals("status") ||columns[i].toLowerCase().equals("create")) {
                sql.append(values[i]);
            } else if (columns[i].toLowerCase().equals("name") || columns[i].toLowerCase().equals("group")||  columns[i].toLowerCase().equals("url")) {
                sql.append("'" + values[i] + "'");
            }
            if (i != columns.length - 1) {
                sql.append(" AND");
            }
        }
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql.toString());
            Report report = null;
            while ((report = getReport(rs)) != null) {
                list.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;

    }


    @Override
    public int insert(Report report) {
        String sql = "insert into report(name,`create`,year,`group` ,status,url) values (?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement prepar = null;
        int i = 0;
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1,report.getName());
            prepar.setLong(2, report.getCreate());
            prepar.setInt(3, report.getYear());
            prepar.setString(4, report.getGroup());
            prepar.setInt(5, report.getStatus());
            prepar.setString(6,report.getUrl());
            i = prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Override
    public int insert(List<Report> reports) {
        return 0;
    }

    @Override
    public int update(Report report) {
        return 0;
    }

    @Override
    public int update(List<Report> reports) {
        return 0;
    }

    @Override
    public int delect(Report report) {
        return 0;
    }
}
