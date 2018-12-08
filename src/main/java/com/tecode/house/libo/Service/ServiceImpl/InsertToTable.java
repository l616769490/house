package com.tecode.house.libo.Service.ServiceImpl;

import com.tecode.house.libo.Service.InsertTable;
import com.tecode.table.*;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * 插入表格
 */
public class InsertToTable implements InsertTable {

    public static void main(String[] args) {
        InsertToTable s= new InsertToTable();
        //insertSelfTable("2013","自住");
        //insertRoomTable("2013","45","15");
        //insertSingleTable("2013","1999","是否");
    }



    /**
     * 把查询的数据封装进Table   自住、租赁
     * map :(499431400346,  5_1970_2_租赁)
     */
//    @Override
//    public static Table insertSelfTable1(String tableName,String select){
//        RoomByCity02 r = new RoomByCity02();
//        //List<String> ROW = new ArrayList<>();
//        //建表
//        Table table = new Table();
//            table.setYear(2011);
//            //  表头
//
//            Page p = new Page();
//                //当前页
//                p.setThisPage(5);
//                //list存放总页数
//                List<Integer> list = new ArrayList<>();
//
//            Search s = new Search();
//                s.setTitle("居住状态").addValue("自住").addValue("租赁");
//
//            //数据   Row
//
//                //List<String> row1 = new ArrayList<>();
//                //调用获取self表格所需字段的方法
//                 Map<String, String> m = r.selfRentTable(tableName, select);
//                 int size = m.size();
//                 int page = size/10+1;
//                 Iterator<Tuple2<String, String>> iter = m.iterator();
//                while(iter.hasNext()){
//                    Tuple2<String, String> next = iter.next();
//                    Row row = new Row();
//                    String[] s1 = next._2.split("_");
//                    for (String s2 : s1) {
//                        row.addRow(s2);
//                    }
//                    table.addData(row);
//                    //int size = iter.length();
//                    list.add(size);
//                    p.setData(list);
//                }
//                table.setPage(p).addTop("ID").addTop("城市等级").addTop("简称年份")
//                                .addTop("建筑结构").addTop("自住\\租赁")
//                        .addSearchs(s);
//        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
//       // System.out.println("============"+table.getData());
//        System.out.println("总条数"+size);
//        System.out.println("总页数"+page);
//        System.out.println("当前页"+table.getPage().getThisPage());
//        System.out.println(table.getTop());
//        //System.out.println(table.getData().subList(0,10));
//        table.getData().subList(0,10).forEach(e->System.out.println(e.getRow()));
//
//        return table;
//    }


    RoomByCity02 r = new RoomByCity02();
    /**
     * 把查询的数据封装进Table   自住、租赁
     * map :(499431400346,  5_1970_2_租赁)
     */
    @Override
    public Table insertSelfTable(TablePost tablePost) {
        String tableName = tablePost.getYear().toString();
        //建表
        Table table = new Table();

        String filter = null;



        List<Search> searches = tablePost.getSearches();
        for (Search search : searches) {
            if(search.equals("自住")){
                filter = search.toString();
            }else{
                filter = search.toString();
            }
        }

        int page = tablePost.getPage();



//        for(Search s : searches){
//            for(String v : s.getValues()){
//                filter = v;
//            }
//        }
//        table.setYear(tablePost.getYear());
//        //  表头
//

//        //list存放总页数
//        List<Integer> list = new ArrayList<>();
        //List<Search> search = getSearch(tablePost.getYear(), "公平市场租金", "基础分析");
        Search s = new Search();
        s.setTitle("居住状态").addValue("自住").addValue("租赁");

        //调用获取self表格所需字段的方法
        Map<String, String> m = r.selfRentTable(tableName, filter, page);
        int i = m.size();
        int pa = i/10 +1;

        List<Integer> list = new ArrayList<>();
        if (pa <= 5) {
            for (int ii = 1; ii <= pa; ii++) {
                list.add(ii);
            }
        } else {

            if (page - 2 <= 1) {
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            } else if (page + 2 >= pa) {
                for (int ii = pa - 4; ii <= pa; ii++) {
                    list.add(ii);
                }
            } else {
                for (int ii = page - 2; ii <= page + 2; ii++) {
                    list.add(ii);
                }
            }
        }
        table.setYear(tablePost.getYear());
        Page p = new Page();
//        //当前页
        p.setThisPage(tablePost.getPage());
        p.setData(list);
        table.setPage(p);


        //int size = m.size();
        //int page1 = size/10+1;
        Iterator<Tuple2<String, String>> iter = m.iterator();
        while(iter.hasNext()){
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                row.addRow(s2);
            }
            table.addData(row);
            list.add(i);
            p.setData(list);
        }
        table.setPage(p).addTop("ID").addTop("城市等级").addTop("简称年份")
                .addTop("建筑结构").addTop("自住\\租赁")
                .addSearchs(s);
        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
        // System.out.println("============"+table.getData());
        System.out.println("总条数"+i);
        System.out.println("总页数"+pa);
        System.out.println("当前页"+table.getPage().getThisPage());
        System.out.println(table.getTop());
        //System.out.println(table.getData().subList(0,10));
        table.getData().subList(0,10).forEach(e->System.out.println(e.getRow()));

        return table;
    }

    /**
     * 把数据封装进表格         房间数、卧室数
     * (599367700442,599367700442_5_6_3)
     */
    @Override
    public Table insertRoomTable(TablePost tablePost) {
        String tableName = tablePost.getYear().toString();
        String city = null;
        String rooms = null;
        Integer page1 = tablePost.getPage();
        List<Search> searches = tablePost.getSearches();
        for (Search search : searches) {
            if(search.equals("城市等级")){

            }
        }
        RoomByCity02 r = new RoomByCity02();
        //建表
        Table table = new Table();
        table.setYear(2011);
        //  表头

        Page p = new Page();
        //当前页
        p.setThisPage(5);
        //list存放总页数
        List<Integer> list = new ArrayList<>();

        Search s = new Search();
        s.setTitle("城市等级").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5");
        s.setTitle("房间数状态").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5")
                .addValue("6").addValue("7").addValue("8").addValue("9").addValue("10+");

        //数据   Row

        //List<String> row1 = new ArrayList<>();
        //调用获取self表格所需字段的方法
        Map<String, String> m = r.roomsTable(tableName,city,rooms,page1);
        int size = m.size();
        int page = size/10+1;
        Iterator<Tuple2<String, String>> iter = m.iterator();
        while(iter.hasNext()){
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                row.addRow(s2);
            }
            table.addData(row);
            //int size = iter.length();
            list.add(size);
            p.setData(list);
        }
        table.setPage(p).addTop("ID").addTop("城市等级").addTop("房间数")
                .addTop("卧室数")
                .addSearchs(s);
        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
        // System.out.println("============"+table.getData());
        System.out.println("总条数"+size);
        System.out.println("总页数"+page);
        System.out.println("当前页"+table.getPage().getThisPage());
        System.out.println(table.getTop());
        //System.out.println(table.getData().subList(0,10));
        if(m.size()>=10){
            table.getData().subList(0,10).forEach(e->System.out.println(e.getRow()));
        }else{
            table.getData().forEach(e->System.out.println(e.getRow()));
        }


        return table;

    }

    /**
     * 封装进表格        独栋建筑
     */
    @Override
    public Table insertSingleTable(TablePost tablePost) {
        RoomByCity02 r = new RoomByCity02();
        //建表
        Table table = new Table();
//        table.setYear(2011);
//        //  表头
//        Integer page1 = tablePost.getPage();
//        Page p = new Page();
//        //当前页
//        p.setThisPage(5);
//        //list存放总页数
//        List<Integer> list = new ArrayList<>();
//
//        Search s = new Search();
//        s.setTitle("年份").addValue("1900-2000").addValue("2000-2010").addValue("2010-2018");
//
//        //数据   Row
//
//        //List<String> row1 = new ArrayList<>();
//        //调用获取self表格所需字段的方法
//        Map<String, String> m = r.singleTable(tableName,year,single,page1);
//        int size = m.size();
//        int page = size/10+1;
//        Iterator<Tuple2<String, String>> iter = m.iterator();
//        while(iter.hasNext()){
//            Tuple2<String, String> next = iter.next();
//            Row row = new Row();
//            String[] s1 = next._2.split("_");
//            for (String s2 : s1) {
//                row.addRow(s2);
//            }
//            table.addData(row);
//            //int size = iter.length();
//            list.add(size);
//            p.setData(list);
//        }
//        table.setPage(p).addTop("ID").addTop("城市等级").addTop("年份")
//                .addTop("独栋")
//                .addSearchs(s);
//        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
//        // System.out.println("============"+table.getData());
//        System.out.println("总条数"+size);
//        System.out.println("总页数"+page);
//        System.out.println("当前页"+table.getPage().getThisPage());
//        System.out.println(table.getTop());
//        //System.out.println(table.getData().subList(0,10));
//        if(m.size()>=10){
//            table.getData().subList(0,10).forEach(e->System.out.println(e.getRow()));
//        }else{
//            table.getData().forEach(e->System.out.println(e.getRow()));
//        }
//


        return table;
    }
}
