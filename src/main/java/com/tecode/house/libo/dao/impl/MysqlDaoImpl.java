package com.tecode.house.libo.dao.impl;

import java.sql.*;

public class MysqlDaoImpl {
    private static Connection conn;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/house";
    private static String user = "root";
    private static String password = "root";


    static {
        try {
            Class.forName( driver);
            conn= DriverManager.getConnection(url,user,password);
            if(conn.isClosed()){
                System.out.println("Success to connect Database");
            }
        } catch (Exception e) {
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


    public int insertIntoLegend(String name,String dimGroupName,int diagramId){
        int id = -1;
        try {
            String sql = "insert into legend (`name`,`dimGroupName`,`diagramId`) values (?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});
            ps.setString(1,name);
            ps.setString(2,dimGroupName);
            ps.setInt(3,diagramId);
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
            String sql = "insert into `data` (`value`,`xId`,`legendId`,`x`,`legend`) values (?,?,?,?,?);";
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
            String sql = "insert into search (`name`,`dimGroupName`,`reportId`) values (?,?,?);";
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


    public int selectData(String sql){
        int s =0;
        try {
           Statement ps = conn.createStatement();
            ResultSet resultSet = ps.executeQuery(sql);
            while (resultSet.next()){
               s += (int)Double.parseDouble(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return s;
    }
    public static void main(String[] args) {
        MysqlDaoImpl m = new MysqlDaoImpl();


    }
}
