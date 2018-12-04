package com.tecode.house.zhangzhou.mysqlDao.impl;

import java.sql.*;

public class MysqlDaoImpl {
    private static Connection conn;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/house";
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
            "  `url` varchar(100) NOT NULL,\n" +
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

    public int insertIntoReport(String name,long create,int year,String group,int status,String url){
        int id = 0;
        PreparedStatement ps= null;
        try {
            String sql = "insert into report (`name`,`create`,`year`,`group`,`status`,`url`) values (?,?,?,?,?,?);";
            ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setLong(2,create);
            ps.setInt(3,year);
            ps.setString(4,group);
            ps.setInt(5,status);
            ps.setString(6,url);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int insertIntoDiagram(String name,int type,int reprotId,String subtext){
        int id = -1;
        try {
            String sql = "insert into diagram (`name`,`type`,`reportId`,`subtext`) values (?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setInt(2,type);
            ps.setInt(3,reprotId);
            ps.setString(4,subtext);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public int insertIntoXAxis(String name,int diagramId,String dimGroupName){
        int id = -1;
        try {
            String sql = "insert into xAxis (`name`,`diagramId`,`dimGroupName`) values (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setInt(2,diagramId);
            ps.setString(3,dimGroupName);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public int insertIntoYAxis(String name,int diagramId){
        int id = -1;
        try {
            String sql = "insert into yAxis (`name`,`diagramId`) values (?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setInt(2,diagramId);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int insertIntoDimension(String groupName,String dimName,String dimNameEN){
        int id = -1;
        try {
            String sql = "insert into dimension (`groupName`,`dimName`,`dimNameEN`) values (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,groupName);
            ps.setString(2,dimName);
            ps.setString(3,dimNameEN);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int insertIntoData(String value,int xId,int legendId,String x,String legend){
        int id = -1;
        try {
            String sql = "insert into dimension (`value`,`xId`,`legendId`,`x`,`legend`) values (?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,value);
            ps.setInt(2,xId);
            ps.setInt(3,legendId);
            ps.setString(4,x);
            ps.setString(5,legend);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int insertIntoSearch(String name,String dimGroupName,int reportId){
        int id = -1;
        try {
            String sql = "insert into dimension (`name`,`dimGroupName`,`reportId`) values (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setString(2,dimGroupName);
            ps.setInt(3,reportId);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public static void main(String[] args) {
        MysqlDaoImpl m = new MysqlDaoImpl();


    }
}
