package com.tecode.house.dengya.showservice;

import com.tecode.echarts.Option;
import com.tecode.house.dengya.bean.Data;

import java.util.List;

public interface PictureService {
    /**
     * 获取Data数据集
     *
     * @param year       年份
     * @param reportName 报表名
     * @param type       图标类型
     * @return Data数据集
     */
    public List<Data> getData(String year, String reportName, int type, String group);


    /**
     * 查询并将查询结果封装为Option对象
     *
     * @param year       年份
     * @param reportName 报表名
     * @return Option对象
     */
    public Option select(String year, String reportName, String group);

}
