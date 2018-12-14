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
        s.add(ss.setTitle("城市等级").addValue("1"));
       // s.add(ss.setTitle("房间数").addValue("1"));
        t.setSearches(s);
        //t.setSearches();
        InsertToTable s1= new InsertToTable();
        //s1.insertSelfTable(t);
       //s1. insertRoomTable(t);
        //insertSelfTable("2013","自住");
        s1.insertRoomTable(t);
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

        Integer pp = tablePost.getPage();
        table.addSearchs(new Search().setTitle("居住状态").addValue("自住").addValue("租赁"));


        table.setYear(tablePost.getYear()).addTop("ID").addTop("城市等级").addTop("简称年份")
                .addTop("建筑结构").addTop("自住\\租赁");

        scala.collection.immutable.List<Tuple2<String, String>> m = r.selfRentTable(tableName, filter, pp,table);

        Iterator<Tuple2<String, String>> iter = m.iterator();

        while(iter.hasNext()) {
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                row.addRow(s2);
            }
            table.addData(row);
        }

        if(m.size()>=10){
            table.getData().subList(0,10);
        }else{
            table.getData();
        }
        return table;

    }



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

        Search s1s = new Search();
        s1s.setTitle("城市等级").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5");
        table.addSearchs(s1s);

        table.setYear(tablePost.getYear()).addTop("ID").addTop("城市等级").addTop("房间数")
                .addTop("卧室数");

        Search s2s = new Search();
        s2s.setTitle("房间数").addValue("1").addValue("2").addValue("3").addValue("4").addValue("5").addValue("6").addValue("7")
                .addValue("8").addValue("9").addValue("10+");

        String rom = null;
        String filter = null;
        for (Search search : tablePost.getSearches()) {
            if(search.getTitle().equals("城市等级")){
                filter = search.getValues().get(0);
            }else if(search.getTitle().equals("房间数")){
                rom = search.getValues().get(0);
            }
        }



        table.addSearchs(s2s);


        scala.collection.immutable.List<Tuple2<String, String>> m = r.roomsTable("thads:"+tableName, filter,rom, tablePost.getPage(),table);
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

        }

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

        RoomByCity02 r = new RoomByCity02();
        //建表
        Table table = new Table();
        String tableName =tablePost.getYear().toString();

        Search s1s = new Search();
        s1s.setTitle("年份").addValue("1900-2000").addValue("2000-2010").addValue("2010-2018");


       //处理搜索条件为空
        String filter = null;
        String single = null;
        for (Search search : tablePost.getSearches()) {
            if(search.getTitle().equals("年份")){
                filter = search.getValues().get(0);
            }else{
                single = search.getValues().get(0);
            }
        }


        Search s2s = new Search();
        s2s.setTitle("独栋").addValue("是").addValue("否");





        //数据   Row
        //List<String> row1 = new ArrayList<>();
        //调用获取self表格所需字段的方法
        scala.collection.immutable.List<Tuple2<String, String>> m = r.singleTable("thads:"+tableName, filter,single,tablePost.getPage() ,table);
        int size = m.size();
        //int page = size/10+1;
        Iterator<Tuple2<String, String>> iter = m.iterator();
        while(iter.hasNext()){
            Tuple2<String, String> next = iter.next();
            Row row = new Row();
            String[] s1 = next._2.split("_");
            for (String s2 : s1) {
                    row.addRow(s2);
            }
            table.addData(row);
        }
        table.addTop("ID").addTop("城市等级").addTop("年份")
                .addTop("独栋")
                .addSearchs(s1s)
                .addSearchs(s2s);

        return table;
    }

}
