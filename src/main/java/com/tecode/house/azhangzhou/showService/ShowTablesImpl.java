package com.tecode.house.azhangzhou.showService;

import com.tecode.house.azhangzhou.service.SparkService;
import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple6;
import scala.Tuple7;

import java.util.ArrayList;
import java.util.List;

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
        List<Tuple6<String, String, String, String, String, String>> tuple6s = sc.selectVacancyTable("thads:" + tp.getYear(), tp.getSearches().get(0).getValues().get(0), tp.getPage());
        table.setPage(page);
        table.setSearch(tp.getSearches());

        for (Tuple6<String, String, String, String, String, String> tuple6 : tuple6s) {
            if(!tuple6._1().equals("1")){
                Row r = new Row();
                if (tuple6._5().equals("-6")) {
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow(tuple6._4()).addRow("居住").addRow(tuple6._6());
                } else {
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow(tuple6._4()).addRow("空置").addRow(tuple6._6());
                }
                table.addData(r);
            }else {
                List<Integer> pageList = new ArrayList<>();
                pageList.add(Integer.parseInt(tuple6._1()));
                pageList.add(Integer.parseInt(tuple6._2()));
                pageList.add(Integer.parseInt(tuple6._4()));
                pageList.add(Integer.parseInt(tuple6._6()));
                page.setData(pageList);
            }
        }
        table.setPage(page);
       /* for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
        //System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        //table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        table.getData().forEach(e-> System.out.println(e.getRow()));*/
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
            search2.setTitle("建筑结构类型").addValue(tp.getSearches().get(1).getTitle());
        }
        table.setSearch(tp.getSearches());
        table.setYear(tp.getYear());
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("BEDRMS").addTop("ROOMS");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        List<Tuple6<String, String, String, String, String, String>> tuple6s = sc.selectSingleBuildTable("thads:" + tp.getYear(), tp.getSearches().get(0).getValues().get(1), tp.getSearches().get(1).getValues().get(0), tp.getPage());
        for (Tuple6<String, String, String, String, String, String> tuple6 : tuple6s) {
            if(!tuple6._1().equals("1")){
                Row r = new Row();
                if(tuple6._4().equals("1")){
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow("独栋").addRow(tuple6._5()).addRow(tuple6._6());
                }else{
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow("其他").addRow(tuple6._5()).addRow(tuple6._6());
                }
                table.addData(r);
            }else {
                List<Integer> pageList = new ArrayList<>();
                pageList.add(Integer.parseInt(tuple6._1()));
                pageList.add(Integer.parseInt(tuple6._2()));
                pageList.add(Integer.parseInt(tuple6._4()));
                pageList.add(Integer.parseInt(tuple6._6()));
                page.setData(pageList);
            }

        }
        table.setPage(page);
        /*for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }
        table.setPage(page);
        System.out.println("查询年份："+table.getYear());
        System.out.println("当前页："+table.getPage().getThisPage());
       // System.out.println("所有页："+table.getPage().getData());
        System.out.println(table.getTop());
        table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));*/
        return table;
    }
    /**
     * 显示房产税相关字段的表的方法
     */
    @Override
    public  Table showHouseDutyTable(TablePost tp) {
        Table table = new Table();
        table.setSearch(tp.getSearches());
        table.setYear(tp.getYear());
        table.addTop("CONTROL").addTop("METRO3").addTop("BUILT").addTop("STRUCTURETYPE").addTop("ZSMHC").addTop("ROOMS").addTop("VALUE");
        Page page = new Page();
        page.setThisPage(tp.getPage());

        table.setPage(page);

        SparkService sc = new SparkService();
        List<Tuple7<String, String, String, String, String, String, String>> tuple7s = sc.selectHouseDutyTable("thads:" + tp.getYear(), tp.getSearches().get(0).getValues().get(1), tp.getSearches().get(1).getValues().get(0), tp.getPage());
        for (Tuple7<String, String, String, String, String, String, String> tuple7 : tuple7s) {
            if(!tuple7._1().equals("1")){
                Row r = new Row();
                if(tuple7._4().equals("1")){
                    r.addRow(tuple7._1()).addRow(tuple7._2()).addRow(tuple7._3()).addRow("独栋").addRow(tuple7._5()).addRow(tuple7._6()).addRow(tuple7._7());
                }else{
                    r.addRow(tuple7._1()).addRow(tuple7._2()).addRow(tuple7._3()).addRow("其他").addRow(tuple7._5()).addRow(tuple7._6()).addRow(tuple7._7());
                }
                table.addData(r);
            }else{
                List<Integer> pageList = new ArrayList<>();
                pageList.add(Integer.parseInt(tuple7._1()));
                pageList.add(Integer.parseInt(tuple7._2()));
                pageList.add(Integer.parseInt(tuple7._4()));
                pageList.add(Integer.parseInt(tuple7._7()));
                page.setData(pageList);
            }
        }
        table.setPage(page);

        /*for (Search s : table.getSearch()) {
            System.out.println(s.getTitle());
        }*/
      //  System.out.println("查询年份："+table.getYear());
        //System.out.println("当前页："+table.getPage().getThisPage());
        //System.out.println("所有页："+table.getPage().getData());
       // System.out.println(table.getTop());
       // table.getData().subList(0,10).forEach(e-> System.out.println(e.getRow()));
        return table;
    }
}
