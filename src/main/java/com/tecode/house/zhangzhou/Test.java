package com.tecode.house.zhangzhou;

import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;

public class Test {
    public static void main(String[] args) {
        Long begin = System.currentTimeMillis();
        System.out.println("正在导入....");
        HBaseDao hBaseDao = new HBaseDaoImpl();
        String path = "e:/american/thads2013n.csv";
        hBaseDao.insert("2013",path);
        Long end = System.currentTimeMillis();
        System.out.println("导入成功。");
        System.out.println("花费时间："+(end-begin)/1000);
    }
}
