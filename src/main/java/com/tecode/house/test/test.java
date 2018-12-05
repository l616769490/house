package com.tecode.house.test;

import com.tecode.house.dengya.bean.Data;
import com.tecode.house.dengya.dao.MySQLDao;
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl;
import com.tecode.house.dengya.utils.MySQLUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class test {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = MySQLUtil.getConn();
            MySQLDao dao = new MySQLDaoImpl();
            List<Data> byTableData = dao.getByTableData(conn, 1, 1);
            for (Data byTableDatum : byTableData) {
                System.out.println(byTableDatum.getValue());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
