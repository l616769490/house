package com.tecode.house.zhangzhou.showService;

import com.tecode.house.zhangzhou.service.SparkService;
import com.tecode.table.Page;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import scala.Tuple2;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.immutable.Map;

/**
 * 显示表的类
 */
public class ShowTables {
    /**
     * 显示空置状态的表的方法
     * @param year：年份
     * @param nowPage：当前页
     * @param choice：查询条件
     */
    public  void showVacancyTable(int year,int nowPage,String choice) {
        Table table = new Table();
        table.setYear(year);
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("AGE1").addTop("VACANCY").addTop("ASSISTED");
        Page page = new Page();
        page.setThisPage(nowPage);
        SparkService sc = new SparkService();
        Map<String, Iterable<String>> dataMap = sc.selectVacancyTable("thads:"+year,choice);
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
    }

    /**
     * 显示独栋建筑的表的方法
     * @param year：年份
     * @param nowPage：当前页
     * @param choice：查询条件1
     * @param cho2：查询条件2
     */
    public  void showSingleBuildingTable(int year,int nowPage,String choice,String cho2) {
        Table table = new Table();
        Search search2 = new Search();
        if(!search2.getValues().contains(cho2)){
            search2.setTitle("是否独栋").addValue(cho2);
        }
        table.addSearchs(search2);
        table.setYear(year);
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("BEDRMS").addTop("ROOMS");
        Page page = new Page();
        page.setThisPage(nowPage);
        SparkService sc = new SparkService();
        Search search = new Search();
        search.setTitle("城市规模");
        Map<String, Iterable<String>> dataMap = sc.selectSingleBuildTable("thads:"+year,choice,cho2);
        showUtil(dataMap,table,page,search);
        for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
    }
    /**
     * 显示房产税相关字段的表的方法
     * @param year：年份
     * @param nowPage：当前页
     * @param choice：查询条件1
     * @param cho2：查询条件2
     */
    public  void showHouseDutyTable(int year,int nowPage,String choice,String cho2) {
        Table table = new Table();
        Search search2 = new Search();
        if(!search2.getValues().contains(cho2)){
            search2.setTitle("是否独栋").addValue(cho2);
        }
        table.addSearchs(search2);
        table.setYear(year);
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("ZSMHC").addTop("ROOMS").addTop("VALUE");
        Page page = new Page();
        page.setThisPage(nowPage);
        SparkService sc = new SparkService();
        Search search = new Search();
        search.setTitle("城市规模");
        Map<String, Iterable<String>> dataMap = sc.selectSingleBuildTable("thads:"+year,choice,cho2);
        showUtil(dataMap,table,page,search);
        for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
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
