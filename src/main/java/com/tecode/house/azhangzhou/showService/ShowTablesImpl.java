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
        table.addTop("房屋编号").addTop("城市规模").addTop("建成年份").addTop("户主年龄").addTop("空置状态").addTop("是否辅助用房");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        List<Tuple6<String, String, String, String, String, String>> tuple6s=null;
        if(tp.getSearches().size()==0){
            tuple6s= sc.selectVacancyTable("thads:" + tp.getYear(), "-1", tp.getPage());
        }else {
            tuple6s= sc.selectVacancyTable("thads:" + tp.getYear(), tp.getSearches().get(0).getValues().get(0), tp.getPage());
        }
        List<Search> list = new ArrayList<>();
        Search search= new Search();
        search.setTitle("空置状态");
        search.addValue("空置").addValue("居住");
        list.add(search);
        table.setPage(page);
        table.setSearch(list);


        for (Tuple6<String, String, String, String, String, String> tuple6 : tuple6s) {
            if(!tuple6._1().equals("1")){
                Row r = new Row();
                if (tuple6._5().equals("-6")) {
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow(tuple6._4()).addRow("居住").addRow(tuple6._6());
                } else {
                    if(!tuple6._5().equals("")){
                        r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow(tuple6._4()).addRow("空置").addRow(tuple6._6());
                    }
                }
                table.addData(r);
            }else {
                List<Integer> pageList = getPageList(tp.getPage(), Integer.parseInt(tuple6._6()));
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

    private List<Integer> getPageList(int nowPage,int endPage){
        List<Integer> pageList = new ArrayList<>();
        if(endPage<=5){
            for(int i=1;i<=endPage;i++){
                pageList.add(i);
            }
        }else {
            if(nowPage<=3){
                for(int i=1;i<=nowPage+1;i++){
                    pageList.add(i);
                }
                pageList.add(endPage-1);
                pageList.add(endPage);
            }else if(nowPage<=endPage-2){
                pageList.add(1);
                pageList.add(nowPage-1);
                pageList.add(nowPage);
                pageList.add(nowPage+1);
                pageList.add(endPage);
            }else if(nowPage==endPage-1){
                pageList.add(1);
                pageList.add(2);
                pageList.add(nowPage-1);
                pageList.add(nowPage);
                pageList.add(endPage);
            }else {
                pageList.add(1);
                pageList.add(2);
                pageList.add(3);
                pageList.add(endPage-1);
                pageList.add(endPage);
            }
        }
        return pageList;
    }
    /**
     * 显示独栋建筑的表的方法
     */
    @Override
    public  Table showSingleBuildingTable(TablePost tp) {
        Table table = new Table();
        List<Search> list = new ArrayList<>();
        Search search1= new Search();
        search1.setTitle("城市规模");
        search1.addValue("1").addValue("2").addValue("3").addValue("4").addValue("5");
        list.add(search1);
        Search search2= new Search();
        search2.setTitle("建筑结构类型");
        search2.addValue("独栋").addValue("其他");
        list.add(search2);
        table.setSearch(list);
        table.setYear(tp.getYear());
        table.addTop("房屋编号").addTop("城市规模").addTop("建成年份").addTop("建筑结构类型").addTop("卧室数").addTop("房间数");
        Page page = new Page();
        page.setThisPage(tp.getPage());
        SparkService sc = new SparkService();
        String s1 ="-1";
        String s2 = "-1";
        for (Search search : tp.getSearches()) {
            if(search.getTitle().equals("城市规模")){
                s1 = search.getValues().get(0);
            }else if(search.getTitle().equals("建筑结构类型")){
                s2 = search.getValues().get(0);
            }
        }
        List<Tuple6<String, String, String, String, String, String>> tuple6s = sc.selectSingleBuildTable("thads:" + tp.getYear(), s1,s2, tp.getPage());
        for (Tuple6<String, String, String, String, String, String> tuple6 : tuple6s) {
            if(!tuple6._1().equals("1")){
                Row r = new Row();
                if(tuple6._4().equals("1")){
                    r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow("独栋").addRow(tuple6._5()).addRow(tuple6._6());
                }else{
                    if(!tuple6._4().equals("")){
                        r.addRow(tuple6._1()).addRow(tuple6._2()).addRow(tuple6._3()).addRow("其他").addRow(tuple6._5()).addRow(tuple6._6());
                    }
                }
                table.addData(r);
            }else {
                List<Integer> pageList = getPageList(tp.getPage(), Integer.parseInt(tuple6._6()));
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
        List<Search> list = new ArrayList<>();
        Search search1= new Search();
        search1.setTitle("城市规模");
        search1.addValue("1").addValue("2").addValue("3").addValue("4").addValue("5");
        list.add(search1);
        Search search2= new Search();
        search2.setTitle("建筑结构类型");
        search2.addValue("独栋").addValue("其他");
        list.add(search2);
        table.setSearch(list);
        table.setYear(tp.getYear());
        table.addTop("房屋编号").addTop("城市规模").addTop("建成年份").addTop("建筑结构类型").addTop("房产税").addTop("房间数").addTop("市场价");
        Page page = new Page();
        page.setThisPage(tp.getPage());

        table.setPage(page);

        SparkService sc = new SparkService();
        String s1 ="-1";
        String s2 = "-1";
        for (Search search : tp.getSearches()) {
            if(search.getTitle().equals("城市规模")){
                s1 = search.getValues().get(0);
            }else if(search.getTitle().equals("建筑结构类型")){
                s2 = search.getValues().get(0);
            }
        }
        List<Tuple7<String, String, String, String, String, String, String>> tuple7s = sc.selectHouseDutyTable("thads:" + tp.getYear(), s1, s2, tp.getPage());
        for (Tuple7<String, String, String, String, String, String, String> tuple7 : tuple7s) {
            if(!tuple7._1().equals("1")){
                Row r = new Row();
                if(tuple7._4().equals("1")){
                    r.addRow(tuple7._1()).addRow(tuple7._2()).addRow(tuple7._3()).addRow("独栋").addRow(tuple7._5()).addRow(tuple7._6()).addRow(tuple7._7());
                }else{
                    if(!tuple7._4().equals("")){
                        r.addRow(tuple7._1()).addRow(tuple7._2()).addRow(tuple7._3()).addRow("其他").addRow(tuple7._5()).addRow(tuple7._6()).addRow(tuple7._7());
                    }
                }
                table.addData(r);
            }else {
                List<Integer> pageList = getPageList(tp.getPage(), Integer.parseInt(tuple7._7()));
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
