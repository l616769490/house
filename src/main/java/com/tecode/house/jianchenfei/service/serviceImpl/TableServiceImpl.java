package com.tecode.house.jianchenfei.service.serviceImpl;

import com.tecode.house.jianchenfei.dao.HBaseDao;
import com.tecode.house.jianchenfei.dao.impl.DimensionImpl;
import com.tecode.house.jianchenfei.dao.impl.HbaseDaoImpl;
import com.tecode.house.jianchenfei.dao.impl.ReportImpl;
import com.tecode.house.jianchenfei.dao.impl.SearchImpl;
import com.tecode.house.jianchenfei.service.TableSerivce;
import com.tecode.house.jianchenfei.utils.ConnSource;
import com.tecode.table.*;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/9.
 */
public class TableServiceImpl implements TableSerivce {

    private HBaseDao hBaseDao = new HbaseDaoImpl();
    private ReportImpl mysqlDao = new ReportImpl();
    private SearchImpl mysqlDaoSearch = new SearchImpl();
    private DimensionImpl mysqlDaoDimension = new DimensionImpl();
    @Override
    public Table getTablePer(TablePost tablePost) {
        String tableName = "thads:" + tablePost.getYear().toString();
        //获取搜索条件
        String filter = null;
        for (Search search : tablePost.getSearches()) {
            for (String value : search.getValues()) {
                filter = value;
            }
        }
        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getPer(tableName,filter,page);
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

        List<Search> search = getSearch(tablePost.getYear(), "家庭人数统计", "基础分析");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("编号");
        l.add("城市等级");
        l.add("家庭收入");
        l.add("房间数");
        l.add("卧室数");
        l.add("家庭人数");
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
    public Table getTableRate(TablePost tablePost) {
        //获取表名
        String tableName = "thads:" + tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();

        //获取年份条件
        String build = null;
        //获取城市条件
        String rate = null;

        for (Search search : tablePost.getSearches()) {
            String title = search.getTitle();
            if (title.equals("建成年份")) {
                build = search.getValues().get(0);
            } else if (title.equals("房产税")) {
                rate = search.getValues().get(0);
            }

        }
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getRate(tableName, build, rate, page);


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
        List<Search> search = getSearch(tablePost.getYear(), "房产税统计", "年份统计");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("编号");
        l.add("城市等级");
        l.add("家庭收入");
        l.add("建成年份");
        l.add("市场价");
        l.add("房产税");
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
    public Table getTableSingle(TablePost tablePost) {
        //获取表名
        String tableName = "thads:" + tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();

        //获取年份条件
        String region = null;
        //获取城市条件
        String single = null;

        for (Search search : tablePost.getSearches()) {
            String title = search.getTitle();
            if (title.equals("区域统计")) {
                region = search.getValues().get(0);
            } else if (title.equals("独栋比例")) {
                single = search.getValues().get(0);
            }
        }
        Tuple2<Object, List<ArrayList<String>>> Rows = Rows = hBaseDao.getSingle(tableName, region, single, page);

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
        List<Search> search = getSearch(tablePost.getYear(), "独栋比例统计", "按区域统计");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("编号");
        l.add("城市等级");
        l.add("家庭收入");
        l.add("房间数");
        l.add("区域");
        l.add("是否独栋");
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
        List<Search> list = new ArrayList<>();
        try {
            conn = ConnSource.getConnection();
            //从mysql数据库获取报表id
            Integer reportId = mysqlDao.findByColums(new String[]{"name","year","`group`"},new String[]{name,year.toString(),group}).get(0).getId();
            //根据报表id获取搜索表对象
            List<com.tecode.house.jianchenfei.bean.Search> searchs = mysqlDaoSearch.findByColums(new String[]{"reportid"},new String[]{reportId.toString()});
            //根据搜索条件封装Search对象
            for (com.tecode.house.jianchenfei.bean.Search search : searchs) {
                Search s = new Search();
                s.setTitle(search.getName());
                List<String> tableDimension = mysqlDaoDimension.findByColums(new String[]{"groupname"},new String[]{search.getDimgroupname()});
                s.setValues(tableDimension);
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

}
