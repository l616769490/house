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

        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> rows = hbaseDao.getAllForUnits(tableName, filter, page);
        long i = (long)rows._1;

        //封装页数列
        long pa = i/10+1;
        if(i % 10 ==0){
            pa--;
        }
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
                for(int ii = (int)pa -4;ii <= pa; ii++){
                    list.add(ii);
                }
            }else{
                for (int ii= page-2;ii <= page + 2;ii++){
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
            if(title.equals("城市规模搜索")){
                city = search.getValues().get(0);
            }else if(title.equals("住房租金区间搜索")){
                rent = search.getValues().get(0);
            }else if(title.equals("住房售价区间搜索")){
                price = search.getValues().get(0);
            }
        }
        Tuple2<Object, List<ArrayList<String>>> Rows = hbaseDao.getForValue(tableName,city,rent,price,page);
        long i = (long)Rows._1;
        //封装页数列
        long pa = i/10+1;//总页数
        /*if(i == 0){
            pa--;
        }*/
        if(i % 10 ==0){
            pa--;
        }
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
                for(int ii =(int) pa -4;ii <= pa; ii++){
                    list.add(ii);
                }
            }else{
                for (int ii= page -2;ii <= page + 2;ii++){
                    list.add(ii);
                }
            }
        }

        Table table = new Table();
        table.setYear(tablePost.getYear());
        //构建Page对象
        Page p = new Page();
        p.setThisPage(tablePost.getPage());
        p.setData(list);
        table.setPage(p);
       List<Search> search = getSearch(tablePost.getYear(),"市场价格分布图","城市规模");
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
