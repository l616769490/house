package com.tecode.house.libo.jdbc;

import com.tecode.house.libo.bean.*;
import com.tecode.house.libo.util.ConnSource;
import com.tecode.house.libo.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InsertIntoMySQL {

    public static void main(String[] args) throws SQLException {
        InsertIntoMySQL c = new InsertIntoMySQL();
        Report r = new Report();
        r.setName("123");

    }



    private PreparedStatement ps;
    private static Connection conn;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/house";
    private static String user = "root";
    private static String password = "root";



    /**
     * report表
     */
    public int insertReport(Connection conn ,Report report) throws SQLException {
        long time = System.currentTimeMillis();
        int year = 2013;
        String url = "http://166.166.166.166:8080/house";
        String sql = "insert into report (`name`,`create`,`year`,`group`,status,url) values (?,?,?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(sql,new String[]{"id"});


        ps.setString(1,report.getName());
        ps.setInt(2,(int)time);
        ps.setInt(3,year);
        ps.setString(4,report.getGroup());
        ps.setInt(5,report.getStatus());
        ps.setString(6,url);

        int len = ps.executeUpdate();

        int id = 0;
        if(len>0){
            id = getId(ps);
        }
        return id;
    }

    /**
     * 查询reportId当前某值的编号
     */
    public int findByStrReport(){
        Report report = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Report ";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                report = new Report(0,rs.getString(2),rs.getLong(3),rs.getInt(4)
                ,rs.getString(5),rs.getInt(6),rs.getString(7));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        System.out.println("report.getId():=="+report.getId());
        return report.getId();

    }

    /**
     * diagram
     */
    public int insertDiagram(Connection conn ,Diagram diagram) throws SQLException {
        String diagramSql = "insert into diagram(`name`,`type`,reportId,subtext) values(?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(diagramSql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, diagram.getName());
        ps.setInt(2, diagram.getType());
        ps.setInt(3, diagram.getReportId());
        ps.setString(4, diagram.getSubtext());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        System.out.println("id  001 ="+id);
        if (len1 > 0) {
            id = getId(ps);

            System.out.println("id  002 ="+id);
        }
        return id;
    }

    /**
     * 查询diagram当前ID
     */
    public int findByStrDiagram(String str){
        Diagram diagram = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Diagram ";
            ps = conn.prepareStatement(sql);
           // ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                diagram = new Diagram(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return diagram.getId();

    }



    /**
     * x
     */

    public int insertXaxis(Connection conn, Xaxis xaxis) throws SQLException {
       /*
        `id` int(11) NOT NULL AUTO_INCREMENT,
          `name` varchar(20) NOT NULL,
          `diagramId` int(11) NOT NULL,
          `dimGroupName` varchar(50) NOT NULL,
        */
        String Sql = "insert into xaxis(`name`,diagramId,dimGroupName) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(Sql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, xaxis.getName());
        ps.setInt(2, xaxis.getDiagramId());
        ps.setString(3, xaxis.getDimGroupName());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            //System.out.println(id);
        }
        return id;
    }


    /**
     * 查询x的最大编号
     */
    public int findByStrX(String str){
        Xaxis xaxis = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Xaxis";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                xaxis = new Xaxis(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return xaxis.getId();

    }


    /**
     * y
     */
    public int insertYaxis(Connection conn, Yaxis yaxis) throws SQLException {

        String Sql = "insert into yaxis(`name`,diagramId) values(?,?)";
        PreparedStatement ps = conn.prepareStatement(Sql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, yaxis.getName());
        ps.setInt(2, yaxis.getDiagramId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            //System.out.println(id);
        }
        return id;
    }

    /**
     * 查Y最大值
     */
    public int findByStrY(String str){
        Yaxis yaxis = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Yaxis";
            ps = conn.prepareStatement(sql);
           // ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                yaxis = new Yaxis(rs.getInt(1),rs.getString(2),rs.getInt(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return yaxis.getId();

    }

    /**
     * dimension
     */
    public int insertDimension(Connection conn, Dimension dimension) throws SQLException {

        String Sql = "insert into dimension(groupName,dimName,dimNameEN) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(Sql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, dimension.getGroupName());
        ps.setString(2, dimension.getDimName());
        ps.setString(3, dimension.getDimNameEN());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    /**
     * dimention最大值
     */
    public int findByStrdimension(String str){
        Dimension dimension = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Dimension";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                dimension = new Dimension(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return dimension.getId();

    }


    /**
     * legend
     */
    public int insertyLegend(Connection conn, Legend legend) throws SQLException {

        String Sql = "insert into legend(`name`,dimGroupName,diagramId) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(Sql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, legend.getName());
        ps.setString(2, legend.getDimGroupName());
        ps.setInt(3, legend.getDiagramId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }

    /**
     * str====select id from region where kkkkkkkkk
     * Legend最大值
     */
    public int findByStrLegend(String str){
        Legend legend = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Legend";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                legend = new Legend(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return legend.getId();

    }




    /**
     * search
     */
    public int insertSearch(Connection conn, Search search) throws SQLException {

        String Sql = "insert into search(`name`,dimGroupName,reportId) values(?,?,?)";
        PreparedStatement ps = conn.prepareStatement(Sql, new String[]{"id"});
        //占位符赋值
        ps.setString(1, search.getName());
        ps.setString(2, search.getDimGroupName());
        ps.setInt(3, search.getReportId());
        //执行
        int len1 = ps.executeUpdate();
        //获取新生成的主键的值
        //定义一个变量，用于接收新生成的主键的值
        int id = 0;
        if (len1 > 0) {
            id = getId(ps);
            System.out.println(id);
        }
        return id;
    }


    /**
     * findbyLegend
     */
    public int findByStrSearch(String str){
        Search search = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Legend";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                search = new Search(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return search.getId();

    }


    public int insertdata(String value,int xId,int legendId,String x,String legend)  {

        try {
            String sql = "insert into `data`(value,xId,legendId,x,legend) values (?,?,?,?,?);";
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, value);
            ps.setInt(2, xId);
            ps.setInt(3, legendId);
            ps.setString(4, x);
            ps.setString(5, legend);
            int i = ps.executeUpdate();
            if(i>0){
                return i;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *
     */
    public int findByStrData(String str){
        Data data = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConn();
            String sql = "select max(id) from Data";
            ps = conn.prepareStatement(sql);
            //ps.setString(1,str);

            rs = ps.executeQuery();
            if(rs.next()){
                data = new Data(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getString(5),rs.getString(6));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            DBUtil.close(conn);
        }
        return data.getId();

    }




    //查询data
    public List<Data> selectData() throws SQLException {
        List<Data> list = new ArrayList<>();
        String sql = "select * from data";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;

        try {
            conn = ConnSource.getConnection();
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Data data = null;
            while ((data = getData(rs)) != null) {
                list.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stat != null) {
                    stat.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    private Data getData(ResultSet rs) {
        try {
            if (rs.next()) {
                int id = rs.getInt ("id");
                String value = rs.getString("value");
                int xid = rs.getInt("xid");
                int legendId= rs.getInt("legendId");
                String x= rs.getString("x");
                String legend = rs.getString("legend");

                Data data = new Data();
                data.setId(id);
                data.setValue(value);
                data.setxId(xid);
                data.setLegendId(legendId);
                data.setX(x);
                data.setLegend(legend);
                return data;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        return null;
    }













    public int getId(PreparedStatement ps) throws SQLException {
        int id = -1;
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            id = rs.getInt(1);
        }
        return id;
    }


}
