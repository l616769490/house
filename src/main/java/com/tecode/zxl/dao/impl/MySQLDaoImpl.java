package com.tecode.zxl.dao.impl;

import com.tecode.zxl.dao.MySQLDao;
import com.tecode.zxl.server.impl.Value;
import com.tecode.zxl.until.DBUtil;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

import java.sql.*;

public class MySQLDaoImpl implements MySQLDao {

    @Override
    public boolean into() {
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
            String sql="insert into report value(?,?,?,?,?,?,?)";
            conn.setAutoCommit(false);
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,"MaketValue");
            ps.setLong(3,System.currentTimeMillis());
            ps.setInt(4,2013);
            ps.setString(5,"单项统计");
            ps.setInt(6,0);
            ps.setString(7,"http://166.166.1.10/value");
            int i = ps.executeUpdate();
            if(i>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    reportid= rs.getInt(1);

                }

            }

            String sql1="insert into diagram value(?,?,?,?,?)";
            ps=conn.prepareStatement(sql1,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,"市场价");
            ps.setInt(3,2);
            ps.setInt(4,reportid);
            ps.setString(5,"统计各个区间的市场价");
            int i1 = ps.executeUpdate();
            if(i1>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    digId= rs.getInt(1);

                }

            }

            String sql2="insert into xAxis value(?,?,?,?)";
            ps=conn.prepareStatement(sql2,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,"市场价");
            ps.setInt(3,digId);
            ps.setString(4,"市场价区间");
            int i2 = ps.executeUpdate();
            if(i2>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    xId= rs.getInt(1);

                }

            }

            legId=toLeng(conn,ps,rs,digId);
            Value v=new Value();
            Map<String, Object> map = v.getvalue();
            Iterator<Tuple2<String, Object>> it = map.iterator();
            String sql3="insert into dimension value(?,?,?,?)";
            ps=conn.prepareStatement(sql3,new String[]{"id"});
            while (it.hasNext()) {
                Tuple2<String, Object> tuple2 = it.next();
                ps.setInt(1, 0);
                ps.setString(2, "市场价");
                ps.setString(3, tuple2._1);
                ps.setString(4, null);
                int i3 = ps.executeUpdate();
                if (i3 > 0) {
                    rs = ps.getGeneratedKeys();
                    if(rs.next()){
                        dimId=rs.getInt(1);
                    }
                    dataId=toData(ps, conn, xId, dimId, tuple2._2);

                }


            }




            String sql5="insert into yAxis value(?,?,?)";
            ps=conn.prepareStatement(sql5,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,"价格");
            ps.setInt(3,reportid);
            int i3 = ps.executeUpdate();
            if(i3>0){
                rs=ps.getGeneratedKeys();
                if(rs.next()){
                    yId=rs.getInt(1);
                }
            }



            if(i>0&&i1>0&&i2>0&&i3>0&&dataId>0&&legId>0&&dimId>0&&yId>0){
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

    private int toData(PreparedStatement ps,Connection conn,int xId,int dimId,Object value){

        try {
            String sql="insert into data values(?,?,?,?,?,?)";
            ps=conn.prepareStatement(sql,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,value.toString());
            ps.setInt(3,xId);
            ps.setInt(4,dimId);
            ps.setString(5,null);
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

    private int toLeng(Connection conn,PreparedStatement ps,ResultSet rs,int digId){
        String sql4="insert into legend value(?,?,?,?)";
        int id=0;
        try {
            ps=conn.prepareStatement(sql4,new String[]{"id"});
            ps.setInt(1,0);
            ps.setString(2,"市场价");
            ps.setString(3,"市场价");
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
}
