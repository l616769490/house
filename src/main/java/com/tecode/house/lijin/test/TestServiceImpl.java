package com.tecode.house.lijin.test;

import com.tecode.table.*;
import com.tecode.house.zxl.test.GetData;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public Table getTable(TablePost tablePost) {
        Table table=new Table();
/*

        String search=tablePost.getSearches().get(1).getTitle();

        table.setYear(tablePost.getYear()).addTop("房屋ID").addTop("城市").addTop("市场价");
        Page p=new Page();
        table.setPage(p.setThisPage(tablePost.getPage()));
        GetData getData=new GetData();
        Map<String, Iterable<Tuple3<String, Object, Object>>> data = getData.getData(search);
        Iterator<Tuple2<String, Iterable<Tuple3<String, Object, Object>>>> iterator = data.iterator();
        while (iterator.hasNext()){
            Tuple2<String, Iterable<Tuple3<String, Object, Object>>> next = iterator.next();
            String name = next._1;
            table.addSearchs(new Search().setTitle(name));
            Iterable<Tuple3<String, Object, Object>> it = next._2;
            Iterator<Tuple3<String, Object, Object>> it2 = it.iterator();
            List<Tuple3<String, Object, Object>> tuple3List = it.toList();
            int size = tuple3List.size();
            int page=size/10+1;
            for(int i=1;i<page;i++){
                table.setPage(p.addData(i));
            }
            while (it2.hasNext()){
                Tuple3<String, Object, Object> next1 = it2.next();
                table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));
            }
}*/
        return table;
                }

@Override
public Table getTable() {

        Table table=new Table();
        table.setYear(2013).setPage(new Page().setThisPage(5).addData(2).addData(3).addData(4).addData(5).addData(6))
        .addTop("房屋ID").addTop("城市").addTop("市场价");

        GetData getData=new GetData();
        Map<String, Iterable<Tuple3<String, Object, Object>>> data = getData.getData();
        Iterator<Tuple2<String, Iterable<Tuple3<String, Object, Object>>>> iterator = data.iterator();
        while (iterator.hasNext()){
        Tuple2<String, Iterable<Tuple3<String, Object, Object>>> next = iterator.next();
        String name = next._1;
        table.addSearchs(new Search().setTitle(name));
        Iterable<Tuple3<String, Object, Object>> it = next._2;
        Iterator<Tuple3<String, Object, Object>> it2 = it.iterator();
        while (it2.hasNext()){
        Tuple3<String, Object, Object> next1 = it2.next();
        table.addData(new Row().addRow(next1._1()).addRow(next1._2().toString()).addRow(next1._3().toString()));
        }
        }
        return table;

        }
}
