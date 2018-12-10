package com.tecode.house.zzlijun.sSerivce;

import com.tecode.echarts.Option;
import com.tecode.house.zzlijun.bean.Data;
import com.tecode.house.zzlijun.bean.Xaxis;

import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public interface TUService {


    public Xaxis getX(String year, String reportName);
    public Option select(String year, String reportName);
    public List<Data> getData(String year, String reportName);

}
