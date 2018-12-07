package com.tecode.house.jianchenfei.dao.impl;

;
import com.tecode.house.jianchenfei.bean.XAxis;
import com.tecode.house.jianchenfei.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class XAxisImpl implements MysqlDao<XAxis> {
    @Override
    public List findAll() {
        List<XAxis> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM XAxis ";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            XAxis xAxis = null;
            while ((xAxis = getXAxis(rs)) != null) {
                list.add(xAxis);
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

    private XAxis getXAxis(ResultSet rs) {
        try {
            if (rs.next()) {
               int id = rs.getInt("id");
               String name = rs.getString("name");
               int diagramId = rs.getInt("diagramId");
               String dimGroupName = rs.getString("dimGroupName");

               XAxis xAxis = new XAxis();
               xAxis.setId(id);
               xAxis.setName(name);
               xAxis.setDiagramid(diagramId);
               xAxis.setDimgroupname(dimGroupName);

                return xAxis;
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
        List<XAxis> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM xAxis WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") || columns[i].toLowerCase().equals("diagramid")) {
                sql.append(values[i]);
            } else if (columns[i].toLowerCase().equals("name") || columns[i].toLowerCase().equals("dimgroupname")) {
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
            XAxis xAxis = null;
            while ((xAxis = getXAxis(rs)) != null) {
                list.add(xAxis);
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
    public int insert(XAxis xaxis) {
        Connection conn = null;
        PreparedStatement prepar = null;
        int i = 0;
        String sql = "insert into xaxis(name,diagramId,dimGroupName) values(?,?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1, xaxis.getName());
            prepar.setInt(2, xaxis.getDiagramid());
            prepar.setString(3, xaxis.getDimgroupname());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    @Override
    public int update(XAxis xAxis) {
        return 0;
    }

    @Override
    public int delect(XAxis xAxis) {
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
