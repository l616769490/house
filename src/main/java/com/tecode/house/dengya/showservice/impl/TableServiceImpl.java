package com.tecode.house.dengya.showservice.impl;
import com.tecode.house.dengya.dao.HbaseDao;
import com.tecode.house.dengya.dao.MySQLDao;
import com.tecode.house.dengya.dao.impl.HbaseDaoImpl;
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl;
import com.tecode.house.dengya.showservice.TableService;
import com.tecode.house.dengya.utils.MySQLUtil;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Service
public class TableServiceImpl implements TableService {
    HbaseDao hbaseDao = new HbaseDaoImpl();
    private MySQLDao mySQLDao = new MySQLDaoImpl();
    @Override
    public Table getTableForUnits(TablePost tablePost) {
        //构建Hbase表名
        String tableName = "thads:"+tablePost.getYear().toString();
        //获取搜索条件
        String filter = null;
        for(Search search:tablePost.getSearches()){
            for(String value:search.getValues()){
                filter = value;
            }
        }
        System.out.println(filter);
        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> rows = hbaseDao.getAllForUnits(tableName, filter, page);
        int i = (int)rows._1;
        //封装页数列
        int pa = i/10+1;
        List<Integer> list = new ArrayList<>();
        if(pa <= 5){
            for(int ii = 1;ii <= pa;ii++){
                list.add(ii);
            }
        }else{
            if(page - 2 <= 1){
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            }else if(page + 2 >= pa){
                for(int ii = pa -4;ii <= pa; ii++){
                    list.add(ii);
                }
            }else{
                for (int ii= pa -2;ii <= page + 2;ii++){
                    list.add(ii);
                }
            }
        }
        Table table = new Table();
        table.setYear(tablePost.getYear());
        //构建page对象
        Page p = new Page();
        p.setThisPage(tablePost.getPage());
        p.setData(list);
        table.setPage(p);


        List<Search> search = getSearch(tablePost.getYear(),"建筑单元分布图","基础分析");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("房屋编号");
        l.add("城市等价");
        l.add("建成年份");
        l.add("建筑单元数");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for(ArrayList<String> strings: rows._2){
            Row r = new Row();
            r.setRow(strings);
            lll.add(r);
        }
        table.setData(lll);
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
            }else if(title.equals("租金")){
                rent = search.getValues().get(0);
            }else if(title.equals("价格")){
                price = search.getValues().get(0);
            }
        }
        System.out.println(city);
        System.out.println(rent);
        System.out.println(price);
        Tuple2<Object, List<ArrayList<String>>> Rows = hbaseDao.getForValue(tableName,city,rent,price,page);
        int i = (int)Rows._1;
        //封装页数列
        int pa = i/10+1;//总页数
        List<Integer> list = new ArrayList<>();
        if(pa <= 5){
            for(int ii = 1;ii <= pa;ii++){
                list.add(ii);
            }
        }else{
            if((page - 2 )<= 1){
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            }else if((page + 2)>= pa){
                for(int ii = pa -4;ii <= pa; ii++){
                    list.add(ii);
                }
            }else{
                for (int ii= pa -2;ii <= page + 2;ii++){
                    list.add(ii);
                }
            }
        }

        Table table = new Table();
        table.setYear(tablePost.getYear());
        //构建Page对象
        Page p = new Page();
        p.setThisPage(page);
        p.setData(list);
        table.setPage(p);
        // 构建 城市规模Search对象
       /* Search citySerch = new Search();
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
        table.setSearch(ls);*/
       List<Search> search = getSearch(tablePost.getYear(),"市场价格分布图","城市规模统计");
       table.setSearch(search);
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
    public List<Search> getSearch(Integer year, String name, String group) {
        Connection conn = null;
        List<Search> list = new ArrayList<>();
        try {
            conn = MySQLUtil.getConn();
            int reportId = mySQLDao.getByTableReport(conn,name,year,group).getId();
            System.out.println(reportId);


            //根据报表id获取搜索表对象
            List<com.tecode.house.dengya.bean.Search> searchs = mySQLDao.getByTableSearch(conn, reportId);
            //根据搜索条件封装Search对象
            for (com.tecode.house.dengya.bean.Search search : searchs) {
                Search s = new Search();
                s.setTitle(search.getName());
                List<String> tableDimension = mySQLDao.getByTableDimension(conn, search.getDimGroupName());
                s.setValues(tableDimension);
                list.add(s);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            MySQLUtil.close(conn);
        }

        return list;
    }


}
