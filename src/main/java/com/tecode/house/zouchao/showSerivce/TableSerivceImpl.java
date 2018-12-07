package com.tecode.house.zouchao.showSerivce;

import com.tecode.house.zouchao.bean.Report;
import com.tecode.house.zouchao.dao.HBaseDao;
import com.tecode.house.zouchao.dao.MySQLDao;
import com.tecode.house.zouchao.dao.impl.HBaseDaoImpl;
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl;
import com.tecode.house.zouchao.util.MySQLUtil;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TableSerivceImpl implements TableSerivce {

   private HBaseDao hBaseDao = new HBaseDaoImpl();
   private MySQLDao mySQLDao = new MySQLDaoImpl();

    @Override
    public Table getTableForRent(TablePost tablePost) {
        //构建HBAse表名
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
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getForRent(tableName, filter, page);
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
        se.setTitle("公平市场租金区间");
        List<String> va = new ArrayList<>();
        va.add("0-1000");
        va.add("1000-1500");
        va.add("1500-2000");
        va.add("2000-2500");
        va.add("2500-3000");
        va.add("3000+");
        se.setValues(va);
        List<Search> ls = new ArrayList<>();
        ls.add(se);
        table.setSearch(ls);
        List<String> l = new ArrayList<>();
        l.add("公平市场租金");
        l.add("城市规模");
        l.add("人口普查区域");
        l.add("建筑类型");
        l.add("房间数");
        l.add("卧室数");
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
    public Table getTableForPrice(TablePost tablePost) {
        //获取表名
        String tableName = "thads:" + tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();

        //获取年份条件
        String build = null;
        //获取城市条件
        String city = null;

        for (Search search : tablePost.getSearches()) {
            String title = search.getTitle();
            if (title.equals("建成年份")) {
                build = search.getValues().get(0);
            } else if (title.equals("城市规模")) {
                city = search.getValues().get(0);
            }

        }
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getForValue(tableName, build, city, page);


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
        List<Search> search = getSearch(tablePost.getYear(), "价格统计", "年份统计");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("建成年份");
        l.add("公平市场租金");
        l.add("市场价");
        l.add("城市规模");
        l.add("人口普查区域");
        l.add("建筑类型");
        l.add("房间数");
        l.add("卧室数");
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
    public Table getTableForRoom(TablePost tablePost) {
        //获取表名
        String tableName = "thads:" + tablePost.getYear().toString();
        //获取页码
        int page = tablePost.getPage();

        //获取年份条件
        String build = null;
        //获取城市条件
        String city = null;

        for (Search search : tablePost.getSearches()) {
            String title = search.getTitle();
            if (title.equals("建成年份")) {
                build = search.getValues().get(0);
            } else if (title.equals("城市规模")) {
                city = search.getValues().get(0);
            }
        }
        Tuple2<Object, List<ArrayList<String>>> Rows = Rows = hBaseDao.getForRom(tableName, build, city, page);

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
        List<Search> search = getSearch(tablePost.getYear(), "房间数统计", "年份统计");
        table.setSearch(search);
        List<String> l = new ArrayList<>();
        l.add("建成年份");
        l.add("房间数");
        l.add("卧室数");
        l.add("城市规模");
        l.add("人口普查区域");
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
    public List<Search> getSearch(Integer year, String name, String group) {
        Connection conn = null;
        List<Search> list = new ArrayList<>();
        try {
            conn = MySQLUtil.getConn();
            //从mysql数据库获取报表id
            int reportId = mySQLDao.getByTableReport(conn, name, year, group).getId();
            //根据报表id获取搜索表对象
            List<com.tecode.house.zouchao.bean.Search> searchs = mySQLDao.getByTableSearch(conn, reportId);
            //根据搜索条件封装Search对象
            for (com.tecode.house.zouchao.bean.Search search : searchs) {
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
