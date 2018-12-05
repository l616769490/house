package com.tecode.house.lijin.filter;

import com.tecode.table.Search;
import org.apache.hadoop.hbase.client.Result;

import java.util.List;

/**
 * 范围过滤器
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class RangeFilter implements HBaseFilter {
    @Override
    public boolean filter(Result result, FilterBean filterBean, List<Search> searchs) {
        return false;
    }
}
