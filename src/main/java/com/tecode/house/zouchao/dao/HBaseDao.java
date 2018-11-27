package com.tecode.house.zouchao.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HBaseDao {
    /**
     * 获取公平市场租金
     *
     * @param tableName 表名
     * @return
     * @throws IOException
     */
    List<Integer> getAllRent(String tableName) throws IOException;
}
