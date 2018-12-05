package com.tecode.house.zouchao.showSerivce;

import com.tecode.house.zouchao.dao.HBaseDao;
import com.tecode.house.zouchao.dao.impl.HBaseDaoImpl;
import com.tecode.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

@Service
public class TableSerivceImpl implements TableSerivce {

    HBaseDao hBaseDao = new HBaseDaoImpl();

    @Override
    public Table getTable(TablePost tablePost) {
        //构建HBAse表名
        String tableName = "thads:" + tablePost.getYear().toString();
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
    public Table getTable(Integer page, Integer year) {
        String tableName = "thads:" + year.toString();

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
}
