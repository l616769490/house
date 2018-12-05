package com.tecode.house.lijin.test;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.*;
import com.tecode.house.jianchenfei.jdbc.bean.Data;
import com.tecode.house.jianchenfei.jdbc.dao.MysqlDao;
import com.tecode.house.jianchenfei.jdbc.dao.impl.DataImpl;
import com.tecode.house.jianchenfei.jdbc.dao.impl.ReportImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
                .setFormatter("{a}-{b} : {c/250}%");

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
    public Option testPie() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("家庭人数饼图")
                .setSubtext("家庭人数统计");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c} %");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("1人").addData("2人").addData("3人").addData("4人").addData("5人").addData("6人").addData("6+人");

        // 数据

        DataImpl mysqlDao = new DataImpl();
        //String sql1 = "select value from data where x='1' and legend = '家庭人数'";
        String  v1 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"1","家庭人数"}).get(0).getValue();
        //String sql2 = "select value from data where x='2'and legend = '家庭人数'";
        String v2 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"2","家庭人数"}).get(0).getValue();
       // String sql3 = "select value from data where x='3'and legend = '家庭人数'";
        String v3 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"3","家庭人数"}).get(0).getValue();
       // String sql4 = "select value from data where x='4' and legend = '家庭人数'";
        String v4 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"4","家庭人数"}).get(0).getValue();
       // String sql5 = "select value from data where x='5' and legend = '家庭人数'";
        String v5 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"5","家庭人数"}).get(0).getValue();
        //String sql6 = "select value from data where x='6' and legend = '家庭人数'";
        String v6 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"6","家庭人数"}).get(0).getValue();
       // String sql7 = "select value from data where x='6+' and legend = '家庭人数'";
        String v7 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"6+","家庭人数"}).get(0).getValue();
        double sum = Double.parseDouble(v1 + v2 + v3 + v4 + v5 + v6 + v7);

        // 数据
        Series series1 = new Pie().setName("家庭人数")
                .addData(new Pie.PieData<Double>("1人", Double.parseDouble(v1) / sum * 100))
                .addData(new Pie.PieData<Double>("2人", Double.parseDouble(v2) / sum* 100))
                .addData(new Pie.PieData<Double>("3人", Double.parseDouble(v3) / sum* 100))
                .addData(new Pie.PieData<Double>("4人", Double.parseDouble(v4) / sum* 100))
                .addData(new Pie.PieData<Double>("5人", Double.parseDouble(v5) / sum* 100))
                .addData(new Pie.PieData<Double>("6人", Double.parseDouble(v6) / sum* 100))
                .addData(new Pie.PieData<Double>("6+人", Double.parseDouble(v7) / sum* 100));
        ((Pie)series1).setCenter("50%", "50%").setRadius("50%");

        // 数据
        Series series3 = new Pie().setName("饼图")
                .addData(new Pie.PieData<Integer>("数据一", 20))
                .addData(new Pie.PieData<Integer>("数据二", 10))
                .addData(new Pie.PieData<Integer>("数据三", 20))
                .addData(new Pie.PieData<Integer>("数据四", 50));
        ((Pie)series3).setCenter("70%", "30%").setRadius("10%");

        // 数据
        Series series4 = new Pie().setName("饼图")
                .addData(new Pie.PieData<Integer>("数据一", 20))
                .addData(new Pie.PieData<Integer>("数据二", 10))
                .addData(new Pie.PieData<Integer>("数据三", 20))
                .addData(new Pie.PieData<Integer>("数据四", 50));
        ((Pie)series4).setCenter("70%", "60%").setRadius("10%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }



}
