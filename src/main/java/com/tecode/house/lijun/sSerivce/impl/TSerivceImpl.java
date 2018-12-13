package com.tecode.house.lijun.sSerivce.impl;

import com.tecode.house.azouchao.util.MySQLUtil;
import com.tecode.house.lijun.dao.GetHBaseDao;
import com.tecode.house.lijun.dao.MySqlDao;
import com.tecode.house.lijun.dao.impl.GetHBaseDaoImpl;
import com.tecode.house.lijun.dao.impl.MySqlDaoImpl;
import com.tecode.house.lijun.sSerivce.TSerivce;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TSerivceImpl implements TSerivce {

    GetHBaseDao hBaseDao = new GetHBaseDaoImpl();

    @Override
    public Table getTableForCost(TablePost tablePost) {
        //构建HBAse表名
        String tableName =  tablePost.getYear().toString();
        //获取搜索条件
        String filter = tablePost.getSearches().get(0).getValues().get(0);
        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据                                                      "thads:"+tableName, page
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getForCOST( "thads:"+tableName,filter, page);
        int i = (int) Rows._1;
        //封装页数列表
        int pa = i / 10 + 1;
        List<Integer> list = new ArrayList<>();

        if (pa <= 5) {
            for (int ii = 1; ii <= pa; ii++) {
                list.add(ii);
            }
        } else {

            if (page - 2 <= 1) {
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            } else if (page + 2 >= pa) {
                for (int ii = pa - 4; ii <= pa; ii++) {
                    list.add(ii);
                }
            } else {
                for (int ii = page - 2; ii <= page + 2; ii++) {
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
        //构建Search对象
        Search se = new Search();
        se.setTitle("总费用区间");
        List<String> va = new ArrayList<>();
        va.add("0-1500");
        va.add("1500-3000");
        va.add("3000-4500");
        va.add("4500-6000");
        va.add("6000+");
        se.setValues(va);
        List<Search> ls = new ArrayList<>();
        ls.add(se);
        table.setSearch(ls);
        List<String> l = new ArrayList<>();
        l.add("城市等级");
        l.add("房屋费");
        l.add("水电费");
        l.add("其他费用");
        l.add("家庭收入");
        l.add("家庭人数");
        l.add("总费用");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for (ArrayList<String> strings : Rows._2) {
            Row r = new Row();
            r.setRow(strings);
            lll.add(r);
        }
        table.setData(lll);
        return table;
    }

    @Override
    public Table getTablePrice(Integer page, Integer year) {
        String tableName =  year.toString();

        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getPrice("thads:"+tableName, page);
        int i = (int) Rows._1;
        //封装页数列表
        int pa = i / 10 + 1;
        List<Integer> list = new ArrayList<>();
        if (pa <= 5) {
            for (int ii = 1; ii <= pa; ii++) {
                list.add(ii);
            }
        } else {

            if (page - 2 <= 1) {
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            } else if (page + 2 >= pa) {
                for (int ii = pa - 4; ii <= pa; ii++) {
                    list.add(ii);
                }
            } else {
                for (int ii = page - 2; ii <= page + 2; ii++) {
                    list.add(ii);
                }
            }
        }
        Table table = new Table();
        table.setYear(year);
        //构建Page对象
        Page p = new Page();
        p.setThisPage(page);
        p.setData(list);
        table.setPage(p);
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("年龄");
        List<String> va = new ArrayList<>();
        va.add("0-18");
        va.add("18-40");
        va.add("40-65");
        va.add("65+");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("价格");
        List<String> va1 = new ArrayList<>();
        va1.add("0-50");
        va1.add("50-100");
        va1.add("100-150");
        va1.add("150-200");
        va1.add("200-250");
        va1.add("300+");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        ls.add(se1);
        ls.add(se2);
        table.setSearch(ls);
        List<String> l = new ArrayList<>();
        l.add("住房价格");
        l.add("户主年龄");
        l.add("房屋费");
        l.add("其他费用");
        l.add("城市规模");
        l.add("租金");
        l.add("家庭收入");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for (ArrayList<String> strings : Rows._2) {
            Row r = new Row();
            r.setRow(strings);
            lll.add(r);
        }
        table.setData(lll);
        return table;
    }

    @Override
    public Table getTableForIncome(TablePost tablePost) {
        //获取表名
        String tableName =  tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();

        //获取年份条件
        String build = null;
        //获取城市条件
        String city = null;

        Tuple2<Object, List<ArrayList<String>>> Rows = null;
        //调用有搜索条件的方法获取数据

        Rows = hBaseDao.getForIncome("thads:"+tableName,page);


        int i = (int) Rows._1;
        //封装页数列表
        int pa = i / 10 + 1;
        List<Integer> list = new ArrayList<>();
        if (pa <= 5) {

            for (int ii = 1; ii <= pa; ii++) {
                list.add(ii);
            }
        } else {

            if (page - 2 <= 1) {
                list.add(1);
                list.add(2);
                list.add(3);
                list.add(4);
                list.add(5);
            } else if (page + 2 >= pa) {
                for (int ii = pa - 4; ii <= pa; ii++) {
                    list.add(ii);
                }
            } else {
                for (int ii = page - 2; ii <= page + 2; ii++) {
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
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("年龄");
        List<String> va = new ArrayList<>();
        va.add("0-18");
        va.add("18-40");
        va.add("40-65");
        va.add("65+");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("家庭收入");
        List<String> va1 = new ArrayList<>();
        va1.add("0-5");
        va1.add("5-10");
        va1.add("10-15");
        va1.add("15-20");
        va1.add("20+");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        ls.add(se1);
        ls.add(se2);
        table.setSearch(ls);
        List<String> l = new ArrayList<>();
        l.add("住房价格");
        l.add("户主年龄");
        l.add("房屋费");
        l.add("其他费用");
        l.add("卧室数量");
        l.add("家庭收入");
        table.setTop(l);
        List<Row> lll = new ArrayList<>();
        for (ArrayList<String> strings : Rows._2) {
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
        MySqlDao mySQLDao=new MySqlDaoImpl();
        List<Search> list = new ArrayList<>();
        try {
            conn = MySQLUtil.getConn();
            //从mysql数据库获取报表id
            int reportId = mySQLDao.getByTableReport(conn, name, year,group).getId();
            //根据报表id获取搜索表对象
            List<com.tecode.house.lijun.bean.Search> searchs = mySQLDao.getByTableSearch(conn, reportId);
            //根据搜索条件封装Search对象
            for (com.tecode.house.lijun.bean.Search search : searchs) {
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
        } finally {
            MySQLUtil.close(conn);
        }
        return list;
    }

}
