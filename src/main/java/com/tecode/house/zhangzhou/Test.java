package com.tecode.house.zhangzhou;

import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;
import com.tecode.house.zhangzhou.service.SparkService;
import com.tecode.table.Page;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import scala.Tuple2;

import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

public class Test {
    public static void main(String[] args) {
        /*Long begin = System.currentTimeMillis();
        System.out.println("正在导入....");
        HBaseDao hBaseDao = new HBaseDaoImpl();
        String path = "e:/american/thads2011.txt";
        hBaseDao.insert("thads:2011",path);
        //hBaseDao.create("thads:2011");
        Long end = System.currentTimeMillis();
        System.out.println("导入成功。");
        System.out.println("花费时间："+(end-begin)/1000);
        Table table = new Table();
        Page page = new Page();
        page.setThisPage(5);

        SparkService sc = new SparkService();
        Map<String, Iterable<String>> dataMap = sc.selectData();
        Iterator<Tuple2<String, Iterable<String>>> iterator = dataMap.iterator();
        while (iterator.hasNext()){
            Iterable<String> stringIterator = iterator.next()._2;
            Iterator<String> iterator1 = stringIterator.iterator();
            int size = stringIterator.toList().size();
            int pages = size/10 + 1;
            System.out.println("pages:"+pages);
            System.out.println("size"+size);
            for(int i = 1;i<=pages;i++){
                table.setPage(page.addData(i));
            }
            table.addSearchs(new Search().setTitle(iterator.next()._1));
            while (iterator1.hasNext()){
                String next = iterator1.next();
                String[] spls = next.split("-");
                for (String spl : spls) {
                    table.addData(new Row().addRow(spl));
                }
            }

        }

        table.getData().subList(0,10).forEach(e-> System.out.println(e));


*/

    }
}
