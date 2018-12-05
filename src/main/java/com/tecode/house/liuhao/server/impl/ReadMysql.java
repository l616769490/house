package com.tecode.house.liuhao.server.impl;





import com.tecode.house.liuhao.bean.Xaxis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/5.
 */
public class ReadMysql {

    public List readMysql(Connection conn)  {
        Statement statement = null;

        List<String> list  = new ArrayList<>();
        try {
            statement = conn.createStatement();
            //sql查询语句
            String sql = "select x,value from data";
            //用于返回结果
            ResultSet resultSet = statement.executeQuery(sql);
            String xname = "";
            String xvalue = "";
            String value = "";
            Xaxis x = new Xaxis();
            while(resultSet.next()){
                xname = resultSet.getString(1);
                xvalue = resultSet.getString(2);
                value =xname+":"+xvalue;
                list.add(value);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
