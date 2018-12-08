package com.tecode.house.lijun.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.Orient;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.lijun.bean.Data;
import com.tecode.house.lijun.showSerivce.impl.ShowServiceImpl;
import com.tecode.house.lijun.util.MySQLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2018/12/5.
 */

@Controller
public class OController {
    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/cost", method = RequestMethod.POST)
    public Option testPie() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService=new ShowServiceImpl();
        Option option = new Option();
        Connection conn = MySQLUtil.getConn();
        // 标题
        Title title = new Title()
                .setText("房产税分布图")
                .setSubtext("统计年份 :"+ 2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left).setOrient(Orient.vertical);

        // 数据
        Series series1 = new Pie().setName("费用区间");
        List<Data> dataList =showService.getData("2013","房屋费用");

       for (Data data : dataList) {
           legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));
            
        }

        ((Pie) series1).setCenter("40%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);




        return option;
    }



    /**
     * 水电费用
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/shuidian", method = RequestMethod.POST)
    public Option testPie1() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        Connection conn = MySQLUtil.getConn();
        // 标题
        Title title = new Title()
                .setText("水电费用分布图")
                .setSubtext("统计年份 :" + 2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right).setOrient(Orient.vertical);

        // 数据
        Series series1 = new Pie().setName("费用区间");
        List<Data> dataList = showService.getData("2013", "水电费用");

        for (Data data : dataList) {
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }

        ((Pie) series1).setCenter("40%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return  option;
    }


    /**
     * 其他费用
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/other", method = RequestMethod.POST)
    public Option testPie2() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        Connection conn = MySQLUtil.getConn();
        // 标题
        Title title = new Title()
                .setText("其他费用分布图")
                .setSubtext("统计年份 :" + 2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left).setOrient(Orient.vertical);

        // 数据
        Series series1 = new Pie().setName("费用区间");
        List<Data> dataList = showService.getData("2013", "房屋其他费用");

        for (Data data : dataList) {
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }

        ((Pie) series1).setCenter("40%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return  option;
    }

    /**
     * 房屋总费用
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/total", method = RequestMethod.POST)
    public Option testPie4() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        Connection conn = MySQLUtil.getConn();
        // 标题
        Title title = new Title()
                .setText("房屋总费用分布图")
                .setSubtext("统计年份 :" + 2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right).setOrient(Orient.vertical);

        // 数据
        Series series1 = new Pie().setName("费用区间");
        List<Data> dataList = showService.getData("2013", "房屋总费用");

        for (Data data : dataList) {
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }

        ((Pie) series1).setCenter("40%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return  option;
    }



   /* *
     * 家庭收入
     *
     * @return 饼图*/

    @ResponseBody
    @RequestMapping(value = "/income", method = RequestMethod.POST)
    public Option income() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        Connection conn = MySQLUtil.getConn();
        // 标题
        Title title = new Title()
                .setText("家庭收入分布图")
                .setSubtext("统计年份 :" + 2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left).setOrient(Orient.vertical);

        // 数据
        Series series1 = new Pie().setName("收入区间");
        List<Data> dataList = showService.getData("2013", "家庭收入");

        for (Data data : dataList) {
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }

        ((Pie) series1).setCenter("30%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return  option;
    }


    /* *
     * 住房价格
     *
     * @return 饼图*/

    @ResponseBody
    @RequestMapping(value = "/value", method = RequestMethod.POST)
    public Option priceValue() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        // 标题
        Title title1 = new Title()
                .setText("住房价格分布图")
                .setSubtext("统计年份 :" + 2013);

        // 提示框
        Tooltip tooltip1 = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        Tooltip tooltip2 = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend1 = new Legend()
                .setAlign(Align.left).setOrient(Orient.vertical);
        // 数据
        Series series1 = new Pie().setName("住房价格区间");
        List<Data> dataList = showService.getData("2013", "住房价格");


        for (Data data : dataList) {
            legend1.addData(data.getX()).addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));
        }



        ((Pie) series1).setCenter("50%", "50%").setRadius("30%");

        option.setTitle(title1).setTooltip(tooltip1).setLegend(legend1)
                .addSeries(series1);


        return  option;
    }


    /* *
     * 住房价格
     *
     * @return 饼图*/

    @ResponseBody
    @RequestMapping(value = "/frm", method = RequestMethod.POST)
    public Option priceFRM() throws SQLException, ClassNotFoundException {
        ShowServiceImpl showService = new ShowServiceImpl();
        Option option = new Option();
        // 标题
        Title title1 = new Title()
                .setText("住房租金分布图")
                .setSubtext("统计年份 :" + 2013);;

        // 提示框
        Tooltip tooltip1 = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");


        // 图例
        Legend legend1 = new Legend()
                .setAlign(Align.left).setOrient(Orient.vertical);
        // 数据
        Series series1 = new Pie().setName("住房租金区间");
        List<Data> dataList2 = showService.getData("2013", "住房租金");



        for (Data data : dataList2) {
            legend1.addData(data.getX()).addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));
        }

        ((Pie) series1).setCenter("40%", "50%").setRadius("30%");


        option.setTitle(title1).setTooltip(tooltip1).setLegend(legend1)
                .addSeries(series1);
        return  option;
    }

}
