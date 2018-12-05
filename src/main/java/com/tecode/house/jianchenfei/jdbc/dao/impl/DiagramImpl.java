package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.Diagram;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/3.
 */
public class DiagramImpl implements MysqlDao<Diagram>{
    @Override
    public List findAll() {
        List<Diagram> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Diagram";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Diagram diagram = null;
            while ((diagram = getDiagram(rs)) != null) {
                list.add(diagram);
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

    private Diagram getDiagram(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt ("id");
                String name = rs.getString("name");
                int type = rs.getInt("type");
                int reportid= rs.getInt("reportid");
                String subtext= rs.getString("subtext");


               Diagram diagram = new Diagram();
               diagram.setId(id);
               diagram.setName(name);
               diagram.setType(type);
               diagram.setReportid(reportid);
               diagram.setSubtext(subtext);
                return diagram;
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
        List<Diagram> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM diagram WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") || columns[i].toLowerCase().equals("reportid")
                    || columns[i].toLowerCase().equals("type")) {
                sql.append(values[i]);
            } else if (columns[i].toLowerCase().equals("name") || columns[i].toLowerCase().equals("subtext")) {
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
            Diagram diagram = null;
            while ((diagram = getDiagram(rs)) != null) {
                list.add(diagram);
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
    public int insert(Diagram diagram) {

        Connection conn ;
        PreparedStatement prepar = null;
        int i= 0;
        String sql = "insert into diagram(name,`type`,reportId,subtext) values(?,?,?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1, diagram.getName());
            prepar.setInt(2, diagram.getType());
            prepar.setInt(3, diagram.getReportid());
            prepar.setString(4, diagram.getSubtext());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    @Override
    public int update(Diagram diagram) {
        return 0;
    }

    @Override
    public int delect(Diagram diagram) {
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
