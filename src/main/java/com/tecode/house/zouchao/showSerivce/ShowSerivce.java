package com.tecode.house.zouchao.showSerivce;

import com.tecode.echarts.Option;
import com.tecode.house.zouchao.bean.Data;
import com.tecode.house.zouchao.bean.Xaxis;

import java.sql.Connection;
import java.util.List;

public interface ShowSerivce {
    public List<Data> getData(String year,String reportName);
    public Xaxis getX(String year, String reportName);

    public Option select(String year,String reportName);


}
