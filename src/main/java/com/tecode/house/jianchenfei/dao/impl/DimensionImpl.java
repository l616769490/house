package com.tecode.house.jianchenfei.dao.impl;


import com.tecode.house.jianchenfei.bean.Dimension;
import com.tecode.house.jianchenfei.dao.MysqlDao;
import com.tecode.house.jianchenfei.utils.ConnSource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class DimensionImpl implements MysqlDao<Dimension>{
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
        if (columns.length != columns.length) {
            return null;
        }
        List<Dimension> list = new ArrayList<>();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        StringBuffer sql = new StringBuffer("SELECT * FROM Dimension WHERE");
        for (int i = 0; i < columns.length; i++) {
            sql.append(" " + columns[i] + "=");
            if (columns[i].toLowerCase().equals("id") ) {
                sql.append(values[i]);
            } else if (columns[i].toLowerCase().equals("groupname") || columns[i].toLowerCase().equals("dimname")||  columns[i].toLowerCase().equals("dimnameen")) {
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

    @Override
    public int insert(Dimension dimension) {
        Connection conn = null;
        PreparedStatement prepar = null;
        int i = 0;
        String sql = "insert into dimension(groupName,dimName,dimNameEN) values(?,?,?)";
        try {
            conn = ConnSource.getConnection();
            prepar = conn.prepareStatement(sql);
            prepar.setString(1, dimension.getGroupname());
            prepar.setString(2, dimension.getDimname());
            prepar.setString(3, dimension.getDimnameen());
            return prepar.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return i;
    }

    @Override
    public int update(Dimension dimension) {
        return 0;
    }

    @Override
    public int delect(Dimension dimension) {
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
