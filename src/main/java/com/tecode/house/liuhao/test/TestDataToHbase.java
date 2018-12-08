package com.tecode.house.liuhao.test;

import com.tecode.house.liuhao.control.PackageTable;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 */
public class TestDataToHbase {
    public static void main(String[] args) throws IOException {
        /*PackageTable table = new PackageTable();
        TablePost post = new TablePost();
        post.setPage(6);
        post.setYear(2013);
        Search s = new Search();
        s.setTitle("各线城市的税务表");
        List<Search> l =new ArrayList<>();
        List<String> ls = new ArrayList<>();
        ls.add("一线城市");
        ls.add("二线城市");
        ls.add("三线城市");
        ls.add("四线城市");
        ls.add("五线城市");
        s.setValues(ls);
        l.add(s);
        post.setSearches(l);
        Table cityTable = table.getCityTable(post);
        System.out.println("year:"+cityTable.getYear());
        List<Row> data = cityTable.getData();
        System.out.println("page:"+cityTable.getPage());
        System.out.println("top:"+cityTable.getTop());
        for (Row datum : data) {
            System.out.println("data:"+datum.getRow().toString());
        }*/


        PackageTable table = new PackageTable();
        TablePost post = new TablePost();
        post.setPage(6);
        post.setYear(2013);
        Search s = new Search();
        s.setTitle("建筑结构类型统计");
        List<Search> l =new ArrayList<>();
        List<String> ls = new ArrayList<>();
        ls.add("建筑结构类型");

        s.setValues(ls);
        l.add(s);
        post.setSearches(l);
        Table cityTable = table.getStructuceTypeTable(post);
        System.out.println("year:"+cityTable.getYear());
        List<Row> data = cityTable.getData();
        System.out.println("page:"+cityTable.getPage());
        System.out.println("top:"+cityTable.getTop());
        for (Row datum : data) {
            System.out.println("data:"+datum.getRow().toString());
        }


       /* try {
            ReadMysqlDaoImpl read = new ReadMysqlDaoImpl();
            Connection conn = MySQLUtil.getConn();
            read.ReadTableData(conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


 DataToHbase t = new DataToHbase();
        t.readFile();
*/
    }
}
