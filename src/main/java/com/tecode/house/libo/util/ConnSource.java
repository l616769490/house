package com.tecode.house.libo.util;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnSource {
    /**
     * 连接池
     */
    private static BasicDataSource dataSource = null;

    /**
     * 获取一个连接对象
     *
     * @return 连接对象
     * @throws SQLException
     */
    public static synchronized Connection getConnection() throws SQLException {
        // 对象未初始化则先初始化
        if (dataSource == null) {
            init();
        }
        // 通过dataSource对象分配一条链接
        return dataSource.getConnection();
    }

    /**
     * 初始化连接池
     */
    private static void init() {
        Properties dbprops = new Properties();
        try {
            // 加载配置文件
            try {
                dbprops.load(ConnSource.class.getClassLoader().getResourceAsStream("jdbc.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 数据库连接参数
            String driver = dbprops.getProperty("jdbc.driver");
            String url = dbprops.getProperty("jdbc.url");
            String user = dbprops.getProperty("jdbc.user");
            String password = dbprops.getProperty("jdbc.password");

            // 初始连接数
            String initialSize = dbprops.getProperty("dataSource.initialSize");
            // 最大空闲连接数
            String maxIdle = dbprops.getProperty("dataSource.maxIdle");
            // 最小空闲连接数
            String minIdle = dbprops.getProperty("dataSource.minIdle");
            // 最大连接数
            String maxActive = dbprops.getProperty("dataSource.maxActive");
            // 最长等待时间
            String maxWait = dbprops.getProperty("dataSource.maxWait");

            dataSource = new BasicDataSource();
            // 给连接池对应的属性（成员变量）设值即可
            dataSource.setDriverClassName(driver);
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            if (initialSize != null) {
                dataSource.setInitialSize(Integer.parseInt(initialSize));
            }
            if (maxIdle != null) {
                dataSource.setMaxIdle(Integer.parseInt(maxIdle));
            }
            if (minIdle != null) {
                dataSource.setMinIdle(Integer.parseInt(minIdle));
            }
            if (maxActive != null) {
                dataSource.setMaxActive(Integer.parseInt(maxActive));
            }
            if (maxWait != null) {
                dataSource.setMaxWait(Integer.parseInt(maxWait));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
