package com.tecode.house.zxl.dao.impl;

import com.tecode.house.zxl.bean.ReportBean;
import com.tecode.house.zxl.dao.HbaseDao;
import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;


public class HbaseDaoImpl implements HbaseDao {
private Configuration conf= HBaseConfiguration.create();
    @Override
    public ReportBean getMaketValue(){
        return null;
    }
}
