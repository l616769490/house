package com.tecode.house.test;

import com.tecode.house.dengya.bean.Data;
import com.tecode.house.dengya.bean.Report;
import com.tecode.house.dengya.dao.HbaseDao;
import com.tecode.house.dengya.dao.MySQLDao;
import com.tecode.house.dengya.dao.impl.HbaseDaoImpl;
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl;
import com.tecode.house.dengya.utils.MySQLUtil;
import org.apache.hadoop.hbase.util.Bytes;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class getValueTest {
    public static void main(String[] args) {
        MySQLDao mySQLDao  = new MySQLDaoImpl();
        try {
            Connection conn = MySQLUtil.getConn();
            Report byTableReport = mySQLDao.getByTableReport(conn, "建筑单元分布图", 2013, "基础分析");
            System.out.println(byTableReport.getId());


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
}
