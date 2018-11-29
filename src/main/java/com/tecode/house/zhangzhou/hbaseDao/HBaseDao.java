package com.tecode.house.zhangzhou.hbaseDao;

public interface HBaseDao {
    void create(String tableName);
    void insert(String tableName,String path);
}
