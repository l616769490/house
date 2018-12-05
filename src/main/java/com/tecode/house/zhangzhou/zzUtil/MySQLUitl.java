package com.tecode.house.zhangzhou.zzUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLUitl {
    public static String driver;
    public static String url;
    public static String user;
    public static String password;

    //声明一个Property类型的变量，就理解成是Map类型
    public static Properties prop = new Properties();

    //使用static块给4个变量赋值，static的执行时机一定在静态方法之前
    static {
        try {
            prop.load(MySQLUitl.class.getClassLoader().getResourceAsStream("db.properties"));
            driver = prop.getProperty("jdbc.driver");
            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");
            Class.forName(driver);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取链接的方法
    public static Connection getConn() throws ClassNotFoundException, SQLException {
//		Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    //关闭链接的方法
    public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
