package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Diagram;
import com.tecode.house.jianchenfei.jdbc.bean.Dimension;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;
import scala.tools.cmd.gen.AnyVals;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class DimensionImpl implements MysqlDao{
    @Override
    public List findAll() {
        List<Dimension> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Dimension";
        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Dimension dimension = null;
            while ((dimension = getDimension(rs)) != null) {
                list.add(dimension);
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

    private Dimension getDimension(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt ("id");
                String groupname = rs.getString("groupname");
                String dimname= rs.getString("dimname");
                String dimnameen = rs.getString("dimnameen");


                Dimension dimension = new Dimension();

                dimension.setId(id);
                dimension.setGroupname(groupname);
                dimension.setDimname(dimname);
                dimension.setDimnameen(dimnameen);
                return dimension;
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
        Dimension dimension = (Dimension) o;
        Connection conn = null;
        PreparedStatement prepar = null;
        String dimensionSql = "insert into dimension(groupName,dimName,dimNameEN) values(?,?,?)";
        try {
            prepar.setString(1, dimension.getGroupname());
            prepar.setString(2, dimension.getDimname());
            prepar.setString(3, dimension.getDimnameen());
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
