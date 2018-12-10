package com.tecode.house.lijun.sSerivce;

import com.tecode.echarts.Option;
import com.tecode.house.lijun.bean.Data;
import com.tecode.house.lijun.bean.Xaxis;

import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public interface TUService {


    public Xaxis getX(String year, String reportName);
    public Option select(String year, String reportName);
    public List<Data> getData(String year, String reportName);

}
