package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/12/3.
 */
public class DataImpl implements MysqlDao<Data> {

    @Override
    public List<Data> findAll() {
        List<Data> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Data";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Data data = null;
            while ((data = getData(rs)) != null) {
                list.add(data);
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

    private Data getData(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt ("id");
                String value = rs.getString("value");
                int xid = rs.getInt("xid");
                int legendId= rs.getInt("legendId");
                String x= rs.getString("x");
                String legend = rs.getString("legend");

                Data data = new Data();
                data.setId(id);
                data.setValue(value);
                data.setXid(xid);
                data.setLegendid(legendId);
                data.setX(x);
                data.setLegend(legend);
                return data;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Data> findByColumn(String column, String value) {
        return null;
    }

    @Override
    public List<Data> findByColums(String[] columns, String[] values) {
        return null;
    }

    @Override
    public int insert(Object o) {
        Data data = (Data) o;
        String sql = "INSERT INTO data(`value`,xid,legendid,x,legend) VALUES (?,?,?,?,?,?)";
        // Connection conn = DBUtil.getConnection();
        Connection conn = null;
        PreparedStatement prepar = null;
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setInt(1,data.getId());
            prepar.setString(2,data.getValue());
            prepar.setInt(3,data.getXid());
            prepar.setInt(4,data.getLegendid());
            prepar.setString(5,data.getX());
            prepar .setString(6,data.getLegend());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepar != null) {
                    prepar.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return 0;
    }

    @Override
    public int insert(List<Data> datas) {
        return 0;
    }

    @Override
    public int update(Data data) {
        return 0;
    }

    @Override
    public int update(List<Data> datas) {
        return 0;
    }

    @Override
    public int delect(Data data) {
        return 0;
    }

    public static void main(String[] args) {
        DataImpl d = new DataImpl();
        System.out.println(d.findAll());
    }
}
