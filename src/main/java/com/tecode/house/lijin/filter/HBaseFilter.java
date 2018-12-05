package com.tecode.house.lijin.filter;

import com.tecode.table.Search;
import org.apache.hadoop.hbase.client.Result;

import java.util.List;

/**
 * 过滤器接口
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public interface HBaseFilter {
    /**
     * HBase数据过滤
     *
     * @param result     一行数据集
     * @param filterBean 过滤规则，对于每个传入的条件，采取什么规则
     * @param searchs    搜索条件，传入的搜索条件
     * @return true : 保留； false : 丢掉
     */
    boolean filter(Result result, FilterBean filterBean, List<Search> searchs);
}
