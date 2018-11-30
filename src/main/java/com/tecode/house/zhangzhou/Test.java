package com.tecode.house.zhangzhou;

import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;

public class Test {
    public static void main(String[] args) {
        Long begin = System.currentTimeMillis();
        System.out.println("正在导入....");
        HBaseDao hBaseDao = new HBaseDaoImpl();
        String path = "e:/american/thads2011.txt";
        hBaseDao.insert("thads:2011",path);
        //hBaseDao.create("thads:2011");
        Long end = System.currentTimeMillis();
        System.out.println("导入成功。");
        System.out.println("花费时间："+(end-begin)/1000);
    }
}
