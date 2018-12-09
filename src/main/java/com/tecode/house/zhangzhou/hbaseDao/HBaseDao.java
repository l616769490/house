package com.tecode.house.zhangzhou.hbaseDao;

/**
 * @author zz
 * hbase数据库操作的接口
 */
public interface HBaseDao {

    /**
     * 创建命名空间
     * @return true：成功 false：失败
     */
    boolean createNameSpace();
    /**
     * 创建表格
     * @param tableName 表格名称
     * @return true：成功 false：失败
     */
    boolean create(String tableName);

    /**
     * 插入数据
     * @param tableName 表明
     * @param path 文件路径
     * @return true：成功 false：失败
     */
    boolean insert(String tableName,String path);

}
