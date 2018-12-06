package com.tecode.house.lijin.filter;

import com.tecode.table.Search;
import org.apache.hadoop.hbase.client.Result;

import java.util.List;
import java.util.Map;

/**
 * 等值过滤器
 * 需要字段和搜索值完全匹配方可通过
 * 版本：2018/12/5 V1.0
 * 成员：李晋
 */
public class EqualsFilter implements HBaseFilter {
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

            // 如果匹配条件为or时， 遇到任意一个匹配上的， 即可返回 true
            // 如果匹配条件为and时， 遇到任意一个匹配不上的， 即可返回false
            if (b != field.equals(value)) {
                return !b;
            }
        }
        // 如果匹配条件为or， 当全部字段匹配完成还没有通过， 即返回false
        // 如果匹配条件为and， 当全部字段匹配完成时都没有不通过的， 即返回true
        return b;
    }
}
