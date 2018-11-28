package com.tecode.house.zhangzhou;

import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;

public class Test {
    public static void main(String[] args) {
        HBaseDao hBaseDao = new HBaseDaoImpl();
        hBaseDao.create("2011");
    }
}
