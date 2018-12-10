package com.tecode.house.chenyong.service;

import com.tecode.table.*;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/8.
 */
@Service
public class EncapsulationIntoTable {

    GetDataService getDataService = new GetDataService();

    //将获得到的原始数据封装进年龄分布age表对象中
    public Table intoTableAge(TablePost tablePost){
        List<Search> searches = tablePost.getSearches();
        String ageInterval = searches.get(0).getValues().get(0);
        Tuple2<Object, List<ArrayList<String>>> fromHbaseToTableAge = getDataService.getFromHbaseToTableAge(tablePost.getYear().toString(),ageInterval , tablePost.getPage());
        Table table = new Table();
        Page page = new Page();
        Search search = new Search();
        List<Search> seaList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        ageList.add("18以下");
        ageList.add("18-40");
        ageList.add("41-65");
        ageList.add("65以上");
        search.setTitle("年龄区间");
        search.setValues(ageList);
        seaList.add(search);
        //添加当前显示页码
        page.setThisPage(tablePost.getPage());
        //封装页码数
        Integer count = (Integer) fromHbaseToTableAge._1();
        List<Integer> pages = new ArrayList<>();
        if( tablePost.getPage() >= 1 && tablePost.getPage() <= 3){
            for(int j = 1; j <= 5; j ++){
                pages.add(j);
            }
        }else if (tablePost.getPage() >= ((count/20) + 1)){
            for(int i = ((count/20) + 1)-4; i <= (count/20) + 1;i ++){
                pages.add(i);
            }
        }else if ((tablePost.getPage() <= ((count/20) + 1) - 2) && (tablePost.getPage() >= ((count/20) + 1) - 5) ){
            for(int l = ((count/20) + 1)-4; l <= (count/20) + 1;l ++){
                pages.add(l);
            }
        }else {
            for (int k = tablePost.getPage()-2; k <= tablePost.getPage()+2; k++){
                pages.add(k);
            }
        }
        page.setData(pages);
        table.setYear(tablePost.getYear());
        table.setPage(page);
        table.setSearch(tablePost.getSearches());
        List<String> top = new ArrayList<>();
        List<Row> data = new ArrayList<>();
        for (ArrayList<String> strings : fromHbaseToTableAge._2()) {
            List<String> line = new ArrayList<>();
            //创建row对象
            Row row = new Row();
            for (String string : strings) {
                //将查询的多列的值添加对应的列
                line.add(string);
            }
            //将对应查询多列添加进一行
            row.setRow(line);
            //将查询显示的多行添加进集合
            data.add(row);
        }
        top.add("房屋编号(control)");
        top.add("建筑类型(type)");
        top.add("家庭收入(zinc2)");
        top.add("每月房屋费用(zsmhc)");
        top.add("户主年龄(age1)");
        table.setTop(top);
        table.setData(data);
        table.setSearch(seaList);
        return table;
    }
    //将获得到的原始数据封装进通过年龄查询房间数和卧室数表对象中
    public Table intoTableRooms(TablePost tablePost){
        List<Search> searches = tablePost.getSearches();
        String ageInterval = searches.get(0).getValues().get(0);
        Tuple2<Object, List<ArrayList<String>>> fromHbaseToTableRooms = getDataService.getFromHbaseToTableRooms(tablePost.getYear().toString(), ageInterval, tablePost.getPage());
        Table table = new Table();
        Page page = new Page();
        Search search = new Search();
        List<Search> seaList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        ageList.add("18以下");
        ageList.add("18-40");
        ageList.add("41-65");
        ageList.add("65以上");
        search.setTitle("年龄区间");
        search.setValues(ageList);
        seaList.add(search);
        //添加当前显示页码
        page.setThisPage(tablePost.getPage());
        //封装页码数
        Integer count = (Integer) fromHbaseToTableRooms._1();
        List<Integer> pages = new ArrayList<>();
        if( tablePost.getPage() >= 1 && tablePost.getPage() <= 3){
            for(int j = 1; j <= 5; j ++){
                pages.add(j);
            }
        }else if (tablePost.getPage() >= ((count/20) + 1)){
            for(int i = ((count/20) + 1)-4; i <= (count/20) + 1;i ++){
                pages.add(i);
            }
        }else if ((tablePost.getPage() <= ((count/20) + 1) - 2) && (tablePost.getPage() >= ((count/20) + 1) - 5) ){
            for(int l = ((count/20) + 1)-4; l <= (count/20) + 1;l ++){
                pages.add(l);
            }
        }else {
            for (int k = tablePost.getPage()-2; k <= tablePost.getPage()+2; k++){
                pages.add(k);
            }
        }
        page.setData(pages);
        table.setYear(tablePost.getYear());
        table.setPage(page);
        table.setSearch(tablePost.getSearches());
        List<String> top = new ArrayList<>();
        List<Row> data = new ArrayList<>();
        for (ArrayList<String> strings : fromHbaseToTableRooms._2()) {
            List<String> line =  new ArrayList<>();
            //创建row对象
            Row row = new Row();
            for (String string : strings) {
                //将查询的多列的值添加对应的列
                line.add(string);
            }
            //将对应查询多列添加进一行
            row.setRow(line);
            //将查询显示的多行添加进集合
            data.add(row);
        }
        top.add("房屋编号(control)");
        top.add("户主年龄(age1)");
        top.add("市场价(value)");
        top.add("总房间数(rooms)");
        top.add("卧室数(bedrms)");
        top.add("家庭人数(per)");
        table.setTop(top);
        table.setData(data);
        table.setSearch(seaList);
        return table;
    }
    //将获得到的原始数据封装进通过年龄查询水电费表对象中
    public Table intoTableUtility(TablePost tablePost){
        List<Search> searches = tablePost.getSearches();
        String ageInterval = searches.get(0).getValues().get(0);
        Tuple2<Object, List<ArrayList<String>>> fromHbaseToTableUtility = getDataService.getFromHbaseToTableUtility(tablePost.getYear().toString(), ageInterval, tablePost.getPage());
        Table table = new Table();
        Page page = new Page();
        //添加搜索对象并封装
        Search search = new Search();
        List<Search> seaList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        ageList.add("18以下");
        ageList.add("18-40");
        ageList.add("41-65");
        ageList.add("65以上");
        search.setTitle("年龄区间");
        search.setValues(ageList);
        seaList.add(search);
        //添加当前显示页码
        page.setThisPage(tablePost.getPage());
        //封装页码数
        Integer count = (Integer) fromHbaseToTableUtility._1();
        List<Integer> pages = new ArrayList<>();
        if( tablePost.getPage() >= 1 && tablePost.getPage() <= 3){
            for(int j = 1; j <= 5; j ++){
                pages.add(j);
            }
        }else if (tablePost.getPage() >= ((count/20) + 1)){
            for(int i = ((count/20) + 1)-4; i <= (count/20) + 1;i ++){
                pages.add(i);
            }
        }else if ((tablePost.getPage() <= ((count/20) + 1) - 2) && (tablePost.getPage() >= ((count/20) + 1) - 5) ){
            for(int l = ((count/20) + 1)-4; l <= (count/20) + 1;l ++){
                pages.add(l);
            }
        }else {
            for (int k = tablePost.getPage()-2; k <= tablePost.getPage()+2; k++){
                pages.add(k);
            }
        }
        page.setData(pages);
        //向table对象中封装所需要的数据
        table.setYear(tablePost.getYear());
        table.setPage(page);
        table.setSearch(tablePost.getSearches());
        //设置top属性
        List<String> top = new ArrayList<>();
        top.add("房屋编号(control)");
        top.add("户主年龄(age1)");
        top.add("每月水电费(utility)");
        top.add("家庭人数(per)");
        //添加数据
        List<Row> data = new ArrayList<>();
        for (ArrayList<String> strings : fromHbaseToTableUtility._2()) {
            List<String> line = new ArrayList<>();
            //创建row对象
            Row row = new Row();
            for (String string : strings) {
                //添加一条多个列的数据
                line.add(string);
            }
            //将查询的多列数据添加进一行
            row.setRow(line);
            //将多行数据添加进显示当前页中
            data.add(row);
        }
        table.setTop(top);
        table.setData(data);
        table.setSearch(seaList);
        return table;
    }

}
