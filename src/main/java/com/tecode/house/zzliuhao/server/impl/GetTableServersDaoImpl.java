package com.tecode.house.zzliuhao.server.impl;

import com.tecode.house.zzliuhao.dao.ReadHbaseDao;
import com.tecode.house.zzliuhao.dao.impl.ReadHbaseDaoImpl;
import com.tecode.house.zzliuhao.server.getTableServersDao;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/7.
 */
@Service
public class GetTableServersDaoImpl implements getTableServersDao {
    /**
     *
     * @param tablePost
     * @return
     */
    @Override
    public Table getCityTax(TablePost tablePost) {
        //获取表名
        String tablename = tablePost.getYear().toString();
        //获取搜索条件
        String search = tablePost.getSearches().get(0).getValues().get(0);
        //获得要查看的页码
        Integer page = tablePost.getPage();
        //调用方法获得数据
        ReadHbaseDao read = new ReadHbaseDaoImpl();
        Tuple2<Integer, List<ArrayList<String>>> data = read.readCityTaxData(tablename, page, search);
        int i = data._1;
        //封装页面列表
        int pa = i/10 +1;
        List<Integer> list = new ArrayList<>();
        for (int j = 1; j <= pa; j++) {
            list.add(j);
        }
        Table table = new Table();
        //封装年份
        table.setYear(tablePost.getYear());
        //否建page对象
        Page p = new Page();
        p.setThisPage(tablePost.getPage());
        p.setData(list);

        //构建search对象
        Search s = new Search();
        s.setTitle("各线城市的税务表");
        List<String> l = new ArrayList<>();
        l.add("一线城市");
        l.add("二线城市");
        l.add("三线城市");
        l.add("四线城市");
        l.add("五线城市");
        s.setValues(l);

        List<String> lz = new ArrayList<>();
        lz.add("城市规模");
        lz.add("房屋费用");
        lz.add("水电费用");
        lz.add("其他费用");
        table.setTop(lz);
        table.setPage(p);
        List<Row> row = new ArrayList<>();
        for (ArrayList<String> rowdata : data._2()) {
            Row r = new Row();
            r.setRow(rowdata);
            row.add(r);
        }
        table.setData(row);
        return table;
    }

    @Override
    public Table getStructureType(TablePost tablePost) {
        //获取表名
        String tablename = tablePost.getYear().toString();
        //获取搜索条件
        String search = tablePost.getSearches().get(0).getValues().get(0);
        //获得要查看的页码
        Integer page = tablePost.getPage();
        //调用方法获得数据
        String arr = "";
        ReadHbaseDao read = new ReadHbaseDaoImpl();
        read.readData(tablename,page,arr);
        Tuple2<Integer, List<ArrayList<String>>> data = read.readCityTaxData(tablename, page, search);
        int i = data._1;
        //封装页面列表
        int pa = i/10 +1;
        List<Integer> list = new ArrayList<>();
        for (int j = 1; j <= pa; j++) {
            list.add(j);
        }
        Table table = new Table();
        //封装年份
        table.setYear(tablePost.getYear());
        //否建page对象
        Page p = new Page();
        p.setThisPage(tablePost.getPage());
        p.setData(list);
        //构建search对象
        Search s = new Search();
        s.setTitle("美国建筑结构类型统计");
        List<String> l = new ArrayList<>();
        l.add("建筑类型");

        s.setValues(l);

        List<String> lz = new ArrayList<>();
        lz.add("建筑结构类型");
        lz.add("城市等级");
        lz.add("房屋费用");
        table.setTop(lz);


        List<Row> row = new ArrayList<>();
        for (ArrayList<String> rowdata : data._2()) {
            Row r = new Row();
            r.setRow(rowdata);
            row.add(r);
        }
        table.setData(row);
        table.setPage(p);
        return table;

    }
}
