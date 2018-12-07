package com.tecode.house.zxl.dao.impl;

import com.tecode.house.zxl.dao.MySQLDao;
import com.tecode.house.zxl.until.DBUtil;
import org.springframework.stereotype.Repository;
import scala.Tuple2;
import scala.collection.Iterator;


import java.sql.*;
import java.util.HashMap;



@Repository
public class MySQLDaoImpl implements MySQLDao {

    @Override
    public boolean into(String name,String reportType,String url,String x,String y,String tpye,Iterator<Tuple2<String, Object>> it,String scrip) {
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
            conn = DBUtil.getConn();
            conn.setAutoCommit(false);

            //写入report表
            reportid=toReport(conn,ps,rs,name,reportType,url);

            //写入图表表
            digId=toDig(conn,ps,rs,reportid,tpye,scrip);

            //写入X轴
            xId=toX(conn,ps,rs,digId,tpye,x);

            yId=toY(conn,ps,rs,digId,y);

            //写入图例表
            legId=toLeng(conn,ps,rs,digId,tpye);

            //写入数据表
           String s=toDim(conn,ps,rs,xId,it,tpye);

           dimId=Integer.valueOf(s.split("_")[0]);

           dataId=Integer.valueOf(s.split("_")[1]);





            if(reportid>0&&digId>0&&xId>0&&dataId>0&&legId>0&&dimId>0&&yId>0){
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                conn.rollback();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            DBUtil.colse(conn);
        }


        return false;
    }

    @Override
    public java.util.Map<String, Integer> get() {
        java.util.Map<String,Integer> map=new HashMap<>();
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        try {
            conn=DBUtil.getConn();
            String sql="select d1.dimName,d.value from `data` d right join dimension d1 on d.legendId=d1.id";
            ps= conn.prepareStatement(sql);
            rs= ps.executeQuery();
            while(rs.next()){
                map.put(rs.getString("dimName"),Integer.valueOf(rs.getString("value")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.colse(conn);
        }

        return map;
    }

    private int toData(PreparedStatement ps,Connection conn,int xId,int dimId,Object value,String type){

        try {
            String sql="insert into data values(?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,value.toString());
            ps.setInt(3,xId);
            ps.setInt(4,dimId);
            ps.setString(5,type);
            ps.setString(6,null);
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

    private int toReport(Connection conn,PreparedStatement ps,ResultSet rs,String name,String reportType,String url){
        String sql="insert into report value(?,?,?,?,?,?,?)";
            int reportid=0;
        try {
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,name);
            ps.setLong(3,System.currentTimeMillis());
            ps.setInt(4,2013);
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
        String sql2="insert into xAxis value(?,?,?,?)";
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
        String sql5="insert into yAxis value(?,?,?)";
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

    private String toDim(Connection conn,PreparedStatement ps,ResultSet rs,int xId,Iterator<Tuple2<String, Object>> it,String type) throws SQLException {
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
                dataId=toData(ps, conn, xId, dimId, tuple2._2,type);

            }

        }

        return dimId+"_"+dataId;

    }


}
