package com.tecode.house.lijun.sSerivce;

import com.tecode.echarts.Option;
import com.tecode.house.lijun.bean.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */
public interface TUService {


    public Option select(String year, String reportName, String group);


    public List<Data> getData(String year, String reportName, String group);

}
