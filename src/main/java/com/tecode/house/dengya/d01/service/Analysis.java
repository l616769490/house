package com.tecode.house.dengya.d01.service;

/**
 * 数据分析接口
 */
public interface Analysis {
    /**
     * 数据分析接口
     * @param tableName HBase数据库名字
     * @return  成功/失败
     */
    boolean analysis(String tableName);
}
