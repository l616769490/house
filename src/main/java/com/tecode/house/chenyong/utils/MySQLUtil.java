package com.tecode.house.chenyong.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLUtil {

    public static String driver;
    public static String url;
    public static String user;
    public static String password;
    //声明一个properties类型的变量
    public static Properties prop = new Properties();
    //使用静态代码块给属性赋值
    static {
        try {
            prop.load(MySQLUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            driver = prop.getProperty("jdbc.driver");
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //获取链接
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
    //关闭链接
    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
