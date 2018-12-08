package com.tecode.house.lijun.test;

import com.tecode.house.lijun.controller.TController;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test1 {
    public static void main(String[] args) throws IOException {
      /*  HBaseUtil util = new HBaseUtil();
        util.deleteTable("thads:2011");
              System.out.println(util.isNameSpaceExits("thads"));

        //导入数据
        FilleToHBase fth = new FilleToHBase();
        fth.readFile("D:\\thads2011.txt","thads:2011");
        fth.readFile("D:\\thads2013n.csv","thads:2013");*/

/*
     //分析房屋费用
        ZSMHCAnalysis Analysis = new ZSMHCAnalysis();
        Analysis.analysis("2013");
        System.out.println("1");

        //分析水电
        UTILITYAnalysis utilityAnalysis =new UTILITYAnalysis();
        utilityAnalysis.analysis("2013");
        System.out.println("2");

        //分析其他费用
        OTHERCOSTAnalysis othercostAnalysis =new OTHERCOSTAnalysis();
        othercostAnalysis.analysis("2013");
        System.out.println("3");

        //房屋总费用
        TotalCostAnalysis totalCostAnalysis=new TotalCostAnalysis();
        totalCostAnalysis.analysis("2013");

        //家庭收入
        ZINC2Analysis zinc2Analysis =new ZINC2Analysis();
        zinc2Analysis.analysis("2013");

        //房屋价格
        PriceValueAnalysis priceValueAnalysis =new PriceValueAnalysis();
        priceValueAnalysis.analysis("2013");

        //住房租金
        PriceFRMAnalysis priceFRMAnalysis =new PriceFRMAnalysis();
        priceFRMAnalysis.analysis("2013");*/


        TController t = new TController();
        TablePost tp = new TablePost();
        tp.setPage(6);
        tp.setYear(2013);
        //构建    建成年份Search对象
        Search se1 = new Search();
        se1.setTitle("房屋费用");
        List<String> va = new ArrayList<>();
        va.add("1900-1940");
        se1.setValues(va);
        List<Search> ls = new ArrayList<>();
        Search se2 = new Search();
        se2.addValue("");
        //ls.add(se1);
        ls.add(se2);
        tp.setSearches(ls);
        Table table = t.cost(tp);
        System.out.println("year:   "+table.getYear());
        System.out.println("this:   "+table.getPage().getThisPage());
        System.out.println("page:   "+table.getPage().getData().toString());
        for (Search search : table.getSearch()) {
            System.out.println("search: "+search.getValues().toString());
        }
        System.out.println("top:    "+table.getTop().toString());

        for (Row datum : table.getData()) {
            System.out.println("data:   "+datum.getRow().toString());
        }























      /*  try {
            Connection conn = MySQLUtil.getConn();
            MySQLDaoImpl dao = new MySQLDaoImpl();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/




    }
}