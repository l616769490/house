package com.tecode.house.lijin.filter;

import com.tecode.table.Search;
import org.apache.hadoop.hbase.client.Result;

import java.util.List;
import java.util.Map;

/**
 * 范围过滤器
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class RangeFilter implements HBaseFilter {
    @Override
    public boolean filter(Map<String, String> map, FilterBean filterBean, List<Search> searchs) {
        if (searchs == null || searchs.size() == 0) {
            return true;
        }

        // 判断过滤规则是 or 还是 and
        String rule = filterBean.getRule();
        boolean b = false;
        if ("and".equals(rule)) {
            b = true;
        }

        for (Search search : searchs) {
            // 首先判断是否是该字段， 如：要搜索1区属于L30的， 先判断该条记录是不是1区，不是则跳到下一个搜索条件
            String searchTitle = search.getTitle();
            String hBaseTitle = map.get(filterBean.getGroupName());
            if(!searchTitle.equals(hBaseTitle)) {
                continue;
            }

            // 取出用于匹配的字段
            String field = filterBean.getField();       // ZINC2
            // 获得匹配值
            double hBaseValue = Double.parseDouble(map.get(field));

            // 取出搜索值
            String searchValue = search.getValues().get(0);     // L30

            // 拆分范围
            String[] ranges = searchValue.split("-");

            // 临时记录本次比较结果用的
            boolean b1 = false;

            if (ranges.length == 1) {
                // 如果长度为1，说明是最后一个范围
                // 去掉最后的+
                double v = Double.parseDouble(field.replace("+", ""));
                if (hBaseValue >= v) {
                    b1 = true;
                }
            } else {
                // 长度不为1，则取出范围数组
                double min = Double.parseDouble(ranges[0]);
                double max = Double.parseDouble(ranges[1]);
                if (min <= hBaseValue && hBaseValue < max) {
                    b1 = true;
                }
            }

            // 如果匹配条件为or时， 遇到任意一个匹配上的， 即可返回 true
            // 如果匹配条件为and时， 遇到任意一个匹配不上的， 即可返回false
            if (b != b1) {
                return !b;
            }
        }
        // 如果匹配条件为or， 当全部字段匹配完成还没有通过， 即返回false
        // 如果匹配条件为and， 当全部字段匹配完成时都没有不通过的， 即返回true
        return b;
    }
}
