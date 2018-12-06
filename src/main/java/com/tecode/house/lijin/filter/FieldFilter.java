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
                minDouble = Double.parseDouble(min);
            }

            if (max != null && !"".equals(max)) {
                maxDouble = Double.parseDouble(max);
            }
        }

        for (Search search : searchs) {
            // 取出搜索哪个字段
            String title = search.getTitle();
            // 取出搜索值
            List<String> values = search.getValues();
            // 查找搜索的字段对应数据库中的列名
            String hBaseColumn = filterBean.getColumns().get(title);
            // 找出数据库中查出来的搜索列对应的值
            String value = map.get(hBaseColumn);
            // 取出前端传过来的搜索值，搜索的时候该列表只有一个值
            String field = search.getValues().get(0);
            // 将数据库中的值转换为double
            double hBaseValue = Double.parseDouble(value);

            // 临时记录本次比较结果用的
            boolean b1 = false;

            // 取出前端要求搜索的字段在哪个范围
            Law2 law2 = law2Map.get(value);
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


        return true;
    }

    private class Law2 {
        private double min;
        private double max;

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
