package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.Report;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;
import scala.Int;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class ReportImpl implements MysqlDao {
    @Override
    public List findAll() {
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
                long create = rs.getLong("create");
                int year= rs.getInt("year");
                String group= rs.getString("group");
                int status = rs.getInt("status");

                Report report = new Report();
                report.setName(name);
                report.setCreate(create);
                report.setYear(year);
                report.setGroup(group);
                report.setStatus(status);

                return report;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    @Override
    public List findByColumn(String column, String value) {
        return null;
    }

    @Override
    public List findByColums(String[] columns, String[] values) {
        return null;
    }

    @Override
    public int insert(Object o) {
        Report report = (Report) o;
        String sql = "insert into report(name,`create`,year,`group` ,status) values (?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement prepar = null;

        try {
            prepar.setString(1,report.getName());
            prepar.setLong(2, report.getCreate());
            prepar.setInt(3, report.getYear());
            prepar.setString(4, report.getGroup());
            prepar.setInt(5, report.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int insert(List list) {
        return 0;
    }

    @Override
    public int update(Object o) {
        return 0;
    }

    @Override
    public int update(List list) {
        return 0;
    }

    @Override
    public int delect(Object o) {
        return 0;
    }
}
