package com.tecode.house.zhangzhou.service;

import com.tecode.house.d01.service.Analysis;
import com.tecode.house.zhangzhou.hbaseDao.HBaseDao;
import com.tecode.house.zhangzhou.hbaseDao.impl.HBaseDaoImpl;

/**
 * 实现数据分析接口
 * @author zhangzhou
 */
public class AnalyzeServiceImpl implements Analysis {

    /**
     *
     * @param tableName HBase数据库名字
     * @return 返回成功/失败
     */
    @Override
    public boolean analysis(String tableName) {
        //首先在HBase中创建表格，调用HBaseDao
        HBaseDao hBaseDao = new HBaseDaoImpl();
        hBaseDao.create(tableName);
        //在hbase的“house”表格中添加数据
        String path = "E:\\american\\thads2013n.csv";

        hBaseDao.insert(tableName,path);

        return false;
    }
}
