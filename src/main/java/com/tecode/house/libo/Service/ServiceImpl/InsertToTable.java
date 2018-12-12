package com.tecode.house.libo.Service.ServiceImpl;

import com.tecode.house.libo.Service.InsertTable;
import com.tecode.table.*;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

import java.util.ArrayList;
import java.util.List;

import static org.spark_project.jetty.util.LazyList.iterator;

/**
 * 插入表格
 */
public class InsertToTable implements InsertTable {

    public static void main(String[] args) {
        TablePost t = new TablePost();
        t.setYear(2013);
        t.setPage(5);
        List<Search> s = new ArrayList<>();
        Search ss = new Search();
        s.add(ss.setTitle("城市等级").addValue("3"));
       // s.add(ss.setTitle("房间数").addValue("1"));
        t.setSearches(s);
        //t.setSearches();
        InsertToTable s1= new InsertToTable();
        //s1.insertSelfTable(t);
       s1. insertRoomTable(t);
        //insertSelfTable("2013","自住");
        //insertRoomTable("2013","45","15");
       //s1. insertSingleTable(t);
    }

    /**
     * 把查询的数据封装进Table   自住、租赁
     * map :(499431400346,  5_1970_2_租赁)
     *      (100013130103,1,2005,1,1)
     */
    @Override
    public Table insertSelfTable(TablePost tablePost) {
        RoomByCity02 r = new RoomByCity02();
        String tableName = "thads:"+tablePost.getYear().toString();
        //建表
        Table table = new Table();
        String filter = null;
        for (Search search : tablePost.getSearches()) {
            for (String value : search.getValues()) {
                filter = value;
            }
        }

       // Search search = tablePost.getSearches().get(0);

        Integer pp = tablePost.getPage();
        table.addSearchs(new Search().setTitle("居住状态").addValue("自住").addValue("租赁"));


        table.setYear(tablePost.getYear()).addTop("ID").addTop("城市等级").addTop("简称年份")
                .addTop("建筑结构").addTop("自住\\租赁");

       // System.out.println(tablePost.getSearches().get(0));
        scala.collection.immutable.List<Tuple2<String, String>> m = r.selfRentTable(tableName, filter, pp,table);




        long size = m.size();

        Iterator<Tuple2<String, String>> iter = m.iterator();

        while(iter.hasNext()) {
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                row.addRow(s2);
            }
            table.addData(row);
            //int size = iter.length();

        }





//        System.out.println("查询年份："+table.getYear());
//        //System.out.println("当前页："+table.getPage().getThisPage());
//        System.out.println("所有页："+size);
//        System.out.println(table.getTop());
       // table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        if(m.size()>=10){
            table.getData().subList(0,10);
        }else{
            table.getData();
        }
        return table;

    }


    /**
     * 截取方法
     */
//    def subString(page:Int,count:Int,list:util.List[(String, String)]): util.List[(String, String)] ={
//        if (page*10>count){
//            list.subList(count-10,count)
//        }else{
//            list.subList((page-1)*10,page*10)
//        }
//    }



    /**
     * 把数据封装进表格         房间数、卧室数
     * (599367700442,599367700442_5_6_3)
     */
    @Override
    public Table insertRoomTable(TablePost tablePost) {
        RoomByCity02 r = new RoomByCity02();
        String tableName = tablePost.getYear().toString();
        //建表
        Table table = new Table();
        table.addSearchs(new Search().setTitle("城市等级").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5"));
//        table.addSearchs(new Search().setTitle("房间数状态").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5")
//                .addValue("6").addValue("7").addValue("8").addValue("9").addValue("10+"));

        table.setYear(tablePost.getYear()).addTop("ID").addTop("城市等级").addTop("房间数")
                .addTop("卧室数");

        String filter = null;
        for (Search search : tablePost.getSearches()) {
            for (String value : search.getValues()) {
                filter = value;
            }
        }


        Page p = new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        // System.out.println(tablePost.getSearches().get(0));
        scala.collection.immutable.List<Tuple2<String, String>> m = r.roomsTable("thads:"+tableName, filter, 3,table);
        int pages = m.size();
        Iterator<Tuple2<String, String>> iter = m.iterator();

        while(iter.hasNext()) {
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                row.addRow(s2);
            }
            table.addData(row);
            //int size = iter.length();

        }


        //showUtil(m,table,p,s);

//        for (Search s1 : table.getSearch()) {
//            System.out.println(s1.getTitle());
//        }

//        System.out.println("查询年份："+table.getYear());
//       // System.out.println("当前页："+table.getPage().getThisPage());
//        System.out.println("所有页："+table.getPage().getData().size());
//        System.out.println(table.getTop());
        if(m.size()>=10){
            table.getData().subList(0,10);
        }else{
            table.getData();
        }
        return table;
    }

    /**
     * 封装进表格        独栋建筑
     */
    @Override
    public Table insertSingleTable(TablePost tablePost) {
//        String tableName = tablePost.getYear().toString();
//        //建表
//        Table table = new Table();
//
//        table.addSearchs(new Search().setTitle("年份").addValue("1900-2000").addValue("2000-2010").addValue("2010-2018"));
//        table.addSearchs(new Search().setTitle("是否独栋").addValue("是").addValue("否"));
//
//        table.setYear(tablePost.getYear()).addTop("ID").addTop("城市等级").addTop("年份")
//                .addTop("独栋");
//
//
//
//
//        Page p = new Page();
//        table.setPage(p.setThisPage(tablePost.getPage()));
//        // System.out.println(tablePost.getSearches().get(0));
//        Map<String, String> m = r.singleTable(tableName, tablePost.getSearches().get(0).getValues().get(1),tablePost.getSearches().get(0).getValues().get(0), 3);
//        int pages = m.size();
//        Iterator<Tuple2<String, String>> iter = m.iterator();
//
//        while(iter.hasNext()) {
//            Tuple2<String, String> next = iter.next();
//            Row row = new Row();
//            String[] s1 = next._2.split("_");
//            for (String s2 : s1) {
//                row.addRow(s2);
//            }
//            table.addData(row);
//            //int size = iter.length();
//
//        }
//
//        Search s = new Search();
//        s.setTitle("房屋卧室数");
//        //showUtil(m,table,p,s);
//
//        for (Search s1 : table.getSearch()) {
//            System.out.println(s1.getTitle());
//        }
//
//        System.out.println("查询年份："+table.getYear());
//        System.out.println("当前页："+table.getPage().getThisPage());
//        System.out.println("所有页："+table.getPage().getData().size());
//        System.out.println(table.getTop());
//        if(m.size()>=10){
//            table.getData().subList(0,10).forEach(e->System.out.println(e.getRow()));
//        }else{
//            table.getData().forEach(e->System.out.println(e.getRow()));
//        }
//        return table;
//
//
//
//    }
        RoomByCity02 r = new RoomByCity02();
        //建表
        Table table = new Table();
        String year = table.setYear(tablePost.getYear()).toString();
        //  表头
       // Integer page1 = tablePost.getPage();
        String filter = null;
        for (Search search : tablePost.getSearches()) {
            for (String value : search.getValues()) {
                filter = value;
            }
        }

       // Page p = new Page();
        //当前页
       // p.setThisPage(5);
        //list存放总页数
        //List<Integer> list = new ArrayList<>();

        Search s = new Search();
        s.setTitle("年份").addValue("1900-2000").addValue("2000-2010").addValue("2010-2018");

        //数据   Row
        //List<String> row1 = new ArrayList<>();
        //调用获取self表格所需字段的方法
        scala.collection.immutable.List<Tuple2<String, String>> m = r.singleTable("thads:"+year, filter,tablePost.getPage() ,table);
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
           // list.add(size);
            //p.setData(list);
        }
        table.addTop("ID").addTop("城市等级").addTop("年份")
                .addTop("独栋")
                .addSearchs(s);
        //System.out.println(table.getData()+table.getPage()+table.getSearch()+table.getTop()+table.getYear());
        // System.out.println("============"+table.getData());
//        System.out.println("总条数"+size);
//        System.out.println("总页数"+page);
//       // System.out.println("当前页"+table.getPage().getThisPage());
//        System.out.println(table.getTop());
        //System.out.println(table.getData().subList(0,10));
        if(m.size()>=10){
            table.getData().subList(0,10);//.forEach(e->System.out.println(e.getRow()));
        }else{
            table.getData();//.forEach(e->System.out.println(e.getRow()));
        }



        return table;
    }

}
