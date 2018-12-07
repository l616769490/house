package com.tecode.house.zxl.until;



import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static Properties pro=new Properties();
    private static String driver;
    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            pro.load(ClassLoader.getSystemResourceAsStream("db.properties"));
            user=pro.getProperty("jdbc.user");
            url=pro.getProperty("jdbc.url");
            password=pro.getProperty("jdbc.password");
            driver=pro.getProperty("jdbc.driver");
            Class.forName(driver);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String getStat(String name) {
        return pro.getProperty(name);
    }
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(url,user, password);
    }

    public static void colse(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
