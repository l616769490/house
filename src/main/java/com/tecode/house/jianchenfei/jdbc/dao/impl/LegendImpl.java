package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.Legend;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;
import scala.tools.cmd.gen.AnyVals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class LegendImpl implements MysqlDao{
    @Override
    public List findAll() {
        List<Legend> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Legend";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Legend legend = null;
            while ((legend = getLegend(rs)) != null) {
                list.add(legend);
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

    private Legend getLegend(ResultSet rs) {
        try {
            if (rs.next()) {
              String name = rs.getString("name");
              String dimgroupname = rs.getString("dimgroupname");
              int diagramid = rs.getInt("diagramid");

                Legend legend = new Legend();
               legend.setName(name);
               legend.setDimgroupname(dimgroupname);
               legend.setDiagramid(diagramid);

                return legend;
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
        Legend legend = (Legend) o;
        Connection conn = null;
        PreparedStatement prepar = null;
        String legendSql = "insert into legend(name,dimgroupname,diagramid) values(?,?,?)";
        try {
            prepar.setString(1, legend.getName());
            prepar.setString(2, legend.getDimgroupname());
            prepar.setInt(3, legend.getDiagramid());
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
