package com.tecode.house.libo.Service;

import com.tecode.table.Page;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

import java.util.ArrayList;
import java.util.List;

public class SelfToTable {




    /**
     * 把查询的数据封装进Table   自住、租赁
     * map :(499431400346,  5_1970_2_租赁)
     */
    public static Table insertSelfTable(String tableName,String select){
        RoomByCity02 r = new RoomByCity02();
        //List<String> ROW = new ArrayList<>();
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
                s.setTitle("居住状态").addValue("自住").addValue("租赁");

            //数据   Row

                //List<String> row1 = new ArrayList<>();
                //调用获取self表格所需字段的方法
                 Map<String, String> m = r.selfRentTable(tableName, select);
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
                table.setPage(p).addTop("ID").addTop("城市等级").addTop("简称年份")
                                .addTop("建筑结构").addTop("自住\\租赁")
                        .addSearchs(s);
        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
       // System.out.println("============"+table.getData());
        System.out.println("总条数"+size);
        System.out.println("总页数"+page);
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
    public static void insertRoomTable(String tableName,String city,String rooms){
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
        Map<String, String> m = r.roomsTable(tableName,city,rooms);
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





    }

    public static void main(String[] args) {
        SelfToTable s= new SelfToTable();
        //insertSelfTable("2013","自住");
        //insertRoomTable("2013","45","15");
        insertSingleTable("2013","1999","是否");

    }

    /**
     * 封装进表格        独栋建筑
     */
    public static void insertSingleTable(String tableName,String year,String single){
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
        s.setTitle("年份").addValue("1900-2000").addValue("2000-2010").addValue("2010-2018");

        //数据   Row

        //List<String> row1 = new ArrayList<>();
        //调用获取self表格所需字段的方法
        Map<String, String> m = r.singleTable(tableName,year,single);
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
        table.setPage(p).addTop("ID").addTop("城市等级").addTop("年份")
                .addTop("独栋")
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




    }

}
