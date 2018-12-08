package com.tecode.house.zouchao.test;

import com.tecode.house.zouchao.controller.TableController;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {

        //HBaseUtil util = new HBaseUtil();
        //util.deleteTable("thads:2011");
        //      System.out.println(util.isNameSpaceExits("thads"));

        //导入数据
        //FilleToHBase fth = new FilleToHBase();
        //fth.readFile("D:\\thads2011.txt","thads:2011");
        //fth.readFile("D:\\thads2013n.csv","thads:2013");

        //分析租金数据
     /*   RentAnalysis rentAnalysis = new RentAnalysis();
        rentAnalysis.analysis("2013");


        //分析价格
        PriceByBuildAnalysis rentByBuildAnalysis = new PriceByBuildAnalysis();
        System.out.println(rentByBuildAnalysis.analysis("2013"));

        //分析房间数
        RoomsByBuildAnalysis roomsByBuildAnalysis = new RoomsByBuildAnalysis();
        System.out.println(roomsByBuildAnalysis.analysis("2013"));*/

        //try {
        //    Connection conn = MySQLUtil.getConn();
        //    MySQLDaoImpl dao = new MySQLDaoImpl();





        //
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //} catch (SQLException e) {
        //    e.printStackTrace();
        //}


        TableController t = new TableController();
        TablePost tp = new TablePost();
        tp.setPage(3);
        tp.setYear(2013);
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("建成年份");
        List<String> va = new ArrayList<>();
        va.add("1900-1940");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("城市规模");
        List<String> va1 = new ArrayList<>();
        va1.add("五线城市");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        //ls.add(se1);
        ls.add(se2);
        tp.setSearches(ls);
        Table table = t.PriceByBuild(tp);
        System.out.println("year:   "+table.getYear());
        System.out.println("this:   "+table.getPage().getThisPage());
        System.out.println("page:   "+table.getPage().getData().toString());
        for (Search search : table.getSearch()) {
            System.out.println("search: "+search.getValues().toString());
        }
        System.out.println("top:    "+table.getTop().toString());

        for (Row datum : table.getData()) {
            System.out.println("data:   "+datum.getRow().toString());
        }


    }
}