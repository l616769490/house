package com.tecode.house.lijun.test;

import com.tecode.house.lijun.serivce.impl.*;

import java.io.IOException;

public class Test1 {
    public static void main(String[] args) throws IOException {
      /*  HBaseUtil util = new HBaseUtil();
        util.deleteTable("thads:2011");
              System.out.println(util.isNameSpaceExits("thads"));

        //导入数据
        FilleToHBase fth = new FilleToHBase();
        fth.readFile("D:\\thads2011.txt","thads:2011");
        fth.readFile("D:\\thads2013n.csv","thads:2013");*/



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
        priceFRMAnalysis.analysis("2013");







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