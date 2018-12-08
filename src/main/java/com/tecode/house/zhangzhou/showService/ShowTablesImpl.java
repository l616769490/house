package com.tecode.house.zhangzhou.showService;

import com.tecode.house.zhangzhou.service.SparkService;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

/**
 * 显示表的类
 */
@Service
public class ShowTablesImpl implements ShowTables{
    /**
     * 显示空置状态的表的方法
     */
    @Override
    public  Table showVacancyTable(TablePost tp) {

        Table table = new Table();
        table.setYear(tp.getYear());
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("AGE1").addTop("VACANCY").addTop("ASSISTED");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        Map<String, Iterable<String>> dataMap = sc.selectVacancyTable("thads:"+tp.getYear(),tp.getSearches().get(0).getValues().get(0));
        Search search = new Search();
        search.setTitle("空置状态");
        showUtil(dataMap,table,page,search);
        for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        return table;
    }

    /**
     * 显示独栋建筑的表的方法
     */
    @Override
    public  Table showSingleBuildingTable(TablePost tp) {
        Table table = new Table();
        Search search2 = new Search();
        if(!search2.getValues().contains(tp.getSearches().get(1).getTitle())){
            search2.setTitle("是否独栋").addValue(tp.getSearches().get(1).getTitle());
        }
        table.addSearchs(search2);
        table.setYear(tp.getYear());
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("BEDRMS").addTop("ROOMS");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        Search search = new Search();
        search.setTitle("城市规模");
        Map<String, Iterable<String>> dataMap = sc.selectSingleBuildTable("thads:"+tp.getYear(),tp.getSearches().get(0).getTitle(),tp.getSearches().get(1).getTitle());
        showUtil(dataMap,table,page,search);
        for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        return table;
    }
    /**
     * 显示房产税相关字段的表的方法
     */
    @Override
    public  Table showHouseDutyTable(TablePost tp) {
        Table table = new Table();
        Search search2 = new Search();
        if(!search2.getValues().contains(tp.getSearches().get(1).getTitle())){
            search2.setTitle("是否独栋").addValue(tp.getSearches().get(1).getTitle());
        }
        table.addSearchs(search2);
        table.setYear(tp.getYear());
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("ZSMHC").addTop("ROOMS").addTop("VALUE");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        Search search = new Search();
        search.setTitle("城市规模");
        Map<String, Iterable<String>> dataMap = sc.selectSingleBuildTable("thads:"+tp.getYear(),tp.getSearches().get(0).getTitle(),tp.getSearches().get(1).getTitle());
        showUtil(dataMap,table,page,search);
        for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        return table;
    }

    /**
     * 私有工具类，供本类中的其他方法调用
     * @param dataMap：数据的Map
     * @param table：表对象
     * @param page：页对象
     * @param search：搜索对象
     */
    private void showUtil(Map<String, Iterable<String>> dataMap,Table table,Page page,Search search) {
        Iterator<Tuple2<String, Iterable<String>>> iterator = dataMap.iterator();
        while (iterator.hasNext()) {
            Tuple2<String, Iterable<String>> tmpIt = iterator.next();
            String tmp = tmpIt._1;
            Iterable<String> stringIterator = tmpIt._2;
            Iterator<String> iterator1 = stringIterator.iterator();
            int size = stringIterator.toList().size();
            int pages = size / 10 + 1;
            System.out.println("pages:" + pages);
            System.out.println("size" + size);
            for (int i = 1; i <= pages; i++) {
                table.setPage(page.addData(i));
            }
            table.addSearchs(search.addValue(tmp));

            while (iterator1.hasNext()) {
                Row r = new Row();
                String next = iterator1.next();
                String[] spls = next.split("_");
                for (String spl : spls) {
                    table.addData(r.addRow(spl));
                }
            }
        }
    }

}
