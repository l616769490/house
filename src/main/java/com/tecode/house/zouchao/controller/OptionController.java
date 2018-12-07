package com.tecode.house.zouchao.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Orient;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.zouchao.bean.Data;
import com.tecode.house.zouchao.bean.Xaxis;
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl;
import com.tecode.house.zouchao.showSerivce.ShowSerivce;
import com.tecode.house.zouchao.showSerivce.ShowSerivceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OptionController {
    private ShowSerivceImpl showSerivce = new ShowSerivceImpl();

    @ResponseBody
    @RequestMapping(value = "/rent", method = RequestMethod.POST)
    public Option rent(String year) {
        Option option = new Option();
        Title title = new Title()
                .setText("公平市场租金分布")
                .setSubtext("统计年份：" + 2013);
        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a} <br/>{b} : {c} ({d}%)");

        // 图例
        Legend legend = new Legend()
                .setOrient(Orient.vertical)
                .setAlign(Align.left);
        // 数据
        Series series1 = new Pie().setName("租金区间");

        List<Data> legendData = showSerivce.getData("2013", "公平市场租金", 2,"基础分析");
        //System.out.println(legendData.size());
        //遍历data对象，设置图例及数据对象的数据
        for (Data data : legendData) {
            //System.out.println("x:  " + data.getX());
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }
        //设置饼图的位置及大小
        ((Pie) series1).setCenter("75%", "40%").setRadius("30%");

        ////折线图
        //
        //
        List<Data> lineData = showSerivce.getData("2013", "公平市场租金", 1,"基础分析");
        String Max = null;
        String Min = null;
        String Avg = null;
        //获取最大最小平均值
        for (Data data : lineData) {
            String str = data.getX();
            if (str.equals("最大值")) {
                Max = data.getValue();
                //System.out.println("Max:    " + Max);
            } else if (str.equals("最小值")) {
                Min = data.getValue();
                //System.out.println("Min:    " + Min);
            } else if (str.equals("平均值")) {
                Avg = data.getValue();
                //System.out.println("AVG:    " + Avg);
            }
        }
        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("").addData("").addData("");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        Series<Integer> series2 = new Line<Integer>()
                .setName("最大值")
                .addData(Integer.valueOf(Max)).addData(Integer.valueOf(Max)).addData(Integer.valueOf(Max));
        Series<Integer> series3 = new Line<Integer>()
                .setName("最小值")
                .addData(Integer.valueOf(Min)).addData(Integer.valueOf(Min)).addData(Integer.valueOf(Min));
        Series<Integer> series4 = new Line<Integer>()
                .setName("平均值")
                .addData((int) Double.parseDouble(Avg)).addData((int) Double.parseDouble(Avg)).addData((int) Double.parseDouble(Avg));
        //// 图例
        Legend lineLegend = new Legend()
                .setAlign(Align.left)
                .addData("最大值")
                .addData("最小值")
                .addData("平均值");

        option.setTitle(title).setTooltip(tooltip).addxAxis(x).addyAxis(y).setLegend(lineLegend)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/priceByBuild", method = RequestMethod.POST)
    public Option priceByBuild(String year) {
        Option option = showSerivce.select("2013", "价格统计","年份统计");
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/roomByBuild", method = RequestMethod.POST)
    public Option roomByBuild(String year) {
        Option option = showSerivce.select("2013", "房间数统计","年份统计");
        return option;
    }

}
