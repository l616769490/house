package com.tecode.house.zouchao.test;

import com.tecode.house.zouchao.hbase.FilleToHBase;
import com.tecode.house.zouchao.serivce.impl.PriceByBuildAnalysis;
import com.tecode.house.zouchao.serivce.impl.RoomsByBuildAnalysis;
import com.tecode.house.zouchao.util.HBaseUtil;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //HBaseUtil util = new HBaseUtil();
        //util.deleteTable("thads:2011");
        //      System.out.println(util.isNameSpaceExits("thads"));
        //导入数据
        FilleToHBase fth = new FilleToHBase();
        //fth.readFile("D:\\thads2011.txt","thads:2011");
        fth.readFile("D:\\thads2013n.csv","thads:2013");
        //RentAnalysis rentAnalysis = new RentAnalysis();
        //rentAnalysis.analysis("thads:2013");
        //HBaseDao hb = new HBaseDao();
        //hb.get("thads:2013");
        //PriceByBuildAnalysis rentByBuildAnalysis = new PriceByBuildAnalysis();
        //System.out.println(rentByBuildAnalysis.analysis("thads:2013"));
        //RoomsByBuildAnalysis roomsByBuildAnalysis = new RoomsByBuildAnalysis();
        //System.out.println(roomsByBuildAnalysis.analysis("thads:2013"));
    }
}