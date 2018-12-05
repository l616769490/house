package com.tecode.house.libo.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    public static String driver;
    public static String url;
    public static String user;
    public static String password;

    public static Properties prop = new Properties();

    static {
        try {
            prop.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
            driver = prop.getProperty("jdbc.driver");

            url = prop.getProperty("jdbc.url");
            user = prop.getProperty("jdbc.user");
            password = prop.getProperty("jdbc.password");
            Class.forName(driver);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Connection getConn() throws ClassNotFoundException, SQLException {
//		Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    public static void close(Connection conn){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
