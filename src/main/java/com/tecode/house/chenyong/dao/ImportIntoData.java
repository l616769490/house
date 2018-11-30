package com.tecode.house.chenyong.dao;

import java.io.IOException;
import java.util.List;

public interface ImportIntoData {
    public void readData();
    public void createTable(String rowkey) throws IOException;
    public void insertIntoData(List<String []> list) throws IOException;
}
