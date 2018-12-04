package com.tecode.house.zhangzhou.mysqlDao.impl;

import java.sql.*;

public class MysqlDaoImpl {
    private static Connection conn;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/test";
    private static String user = "root";
    private static String password = "root";

    //报表表(report)
    private static String report = "CREATE TABLE `report` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(50) NOT NULL,\n" +
            "  `create` bigint(20) NOT NULL,\n" +
            "  `year` int(4) NOT NULL,\n" +
            "  `group` varchar(50) NOT NULL,\n" +
            "  `status` int(1) NOT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //图表表（diagram）
    private static String diagram = "CREATE TABLE `diagram` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(50) NOT NULL,\n" +
            "  `type` int(1) NOT NULL,\n" +
            "  `reportId` int(11) NOT NULL,\n" +
            "  `subtext` varchar(100) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `reportId` (`reportId`),\n" +
            "  CONSTRAINT `reportId` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //x轴（xAxis）
    private static String xAxis = "CREATE TABLE `xAxis` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(20) NOT NULL,\n" +
            "  `diagramId` int(11) NOT NULL,\n" +
            "  `dimGroupName` varchar(50) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `diagramId` (`diagramId`),\n" +
            "  CONSTRAINT `diagramId` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
    //y轴（yAxis）
    private static String yAxis = "CREATE TABLE `yAxis` (\n" +
            "  `id` int(11) NOT NULL,\n" +
            "  `name` varchar(255) NOT NULL,\n" +
            "  `diagramId` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `ydim` (`diagramId`),\n" +
            "  CONSTRAINT `ydim` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //维度组表和维度表合成一张表（dimension）
    private static String dimension = "CREATE TABLE `dimension` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `groupName` varchar(50) NOT NULL,\n" +
            "  `dimName` varchar(50) NOT NULL,\n" +
            "  `dimNameEN` varchar(50) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //图例（legend）
    private static String legend = "CREATE TABLE `legend` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(50) NOT NULL,\n" +
            "  `dimGroupName` varchar(50) NOT NULL,\n" +
            "  `diagramId` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `diaID` (`diagramId`),\n" +
            "  CONSTRAINT `diaID` FOREIGN KEY (`diagramId`) REFERENCES `diagram` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //数据（data）
    private static String data = "CREATE TABLE `data` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `value` varchar(50) NOT NULL,\n" +
            "  `xId` int(11) NOT NULL,\n" +
            "  `legendId` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `xid` (`xId`),\n" +
            "  KEY `legid` (`legendId`),\n" +
            "  CONSTRAINT `legid` FOREIGN KEY (`legendId`) REFERENCES `legend` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
            "  CONSTRAINT `xid` FOREIGN KEY (`xId`) REFERENCES `xAxis` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    //搜索（search）
    private static String search = "CREATE TABLE `search` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(50) NOT NULL,\n" +
            "  `dimGroupName` varchar(50) NOT NULL,\n" +
            "  `reportId` int(11) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `reid` (`reportId`),\n" +
            "  CONSTRAINT `reid` FOREIGN KEY (`reportId`) REFERENCES `report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
    static {
        try {
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
            if(conn.isClosed()){
                System.out.println("Success to connect Database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库“house”，并切换到“house”数据库
     */
    public void createDataBase(){
        try {
            Statement st = conn.createStatement();
            //String sql = "create database house";
            //int i = st.executeUpdate(sql);
           // System.out.println(i);
            String sql2 = "use house";
            st.executeUpdate(sql2);
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表
     * @param sql：建表的sql语句
     */
    public void createTable(String sql){
        try {
            System.out.println("正在创建表....");
            Statement st = conn.createStatement();
            st.execute(sql);
            st.close();
            System.out.println("创建成功。。");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void selectFromTable(String sql){
        try {
            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MysqlDaoImpl m = new MysqlDaoImpl();
        m.createDataBase();
        m.createTable(diagram);
        m.createTable(xAxis);
        m.createTable(yAxis);
        m.createTable(dimension);
        m.createTable(legend);
        m.createTable(data);
        m.createTable(search);
    }
}
