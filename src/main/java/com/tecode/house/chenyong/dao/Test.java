package com.tecode.house.chenyong.dao;

import com.tecode.house.chenyong.dao.impl.ImportIntoDataImpl;

public class Test {
    public static void main(String[] arg) {
//        System.out.println("正在创建表");
//       try {
            ImportIntoData  intoHBase = new ImportIntoDataImpl();
//           intoHBase.createTable("thads:2013");
//           System.out.println("表创建成功");
//       } catch (IOException e) {
//            e.printStackTrace();
//       }
        System.out.println("正在导入数据");
        intoHBase.readData();
        System.out.println("数据导入完成");

    }
}
