package com.tecode.house.dengya.showservice.impl;
import com.tecode.house.dengya.dao.HbaseDao;
import com.tecode.house.dengya.dao.impl.HbaseDaoImpl;
import com.tecode.house.dengya.showservice.TableService;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
@Service
public class TableServiceImpl implements TableService {
    HbaseDao hbaseDao = new HbaseDaoImpl();
    @Override
    public Table getTableForUnits(TablePost tablePost) {
        //构建Hbase表名
        String tableName = "thads:"+tablePost.getYear().toString();
        //获取搜索条件
        String filter = tablePost.getSearches().get(0).getValues().get(0);
        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> rows = hbaseDao.getForUnits(tableName, filter, page);
        int i = (int)rows._1;
        //封装页数列表
        int pa = i/10+1;
        List<Integer> list = new ArrayList<>();
        for(int x = 1;x < pa;x++){
            list.add(Integer.valueOf(x));
        }
        Table table = new Table();
        table.setYear(tablePost.getYear());
        //构建page对象
        Page p = new Page();
        p.setThisPage(tablePost.getPage());
        p.setData(list);
        table.setPage(p);
        //构建search对象
        Search search = new Search();
        search.setTitle("建筑单元数");
        List<String> units = new ArrayList<>();
        units.add("singleFamliy");
        units.add("2-200");
        units.add("200-400");
        units.add("400-600");
        units.add("600-800");
        units.add("800+");
        search.setValues(units);
        List<Search> ls = new ArrayList<>();
        ls.add(search);
        table.setSearch(ls);
        //表格头
        List<String> tableN = new ArrayList<>();
        tableN.add("房屋编号");
        tableN.add("城市等价");
        tableN.add("建成年份");
        tableN.add("建筑单元数");
        table.setTop(tableN);
        List<Row> rowll = new ArrayList<>();
        for(ArrayList<String> strings: rows._2){
            Row r = new Row();
            r.setRow(strings);
            rowll.add(r);
        }
        table.setData(rowll);
        return table;
    }

    @Override
    public Table getTableForUnits(Integer page, Integer year) {
        //构建Hbase表名
        String tableName = "thads:"+year.toString();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> rows = hbaseDao.getAllForUnits(tableName,page);


        int i = (int)rows._1;
        //封装页数列表
        int pa = i/10+1;
        List<Integer> list = new ArrayList<>();
        for(int x = 1;x < pa;x++){
            list.add(Integer.valueOf(x));
        }
        Table table = new Table();
        table.setYear(year);
        //构建page对象
        Page p = new Page();
        p.setThisPage(page);
        p.setData(list);
        table.setPage(p);
        //构建search对象
        Search search = new Search();
        search.setTitle("建筑单元数");
        List<String> units = new ArrayList<>();
        units.add("singleFamliy");
        units.add("2-200");
        units.add("200-400");
        units.add("400-600");
        units.add("600-800");
        units.add("800+");
        search.setValues(units);
        List<Search> ls = new ArrayList<>();
        ls.add(search);
        table.setSearch(ls);
        //表格头
        List<String> tableN = new ArrayList<>();
        tableN.add("房屋编号");
        tableN.add("城市等价");
        tableN.add("建成年份");
        tableN.add("建筑单元数");
        table.setTop(tableN);
        List<Row> rowll = new ArrayList<>();
        for(ArrayList<String> strings: rows._2){
            Row r = new Row();
            r.setRow(strings);
            rowll.add(r);
        }
        table.setData(rowll);
        System.out.println();
        return table;
    }

    @Override
    public Table getTableForPrice(TablePost tablePost) {
        //获取表名
        String tableName = "thads:"+tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();
        //获取城市条件
        String city = null;
        //获取租金条件
        String rent = null;
        //获取价格条件
        String price =null;
        for(Search search:tablePost.getSearches()){
            String title = search.getTitle();
            if(title.equals("城市规模")){
                city = search.getValues().get(0);
            }else if(title.equals("租金区间")){
                rent = search.getValues().get(0);
            }else if(title.equals("价格区间")){
                price = search.getValues().get(0);
            }
        }
        Tuple2<Object, List<ArrayList<String>>> Rows = null;
        if(city !=null && rent!= null &&price != null){
            Rows = hbaseDao.getForValue(tableName,rent,price,city,page);
        }else if(city == null && rent != null && price == null ){
            Rows= hbaseDao.getForValueByRent(tableName,rent,page);
        }else if(city != null && rent == null && price ==null){
            Rows=hbaseDao.getForValueByCity(tableName,city,page);
        }else if(city == null && rent ==null && price != null){
            Rows= hbaseDao.getForValueByPrice(tableName,price,page);
        }else if(city != null && rent != null && price ==null){
            Rows= hbaseDao.getForValueByCityAndRent(tableName,city,rent,page);
        }else if(city != null && rent == null && price != null){
            Rows=hbaseDao.getForValueByCityAndPrice(tableName,city,price,page);
        }else if(city == null && rent != null && price != null){
            Rows= hbaseDao.getForValueByRentAndPrice(tableName,rent,price,page);
        }
        int i = (int) Rows._1;
        //封装页数列表
        int pa = i / 10 + 1;
        List<Integer> list = new ArrayList<>();
        for (int x = 1; x <= pa; x++) {
            list.add(Integer.valueOf(x));
        }
        Table table = new Table();
        table.setYear(tablePost.getYear());
        //构建Page对象
        Page p = new Page();
        p.setThisPage(page);
        p.setData(list);
        table.setPage(p);
        // 构建 城市规模Search对象
        Search citySerch = new Search();
        citySerch.setTitle("城市规模");
        List<String> cityTit = new ArrayList<>();
        cityTit.add("一级城市");
        cityTit.add("二级城市");
        cityTit.add("三级城市");
        cityTit.add("四级城市");
        cityTit.add("五级城市");
        citySerch.setValues(cityTit);
        //构建 租金区间Search对象
        Search rentSearch = new Search();
        rentSearch.setTitle("租金区间");
        List<String> rentTit = new ArrayList<>();
        rentTit.add("0-1000");
        rentTit.add("1000-1500");
        rentTit.add("1500-2000");
        rentTit.add("2000-2500");
        rentTit.add("2500-3000");
        rentTit.add("3000+");
        rentSearch.setValues(rentTit);
        //构建 价格区间Search对象
        Search priceSearch = new Search();
        priceSearch.setTitle("价格区间");
        List<String> priceTit = new ArrayList<>();
        priceTit.add("0-50");
        priceTit.add("50-100");
        priceTit.add("100-150");
        priceTit.add("150-200");
        priceTit.add("200-250");
        priceTit.add("250-300");
        priceSearch.setValues(priceTit);
        List<Search> ls = new ArrayList<>();
        ls.add(citySerch);
        ls.add(rentSearch);
        ls.add(priceSearch);
        table.setSearch(ls);
        //表头
        List<String> l = new ArrayList<>();
        l.add("房屋编号");
        l.add("城市等级");
        l.add("房屋售价");
        l.add("房屋租金");
        l.add("建成年份");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for(ArrayList<String> strings:Rows._2){
            Row r = new Row();
            r.setRow(strings);
            lll.add(r);
        }
        table.setData(lll);
        return table;

    }

    @Override
    public Table getTableForPrice(Integer page, Integer year) {
        String tableName = "thads:"+year.toString();
        Tuple2<Object, List<ArrayList<String>>> Rows = hbaseDao.getAllForValue(tableName, page);
        int i = (int) Rows._1;
        //封装页数列表
        int pa = i / 10 + 1;
        List<Integer> list = new ArrayList<>();
        for (int x = 1; x <= pa; x++) {
            list.add(Integer.valueOf(x));
        }
        Table table = new Table();
        table.setYear(year);
        //构建Page对象
        Page p = new Page();
        p.setThisPage(page);
        p.setData(list);
        table.setPage(p);
        // 构建 城市规模Search对象
        Search citySerch = new Search();
        citySerch.setTitle("城市规模");
        List<String> cityTit = new ArrayList<>();
        cityTit.add("一级城市");
        cityTit.add("二级城市");
        cityTit.add("三级城市");
        cityTit.add("四级城市");
        cityTit.add("五级城市");
        citySerch.setValues(cityTit);
        //构建 租金区间Search对象
        Search rentSearch = new Search();
        rentSearch.setTitle("租金区间");
        List<String> rentTit = new ArrayList<>();
        rentTit.add("0-1000");
        rentTit.add("1000-1500");
        rentTit.add("1500-2000");
        rentTit.add("2000-2500");
        rentTit.add("2500-3000");
        rentTit.add("3000+");
        rentSearch.setValues(rentTit);
        //构建 价格区间Search对象
        Search priceSearch = new Search();
        priceSearch.setTitle("价格区间");
        List<String> priceTit = new ArrayList<>();
        priceTit.add("0-50");
        priceTit.add("50-100");
        priceTit.add("100-150");
        priceTit.add("150-200");
        priceTit.add("200-250");
        priceTit.add("250-300");
        priceSearch.setValues(priceTit);
        List<Search> ls = new ArrayList<>();
        ls.add(citySerch);
        ls.add(rentSearch);
        ls.add(priceSearch);
        table.setSearch(ls);
        //表头
        List<String> l = new ArrayList<>();
        l.add("房屋编号");
        l.add("城市等级");
        l.add("房屋售价");
        l.add("房屋租金");
        l.add("建成年份");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for(ArrayList<String> strings:Rows._2){
            Row r = new Row();
            r.setRow(strings);
            lll.add(r);
        }
        table.setData(lll);


        return table;
    }
}
