package com.tecode.house.jianchenfei.test;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.jianchenfei.jdbc.dao.impl.DataImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/6.
 */
public class PieOption {
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
        String v1 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1", "家庭人数"}).get(0).getValue();
        //String sql2 = "select value from data where x='2'and legend = '家庭人数'";
        String v2 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2", "家庭人数"}).get(0).getValue();
        // String sql3 = "select value from data where x='3'and legend = '家庭人数'";
        String v3 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"3", "家庭人数"}).get(0).getValue();
        // String sql4 = "select value from data where x='4' and legend = '家庭人数'";
        String v4 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"4", "家庭人数"}).get(0).getValue();
        // String sql5 = "select value from data where x='5' and legend = '家庭人数'";
        String v5 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"5", "家庭人数"}).get(0).getValue();
        //String sql6 = "select value from data where x='6' and legend = '家庭人数'";
        String v6 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"6", "家庭人数"}).get(0).getValue();
        // String sql7 = "select value from data where x='6+' and legend = '家庭人数'";
        String v7 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"6+", "家庭人数"}).get(0).getValue();
        double sum = Double.parseDouble(v1 + v2 + v3 + v4 + v5 + v6 + v7);

        // 数据
        Series series1 = new Pie().setName("家庭人数")
                .addData(new Pie.PieData<Double>("1人", Double.parseDouble(v1) / sum))
                .addData(new Pie.PieData<Double>("2人", Double.parseDouble(v2) / sum))
                .addData(new Pie.PieData<Double>("3人", Double.parseDouble(v3) / sum))
                .addData(new Pie.PieData<Double>("4人", Double.parseDouble(v4) / sum))
                .addData(new Pie.PieData<Double>("5人", Double.parseDouble(v5) / sum))
                .addData(new Pie.PieData<Double>("6人", Double.parseDouble(v6) / sum))
                .addData(new Pie.PieData<Double>("6+人", Double.parseDouble(v7) / sum));
        ((Pie) series1).setCenter("50%", "50%").setRadius("50%");



        // 数据
        Series series4 = new Pie().setName("饼图")
                .addData(new Pie.PieData<Integer>("数据一", 20))
                .addData(new Pie.PieData<Integer>("数据二", 10))
                .addData(new Pie.PieData<Integer>("数据三", 20))
                .addData(new Pie.PieData<Integer>("数据四", 50));
        ((Pie) series4).setCenter("70%", "60%").setRadius("10%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }
}
