package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.XAxis;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class XAxisImpl implements MysqlDao {
    @Override
    public List findAll() {
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
        XAxis xaxis = (XAxis) o;
        Connection conn = null;
        PreparedStatement prepar = null;
        String xaxisSql = "insert into xaxis(name,diagramId,dimGroupName) values(?,?,?)";
        try {
            prepar.setString(1, xaxis.getName());
            prepar.setInt(2, xaxis.getDiagramid());
            prepar.setString(3, xaxis.getDimgroupname());
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
