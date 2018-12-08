package com.tecode.house.zouchao.showSerivce;

import com.tecode.house.zouchao.dao.HBaseDao;
import com.tecode.house.zouchao.dao.impl.HBaseDaoImpl;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableSerivceImpl implements TableSerivce {

    HBaseDao hBaseDao = new HBaseDaoImpl();

    @Override
    public Table getTableForRent(TablePost tablePost) {
        //构建HBAse表名
        String tableName =  tablePost.getYear().toString();
        //获取搜索条件
        String filter = tablePost.getSearches().get(0).getValues().get(0);
        //获取要查询的页码
        int page = tablePost.getPage();
        //获取数据
        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getForRent(tableName, filter, page);
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
    public Table getTableForRent(Integer page, Integer year) {
        String tableName =  year.toString();

        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getAllForRent(tableName, page);
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
        String tableName =  tablePost.getYear().toString();
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
        Tuple2<Object, List<ArrayList<String>>> Rows = null;
        //调用有搜索条件的方法获取数据
        if (build != null && city != null) {
            Rows = hBaseDao.getForValue(tableName, build, city, page);
        } else if (build == null && city != null) {
            Rows = hBaseDao.getForValueByCity(tableName, city, page);
        } else if (build != null && city == null) {
            Rows = hBaseDao.getForValueByBuild(tableName, build, page);
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
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("建成年份");
        List<String> va = new ArrayList<>();
        va.add("1900-1940");
        va.add("1940-1960");
        va.add("1960-1980");
        va.add("1980-2000");
        va.add("2000+");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("城市规模");
        List<String> va1 = new ArrayList<>();
        va1.add("一线城市");
        va1.add("二线城市");
        va1.add("三线城市");
        va1.add("四线城市");
        va1.add("五线城市");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        ls.add(se1);
        ls.add(se2);
        table.setSearch(ls);
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
    public Table getTableForPrice(Integer page, Integer year) {
        String tableName =  year.toString();

        Tuple2<Object, List<ArrayList<String>>> Rows = hBaseDao.getAllForValue(tableName, page);
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
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("建成年份");
        List<String> va = new ArrayList<>();
        va.add("1900-1940");
        va.add("1940-1960");
        va.add("1960-1980");
        va.add("1980-2000");
        va.add("2000+");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("城市规模");
        List<String> va1 = new ArrayList<>();
        va1.add("一线城市");
        va1.add("二线城市");
        va1.add("三线城市");
        va1.add("四线城市");
        va1.add("五线城市");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        ls.add(se1);
        ls.add(se2);
        table.setSearch(ls);
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
        String tableName =  tablePost.getYear().toString();
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
        Tuple2<Object, List<ArrayList<String>>> Rows = null;
        //调用有搜索条件的方法获取数据
        if (build != null && city != null) {
            Rows = hBaseDao.getForRom(tableName, build, city, page);
        } else if (build == null && city != null) {
            Rows = hBaseDao.getForRomByCity(tableName, city, page);
        } else if (build != null && city == null) {
            Rows = hBaseDao.getForRomByBuild(tableName, build, page);
        }else{
            Rows = hBaseDao.getAllForRom(tableName,page);
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
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("建成年份");
        List<String> va = new ArrayList<>();
        va.add("1900-1940");
        va.add("1940-1960");
        va.add("1960-1980");
        va.add("1980-2000");
        va.add("2000+");
        se1.setValues(va);
        //构建    城市规模Search对象
        Search se2 = new Search();
        se2.setTitle("城市规模");
        List<String> va1 = new ArrayList<>();
        va1.add("一线城市");
        va1.add("二线城市");
        va1.add("三线城市");
        va1.add("四线城市");
        va1.add("五线城市");
        se2.setValues(va1);
        List<Search> ls = new ArrayList<>();
        ls.add(se1);
        ls.add(se2);
        table.setSearch(ls);
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

}
