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
public class DiagramImpl implements MysqlDao{
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
        return null;
    }

    @Override
    public int insert(Object o) {
        Diagram diagram = (Diagram) o;
        Connection conn = null;
        PreparedStatement prepar = null;
        String diagramSql = "insert into diagram(name,`type`,reportId,subtext) values(?,?,?,?)";
        try {
            prepar.setString(1, diagram.getName());
            prepar.setInt(2, diagram.getType());
            prepar.setInt(3, diagram.getReportid());
            prepar.setString(4, diagram.getSubtext());
            return prepar.executeUpdate();
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
