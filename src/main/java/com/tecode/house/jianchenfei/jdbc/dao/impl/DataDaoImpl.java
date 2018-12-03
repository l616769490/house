package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;
import scala.xml.dtd.SystemID;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
public class DataDaoImpl implements MysqlDao<Data> {

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
    public int insert(Data data) {
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
        DataDaoImpl d = new DataDaoImpl();
        System.out.println(d.findAll());
    }
}
