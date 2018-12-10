package com.tecode.house.jianchenfei.dao.impl;


import com.tecode.house.jianchenfei.bean.YAxis;
import com.tecode.house.jianchenfei.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class YAxisImpl implements MysqlDao<YAxis> {
    @Override
    public List findAll() {
        List<YAxis> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM YAxis ";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            YAxis yAxis = null;
            while ((yAxis = getYAxis(rs)) != null) {
                list.add(yAxis);
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

    private YAxis getYAxis(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int diagramId = rs.getInt("diagramId");

                YAxis yAxis = new YAxis();
                yAxis.setId(id);
                yAxis.setName(name);
                yAxis.setDiagramid(diagramId);

                return yAxis;
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
        if (columns.length != columns.length) {
            return null;
        }
        List<YAxis> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM yAxis WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") || columns[i].toLowerCase().equals("diagramid")) {
                sql.append(values[i]);
            } else if (columns[i].toLowerCase().equals("name") ) {
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
            YAxis yAxis = null;
            while ((yAxis = getYAxis(rs)) != null) {
                list.add(yAxis);
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
    public int insert(YAxis yaxis) {
        Connection conn = null;
        PreparedStatement prepar = null;
        int id =0;
        String sql = "insert into yaxis(name,diagramId) values(?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            prepar.setString(1, yaxis.getName());
            prepar.setInt(2, yaxis.getDiagramid());
            int i = prepar.executeUpdate();
            if(i>0){
                ResultSet rs = prepar.getGeneratedKeys();
                if(rs.next()){
                    id =  rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return id;
    }

    @Override
    public int update(YAxis yAxis) {
        return 0;
    }

    @Override
    public int delect(YAxis yAxis) {
        return 0;
    }

    @Override
    public int insert(List list) {
        return 0;
    }

    @Override
    public int update(List list) {
        return 0;
    }


}
