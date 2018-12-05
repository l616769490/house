package com.tecode.house.liuhao.test;

import com.tecode.house.liuhao.server.impl.ReadMysql;
import com.tecode.house.liuhao.utils.DataToHbase;
import com.tecode.house.liuhao.utils.HBaseUtil;
import com.tecode.house.liuhao.utils.MySQLUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/11/28.
 */
public class TestDataToHbase {
    public static void main(String[] args) throws IOException {

        try {
            ReadMysql read = new ReadMysql();
            Connection conn = MySQLUtil.getConn();
            read.readMysql(conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


/* DataToHbase t = new DataToHbase();
        t.readFile();*/

    }
}
