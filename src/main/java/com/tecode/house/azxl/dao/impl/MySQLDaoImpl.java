package com.tecode.house.azxl.dao.impl;

import com.tecode.house.azouchao.util.MySQLUtil;
import com.tecode.house.azxl.dao.MySQLDao;
import org.springframework.stereotype.Repository;
import scala.Tuple2;
import scala.collection.Iterator;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;


@Repository
public class MySQLDaoImpl implements MySQLDao {

    private boolean delete(int year){
        Connection conn=null;
        try {
            conn = MySQLUtil.getConn();
            String sql="deldete from `report` where `year`=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,year);
            int i = ps.executeUpdate();
            if(i>0){
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            MySQLUtil.close(conn);
        }

        return false;
    }

    @Override
    public boolean into(String name,String reportType,String url,String x,String y,String tpye,Iterator<Tuple2<String, Object>> it,String scrip,String year) {
        Connection conn=null;
        ResultSet rs=null;
        PreparedStatement ps=null;
        int reportid=0;
        int digId=0;
        int xId=0;
        int legId=0;
        int yId=0;
        int dimId=0;
        int dataId=0;
        try {
            conn = MySQLUtil.getConn();
            conn.setAutoCommit(false);

            //写入report表
            reportid=toReport(conn,ps,rs,name,reportType,url,year);

            //写入图表表
            digId=toDig(conn,ps,rs,reportid,tpye,scrip);

            //写入X轴
            xId=toX(conn,ps,rs,digId,tpye,x);

            yId=toY(conn,ps,rs,digId,y);

            //写入图例表
            legId=toLeng(conn,ps,rs,digId,tpye);

            //写入数据表
           String s=toDim(conn,ps,rs,xId,it,tpye,legId);

           dimId=Integer.valueOf(s.split("_")[0]);

           dataId=Integer.valueOf(s.split("_")[1]);





            if(reportid>0&&digId>0&&xId>0&&dataId>0&&legId>0&&dimId>0&&yId>0){
                conn.commit();
                int i = updateStatus(conn, reportid);
                if(i>0){
                    conn.commit();
                    return true;
                }else {
                    return false;
                }

            }
        } catch (SQLException | ClassNotFoundException e) {
            try {
                conn.rollback();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            MySQLUtil.close(conn);
        }


        return false;
    }

    private int updateStatus(Connection conn,int reportId) throws SQLException {
        String sql="update report set status=1 where id=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1,reportId);
        int i = ps.executeUpdate();
        return i;
    }

    @Override
    public java.util.Map<String, Integer> get(int year) {
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/house?useUnicode=true&characterEncoding=utf8";
        String user="root";
        String password="root";

        try{
            Class.forName(driver);
            conn= MySQLUtil.getConn();
            String sql="SELECT d.`value`,d.x,d.legend from `data` d LEFT JOIN legend l ON d.legendId=l.id LEFT JOIN diagram d2 ON l.diagramId=d2.id LEFT JOIN report r ON r.id=d2.reportId where r.`year`="+year;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("市场价".equals(s)) {
                    map.put(rs.getString("legend"), Integer.valueOf(rs.getString("value")));
                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }

        return map;
    }

    @Override
    public Map<String, Integer> getIncome(int year) {
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/house?useUnicode=true&characterEncoding=utf8";
        String user="root";
        String password="root";
        try{
            Class.forName(driver);
            conn= MySQLUtil.getConn();
            String sql="SELECT d.`value`,d.x,d.legend from `data` d LEFT JOIN legend l ON d.legendId=l.id LEFT JOIN diagram d2 ON l.diagramId=d2.id LEFT JOIN report r ON r.id=d2.reportId where r.`year`="+year;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("年收入".equals(s)) {
                    map.put(rs.getString("legend"), Integer.valueOf(rs.getString("value")));

                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }

        return map;
    }

    @Override
    public Map<String, Integer> getPerson(int year) {
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/house?useUnicode=true&characterEncoding=utf8";
        String user="root";
        String password="root";

        try{
            Class.forName(driver);
            conn= MySQLUtil.getConn();
            String sql="SELECT d.`value`,d.x,d.legend from `data` d LEFT JOIN legend l ON d.legendId=l.id LEFT JOIN diagram d2 ON l.diagramId=d2.id LEFT JOIN report r ON r.id=d2.reportId where r.`year`="+year;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("家庭人数".equals(s)) {
                    map.put(rs.getString("legend"), Integer.valueOf(rs.getString("value")));

                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }

        return map;
    }

    private int toData(PreparedStatement ps,Connection conn,int xId,int dimId,Object value,String type,String s){

        try {
            String sql="insert into data values(?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,value.toString());
            ps.setInt(3,xId);
            ps.setInt(4,dimId);
            ps.setString(5,type);
            ps.setString(6,s);
            int i = ps.executeUpdate();
            if(i>0){
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int toLeng(Connection conn,PreparedStatement ps,ResultSet rs,int digId,String type){
        String sql4="insert into legend value(?,?,?,?)";
        int id=0;
        try {
            ps=conn.prepareStatement(sql4,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,type);
            ps.setString(3,type);
            ps.setInt(4,digId);
            int i = ps.executeUpdate();
            if(i>0){
                rs = ps.getGeneratedKeys();
                if(rs.next()){
                    id=rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return id;

    }

    private int toReport(Connection conn,PreparedStatement ps,ResultSet rs,String name,String reportType,String url,String year){
        String sql="insert into report value(?,?,?,?,?,?,?)";
            int reportid=0;
        try {
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,name);
            ps.setLong(3,System.currentTimeMillis());
            ps.setInt(4,Integer.valueOf(year));
            ps.setString(5,reportType);
            ps.setInt(6,0);
            ps.setString(7,url);
            int i = ps.executeUpdate();
            if(i>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    reportid= rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return reportid;

    }

    private int toDig(Connection conn,PreparedStatement ps,ResultSet rs,int reportid,String type,String scrip){
        String sql1="insert into diagram value(?,?,?,?,?)";
        int digId=0;
        try {
            ps=conn.prepareStatement(sql1,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,type);
            ps.setInt(3,2);
            ps.setInt(4,reportid);
            ps.setString(5,scrip);
            int i1 = ps.executeUpdate();
            if(i1>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    digId= rs.getInt(1);

                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return digId;
    }

    private int toX(Connection conn,PreparedStatement ps,ResultSet rs,int digId,String type,String x) throws SQLException {
        String sql2="insert into xaxis value(?,?,?,?)";
        int xId=0;
        ps=conn.prepareStatement(sql2,new String[]{"id"});
        ps.setInt(1,0);
        ps.setString(2,x);
        ps.setInt(3,digId);
        ps.setString(4,type);
        int i2 = ps.executeUpdate();
        if(i2>0){
            rs=ps.getGeneratedKeys();
            if(rs.next()){
                xId= rs.getInt(1);

            }

        }
        return xId;
    }

    private int toY(Connection conn,PreparedStatement ps,ResultSet rs,int digId,String y) throws SQLException {
        String sql5="insert into yaxis value(?,?,?)";
        int yId=0;
        ps=conn.prepareStatement(sql5,new String[]{"id"});
        ps.setInt(1,0);
        ps.setString(2,y);
        ps.setInt(3,digId);
        int i3 = ps.executeUpdate();
        if(i3>0){
            rs=ps.getGeneratedKeys();
            if(rs.next()){
                yId=rs.getInt(1);
            }
        }
        return yId;
    }

    private String toDim(Connection conn,PreparedStatement ps,ResultSet rs,int xId,Iterator<Tuple2<String, Object>> it,String type,int legId) throws SQLException {
        int dimId=0;
        int dataId=0;

        String sql3="insert into dimension value(?,?,?,?)";
        ps=conn.prepareStatement(sql3,new String[]{"id"});
        while (it.hasNext()) {
            Tuple2<String, Object> tuple2 = it.next();
            ps.setInt(1, 0);
            ps.setString(2, type);
            ps.setString(3, tuple2._1);
            ps.setString(4, null);
            int i3 = ps.executeUpdate();
            if (i3 > 0) {
                rs = ps.getGeneratedKeys();
                if(rs.next()){
                    dimId=rs.getInt(1);
                }
                dataId=toData(ps, conn, xId, legId, tuple2._2,type,tuple2._1);

            }

        }

        return dimId+"_"+dataId;

    }


}
