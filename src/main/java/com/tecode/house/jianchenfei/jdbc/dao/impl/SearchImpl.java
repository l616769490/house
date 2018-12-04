package com.tecode.house.jianchenfei.jdbc.dao.impl;

import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.bean.Search;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import scala.tools.cmd.gen.AnyVals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/12/4.
 */
public class SearchImpl implements MysqlDao {
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
        Search search = (Search) o;
        Connection conn = null;
        PreparedStatement prepar = null;
        String searchSql = "insert into search(name,dimGroupName,reportId) values(?,?,?)";
        try {
            prepar.setString(1,search.getName());
            prepar.setString(2,search.getDimgroupname());
            prepar.setInt(3,search.getReportid());
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
