package com.tecode.house.lijin.service.impl;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.lijin.service.SelectMysqlServer;
import com.tecode.house.lijin.utils.ConfigUtil;
import com.tecode.mysql.bean.*;
import com.tecode.mysql.bean.Legend;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version ：2018/12/3 V1.0
 * @author: 李晋
 */
public class SelectBasicsRoomsNumServer extends SelectMysqlServer {

    public SelectBasicsRoomsNumServer() {
        super(ConfigUtil.get("mybatis-config2"));
    }

    @Override
    public Option select(int year, String reportName) {
        // 查询报表
        Report report = getReport(year, reportName);
        // 查询图表
        Diagram diagram = getDiagrams(report).get(0);
        // 查询x轴
        XAxis xAxis = getXAxes(diagram).get(0);
        // 查询y轴
        YAxis yAxis = getYAxes(diagram).get(0);
        // 查询图例
        Legend legend = getLegends(diagram).get(0);
        // 查询数据
        Map<String, Map<String, String>> datum = getDatum(xAxis, legend);
        // 查询搜索
        List<Search> searchs = getSearch(report);

        // 拼装数据
        Option option = new Option();
        // 标题
        Title title = new Title().setText(diagram.getName()).setSubtext(diagram.getSubtext());
        // 提示框  {a}（系列名称），{b}（类目值），{c}（数值）
        Tooltip tooltip = new Tooltip().setTrigger(Trigger.item).setFormatter("{a}-{b} : {c}");
        // X轴
        Axis x = new Axis().setType(AxisType.category);
        // Y轴
        Axis y = new Axis().setType(AxisType.value);
        // 图例
        com.tecode.echarts.Legend legendOption = new com.tecode.echarts.Legend().setAlign(Align.right);
        Set<String> legendItems = datum.keySet();
        boolean b = true;
        // 按图例遍历
        for (String legendItem : legendItems) {
            // 生成一组数据
            Series<Integer> series = new Bar<Integer>().setName(legendItem);
            // 取出一组数据
            Map<String, String> xMap = datum.get(legendItem);
            for (Map.Entry<String, String> data : xMap.entrySet()) {
                series.addData(Integer.parseInt(data.getValue()));
                if (b) {
                    x.addData(data.getValue());
                    b = false;
                }
            }
            legendOption.addData(legendItem);
            option.addSeries(series);
        }
        option.setTitle(title).setTooltip(tooltip).setLegend(legendOption).addxAxis(x).addyAxis(y);
        return option;
    }
}
