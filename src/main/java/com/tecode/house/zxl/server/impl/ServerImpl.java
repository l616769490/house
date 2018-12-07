package com.tecode.house.zxl.server.impl;


import com.tecode.table.*;
import com.tecode.house.zxl.dao.MySQLDao;
import com.tecode.house.zxl.dao.impl.MySQLDaoImpl;
import com.tecode.house.zxl.server.MaketPriceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.List;
import scala.collection.immutable.Map;

@Service
public class ServerImpl implements MaketPriceServer {

    @Autowired
    private MySQLDao sd = new MySQLDaoImpl();
    private  Value v = new Value();
    private  Person p = new Person();
    private City c = new City();

    @Override
    public void getValue() {

        Map<String, Object> vmap = v.getvalue();
        Iterator<Tuple2<String, Object>> vIt = vmap.iterator();
        boolean value = sd.into("市场价", "单项查询", "http://166.166.1.10/value", "市场价", "价格", "市场价", vIt, "统计价格区间");


        Map<String, Object> pValue = p.getvalue();
        Iterator<Tuple2<String, Object>> pIt = pValue.iterator();

        boolean personValue = sd.into("家庭人数", "单项查询", "http://166.166.1.10/person", "家庭", "人数", "家庭人数", pIt, "统计家庭人数");



        Map<String, Object> cValue = c.getvalue();
        Iterator<Tuple2<String, Object>> cIt = cValue.iterator();
        boolean cityValue = sd.into("家庭收入", "多项查询", "http://166.166.1.10/income", "城市", "收入", "年收入", cIt, "家庭的年收入");
        if (value && personValue && cityValue) {
            System.out.println("插入数据成功");
        } else {
            System.out.println("插入数据失败");
        }

    }

    @Override
    public Table getTable() {

        Table table=new Table();
        table.setYear(2013).setPage(new Page().setThisPage(1).addData(2).addData(3).addData(4).addData(5).addData(6))
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

    @Override
    public Table getTable(TablePost tablePost) {
        Table table=new Table();
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
        }
        return table;
    }

    @Override
    public java.util.Map<String, Integer> getMaket() {
        return sd.get();

    }

}
