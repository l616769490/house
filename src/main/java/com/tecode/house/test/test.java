package com.tecode.house.test;


import com.tecode.house.dengya.controller.TableController;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.ArrayList;
import java.util.List;


public class test {
    public static void main(String[] args) {
        TableController t = new TableController();
        TablePost tablePost = new TablePost();
       tablePost.setPage(1);
       tablePost.setYear(2013);
       //构建 单元数Search对象
       /*Search search = new Search();
        search.setTitle("单元数");
        List<String> list = new ArrayList<>();
        list.add("0-100");*/
     //   search.setValues(list);
        //构建 建成年份Search对象
     /*  Search search = new Search();
        search.setTitle("城市规模");
        List<String> list1 = new ArrayList<>();
        list1.add("一级城市");
        search.setValues(list1);*/

        //构建 租金对象
     /* Search search1 = new Search();
        search1.setTitle("租金");
        List<String> list2 = new ArrayList<>();
        list2.add("1000-1500");
        search1.setValues(list2);*/

        //构建 租金对象
       Search search2 = new Search();
        search2.setTitle("价格");
        List<String> list3 = new ArrayList<>();
        list3.add("0-50");
        search2.setValues(list3);



     List<Search> ls = new ArrayList<>();
    //ls.add(search);
      //  ls.add(search1);
        ls.add(search2);
        tablePost.setSearches(ls);
        Table table = t.priceByCity(tablePost);
        System.out.println("year:   "+table.getYear());
        System.out.println("this:   "+table.getPage().getThisPage());
        System.out.println("page:   "+table.getPage().getData().toString());
        for(Search search3:table.getSearch()){
            System.out.println("searchs:   "+search3.getValues().toString());
        }
        System.out.println("top:  "+table.getTop().toString());

        for(Row datum:table.getData()){
            System.out.println("data:  "+datum.getRow().toString() );
        }
    }
}
