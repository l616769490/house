package com.tecode.house.zouchao.test;

import com.tecode.house.zouchao.dao.impl.HBaseDao;
import com.tecode.house.zouchao.dao.impl.HBaseScalaDaoImpl;
import com.tecode.house.zouchao.hbase.FilleToHBase;
import com.tecode.house.zouchao.serivce.impl.RentAnalysis;
import com.tecode.house.zouchao.util.HBaseUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        //HBaseUtil util = new HBaseUtil();
        //util.deleteTable("thads:2013");
        //      System.out.println(util.isNameSpaceExits("thads"));
        //导入数据
        //FilleToHBase fth = new FilleToHBase();
        //fth.readFile("D:\\thads2011.txt","thads:2011");
        //RentAnalysis rentAnalysis = new RentAnalysis();
        //rentAnalysis.analysis("thads:2013");
        HBaseDao hb = new HBaseDao();
        hb.get("thads:2013");
    }
}