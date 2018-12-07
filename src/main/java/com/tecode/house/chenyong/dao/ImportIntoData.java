package com.tecode.house.chenyong.dao;

import java.io.IOException;
import java.util.List;

public interface ImportIntoData {
    public List<String> readData();
    public void createTable(String rowkey) throws IOException;
    public void insertIntoData(String table,String rowkey,String columnFamily,String column,String value) throws IOException;
}
