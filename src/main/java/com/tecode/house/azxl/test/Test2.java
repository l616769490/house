package com.tecode.house.azxl.test;

import com.tecode.house.azxl.test.GetPerson;
import com.tecode.table.*;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

public class Test2 {
    public static void main(String[] args) {
        Table table=new Table();
        table.setYear(2013).addTop("房屋ID").addTop("城市").addTop("家庭人数");
        int city=3;

        Page p=new Page();
        table.setPage(p.setThisPage(2));
        GetPerson getPerson=new GetPerson();
        Map<String, Iterable<Tuple3<String, Object, Object>>> data = getPerson.getData(city);
        Iterator<Tuple2<String, Iterable<Tuple3<String, Object, Object>>>> iterator = data.iterator();
        while (iterator.hasNext()){
            Tuple2<String, Iterable<Tuple3<String, Object, Object>>> next = iterator.next();

            String search=next._1;
            table.addSearchs(new Search().setTitle(search));
            table.addSearchs(new Search().setTitle("城市："+city));
            Iterable<Tuple3<String, Object, Object>> it = next._2;
            Iterator<Tuple3<String, Object, Object>> it2 = it.iterator();
            List<Tuple3<String, Object, Object>> tuple3List = it.toList();
            int size = tuple3List.size();
            System.out.println("总数据"+size);
            int page=size/10+1;

            for(int i=1;i<=page;i++){
                table.setPage(p.addData(i));
            }
            while (it2.hasNext()){
                Tuple3<String, Object, Object> next1 = it2.next();
                table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));

            }


        }
        System.out.println(table.getYear());

        System.out.println(table.getTop());

        System.out.println(table.getPage().getThisPage());
        System.out.println(table.getPage().getData());

        table.getSearch().forEach(e-> System.out.println(e.getTitle()));

        getData(table);




    }

    private static void getData(Table table){
        int page=table.getPage().getThisPage();
        if(page>(table.getData().size()/10+1)){
            page=table.getData().size()/10+1;
        }
        int start=0;
        if(page>1){
            start=(page-1)*10;
        }
        int end=start+10;
        if(end>table.getData().size()){
            end=table.getData().size();
        }
        table.getData().subList(start,end).forEach(e-> System.out.println(e.getRow()));
    }
}
