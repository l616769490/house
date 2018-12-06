package com.tecode.house.liaolian.dao.impl;

import com.tecode.house.liaolian.bean.Search;
import com.tecode.house.liaolian.dao.MysqlDao;
import com.tecode.house.liaolian.utils.ConnSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class SearchImpl implements MysqlDao<Search> {
    @Override
    public List findAll() {
        List<Search> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Search ";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Search search = null;
            while ((search = getSearch(rs)) != null) {
                list.add(search);
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

    private Search getSearch(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt ("id");
                String name = rs.getString("name");
                String dimGroupName = rs.getString("dimGroupName");
                int reportId = rs.getInt("reportId");


                Search search = new Search();
                search.setId(id);
                search.setName(name);
                search.setDimgroupname(dimGroupName);
                search.setReportid(reportId);

                return search;
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
        List<Search> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM Search WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") || columns[i].toLowerCase().equals("reportid")) {
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
            Search search = null;
            while ((search = getSearch(rs)) != null) {
                list.add(search);
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
    public int insert(Search search) {
        Connection conn = null;
        PreparedStatement prepar = null;
        int i =0;
        String sql = "insert into search(name,dimGroupName,reportId) values(?,?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1,search.getName());
            prepar.setString(2,search.getDimgroupname());
            prepar.setInt(3,search.getReportid());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    @Override
    public int update(Search search) {
        return 0;
    }

    @Override
    public int delect(Search search) {
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