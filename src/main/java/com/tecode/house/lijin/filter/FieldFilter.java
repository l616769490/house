package com.tecode.house.lijin.filter;

import com.tecode.table.Search;
import org.apache.hadoop.hbase.client.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段过滤器
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class FieldFilter implements HBaseFilter {
    private boolean b = false;

    @Override
    public boolean filter(Map<String, String> map, FilterBean filterBean, List<Search> searchs) {
        if (searchs == null || searchs.size() == 0) {
            return true;
        }

        // 判断过滤规则是 or 还是 and
        String rule = filterBean.getRule();
        if ("and".equals(rule)) {
            b = true;
        }

        List<Law> laws = filterBean.getItems();
        // 从数据库中取出作为比较字段的值
        // Law中存储的是 name : 字段名 min : 字段最小值事哪个字段 max : 字段最大值是哪个字段
        // 如 law = {name : L30, min : IPOV, max : L30}
        // 数据库中 L30 = 30000, IPOV = 20000
        // 则前端传过来的数值在[20000, 30000)这个区间内即匹配L30成功
        // law2Map 中存储的对应字段为: key = "L30", value = {min : 20000, max : 30000}
        Map<String, Law2> law2Map = new HashMap<>();
        for (Law law : laws) {
            String min = law.getMin();
            String max = law.getMax();
        double minDouble = Double.MIN_NORMAL;
        double maxDouble = Double.MAX_VALUE;
        if (min != null && !"".equals(min)) {
            minDouble = Double.parseDouble(map.get(min));
        }

        if (max != null && !"".equals(max)) {
            maxDouble = Double.parseDouble(map.get(max));
        }
            law2Map.put(law.getName(), new Law2(minDouble, maxDouble));
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
            // 查找搜索的字段对应数据库中的列名
            String hBaseColumn = filterBean.getColumns().get(searchValue);      // L30

            // 临时记录本次比较结果用的
            boolean b1 = false;

            // 取出前端要求搜索的字段在哪个范围
            Law2 law2 = law2Map.get(hBaseColumn);
            // 有任何一个搜索条件为null代表该条件全部通过， 即可立即返回true
            if (law2 == null) {
                return true;
            }

            // 匹配是否在规定的范围内
            if (hBaseValue >= law2.getMin() && hBaseValue < law2.getMax()) {
                b1 = true;
            }

            // 如果匹配条件为or时， 遇到任意一个匹配上的， 即可返回 true
            // 如果匹配条件为and时， 遇到任意一个匹配不上的， 即可返回false
            if (b != b1) {
                return !b;
            }
        }

        return b;
    }

    private class Law2 {
        private double min;
        private double max;

        public Law2(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }
    }
}
