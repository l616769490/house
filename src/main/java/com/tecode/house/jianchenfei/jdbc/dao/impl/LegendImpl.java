package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.Legend;
import com.tecode.house.jianchenfei.jdbc.bean.Search;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;
import scala.tools.cmd.gen.AnyVals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class LegendImpl implements MysqlDao<Legend>{
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
        if (columns.length != columns.length) {
            return null;
        }
        List<Legend> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM legend WHERE");
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

    @Override
    public int insert(Legend legend) {
        Connection conn = null;
        PreparedStatement prepar = null;
        int i = 0;
        String sql = "insert into legend(name,dimgroupname,diagramid) values(?,?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1, legend.getName());
            prepar.setString(2, legend.getDimgroupname());
            prepar.setInt(3, legend.getDiagramid());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    @Override
    public int update(Legend legend) {
        return 0;
    }

    @Override
    public int delect(Legend legend) {
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
