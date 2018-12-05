package com.tecode.house.lijin.test;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestOption {

    /**
     * 折线图测试
     *
     * @return 折线图
     */
    @ResponseBody
    @RequestMapping(value = "/test-line", method = RequestMethod.POST)
    public Option testLine() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("折线图")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("数据一").addData("数据二").addData("数据三").addData("数据四");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("2011").addData("2012").addData("2013").addData("2014").addData("2015");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        Series<Integer> series1 = new Line<Integer>()
                .setName("数据一")
                .addData(10).addData(12).addData(17).addData(15).addData(13);
        Series<Integer> series2 = new Line<Integer>()
                .setName("数据二")
                .addData(15).addData(11).addData(13).addData(16).addData(12);
        Series<Integer> series3 = new Line<Integer>()
                .setName("数据三")
                .addData(17).addData(11).addData(12).addData(11).addData(10);
        Series<Integer> series4 = new Line<Integer>()
                .setName("数据四")
                .addData(15).addData(13).addData(11).addData(13).addData(11);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);
        return option;
    }


    /**
     * 柱状图测试
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/test-bar", method = RequestMethod.POST)
    public Option testBar(String year) {
        System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("柱状图")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("测试一").addData("测试二").addData("测试三").addData("测试四");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("2013").addData("2014").addData("2015").addData("2016").addData("2017");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        Series<Integer> series1 = new Bar<Integer>()
                .setName("测试一")
                .addData(10).addData(12).addData(17).addData(15).addData(13);
        Series<Integer> series2 = new Bar<Integer>()
                .setName("测试二")
                .addData(15).addData(11).addData(13).addData(16).addData(12);
        Series<Integer> series3 = new Bar<Integer>()
                .setName("测试三")
                .addData(17).addData(11).addData(12).addData(11).addData(10);
        Series<Integer> series4 = new Bar<Integer>()
                .setName("测试四")
                .addData(15).addData(13).addData(11).addData(13).addData(11);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);
        return option;
    }

    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/test-pie", method = RequestMethod.POST)
    public Option testPie(int x,int y) {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("空置状态饼图")
                .setSubtext("统计房屋的空置状态");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("空置").addData("居住");

        // 数据
        Series series1 = new Pie().setName("房屋数量")
                .addData(new Pie.PieData<Integer>("空置", x))
                .addData(new Pie.PieData<Integer>("居住", y));
        ((Pie)series1).setCenter("30%", "30%").setRadius("10%");


        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }
}
