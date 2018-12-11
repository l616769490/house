package com.tecode.house.azhangzhou.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.azhangzhou.mysqlDao.impl.MysqlDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 1.在接收html请求的java文件的类上添加@Controller
 */
@Controller
public class ZZOptionController {
    /**
     * 独栋建筑比例柱状图
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/city-singleBuilding", method = RequestMethod.POST)
    public Option testBar(String year) {
        System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋建筑比例柱状图")
                .setSubtext("统计不同城市规模中的总建筑数和独栋建筑数");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("建筑总数").addData("独栋建筑").addData("其他建筑");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String sql1 = "select value from data where x='1' and legend = '其他建筑'";
        String sql2 = "select value from data where x='2' and legend = '其他建筑'";
        String sql3 = "select value from data where x='3' and legend = '其他建筑'";
        String sql4 = "select value from data where x='4' and legend = '其他建筑'";
        String sql5 = "select value from data where x='5' and legend = '其他建筑'";
        String sql6 = "select value from data where x='1' and legend = '独栋建筑'";
        String sql7 = "select value from data where x='2' and legend = '独栋建筑'";
        String sql8 = "select value from data where x='3' and legend = '独栋建筑'";
        String sql9 = "select value from data where x='4' and legend = '独栋建筑'";
        String sql10 = "select value from data where x='5' and legend = '独栋建筑'";
        int v1 = mysqlDao.selectData(sql1);
        int v2 = mysqlDao.selectData(sql2);
        int v3 = mysqlDao.selectData(sql3);
        int v4 = mysqlDao.selectData(sql4);
        int v5 = mysqlDao.selectData(sql5);
        int v6 = mysqlDao.selectData(sql6);
        int v7 = mysqlDao.selectData(sql7);
        int v8 = mysqlDao.selectData(sql8);
        int v9 = mysqlDao.selectData(sql9);
        int v10 = mysqlDao.selectData(sql10);
        Series<Integer> series1 = new Bar<Integer>()
                .setName("建筑总数")
                .addData(v1+v6).addData(v2+v7).addData(v3+v8).addData(v4+v9).addData(v5+v10);
        Series<Integer> series2 = new Bar<Integer>()
                .setName("独栋建筑")
                .addData(v6).addData(v7).addData(v8).addData(v9).addData(v10);
        Series<Integer> series3 = new Bar<Integer>()
                .setName("其他建筑")
                .addData(v1).addData(v2).addData(v3).addData(v4).addData(v5);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3);
        return option;
    }


    /**
     * 房产税柱状图
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/city-houseDuty", method = RequestMethod.POST)
    public Option testHouseDuty(String year) {
        System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("房产税柱状图")
                .setSubtext("统计不同城市规模中的平均房产税、最高房产税、最低房产税");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("最高房产税").addData("最低房产税").addData("平均房产税");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String sql1 = "select value from data where x='1' and legend = 'max'";
        String sql2 = "select value from data where x='2' and legend = 'max'";
        String sql3 = "select value from data where x='3' and legend = 'max'";
        String sql4 = "select value from data where x='4' and legend = 'max'";
        String sql5 = "select value from data where x='5' and legend = 'max'";
        String sql6 = "select value from data where x='1' and legend = 'min'";
        String sql7 = "select value from data where x='2' and legend = 'min'";
        String sql8 = "select value from data where x='3' and legend = 'min'";
        String sql9 = "select value from data where x='4' and legend = 'min'";
        String sql10 = "select value from data where x='5' and legend = 'min'";
        String sql11 = "select value from data where x='1' and legend = 'avg'";
        String sql12 = "select value from data where x='2' and legend = 'avg'";
        String sql13 = "select value from data where x='3' and legend = 'avg'";
        String sql14 = "select value from data where x='4' and legend = 'avg'";
        String sql15 = "select value from data where x='5' and legend = 'avg'";
        int v1 = mysqlDao.selectData(sql1);
        int v2 = mysqlDao.selectData(sql2);
        int v3 = mysqlDao.selectData(sql3);
        int v4 = mysqlDao.selectData(sql4);
        int v5 = mysqlDao.selectData(sql5);
        int v6 = mysqlDao.selectData(sql6);
        int v7 = mysqlDao.selectData(sql7);
        int v8 = mysqlDao.selectData(sql8);
        int v9 = mysqlDao.selectData(sql9);
        int v10 = mysqlDao.selectData(sql10);
        int v11 = mysqlDao.selectData(sql11);
        int v12 = mysqlDao.selectData(sql12);
        int v13 = mysqlDao.selectData(sql13);
        int v14 = mysqlDao.selectData(sql14);
        int v15 = mysqlDao.selectData(sql15);
        Series<Integer> series1 = new Bar<Integer>()
                .setName("最高房产税")
                .addData(v1).addData(v2).addData(v3).addData(v4).addData(v5);
        Series<Integer> series2 = new Bar<Integer>()
                .setName("最低房产税")
                .addData(v6).addData(v7).addData(v8).addData(v9).addData(v10);
        Series<Integer> series3 = new Bar<Integer>()
                .setName("平均房产税")
                .addData(v11).addData(v12).addData(v13).addData(v14).addData(v15);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3);
        return option;
    }
    /**
     * 空置状态饼图
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/basic-vacancy", method = RequestMethod.POST)
    public Option testPie() {
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
                .addData("居住").addData("空置");

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String sql1 = "select value from data where x='-6' and legend = '空置状态图例'";
        String sql2 = "select sum(value) from data where x != '-6' and legend = '空置状态图例'";
        int v1 = mysqlDao.selectData(sql1);
        System.out.println("feikong:"+v1);
        int v2 = mysqlDao.selectData(sql2);
        System.out.println("kongzhi:"+v2);
        Series series1 = new Pie().setName("房屋数量")
                .addData(new Pie.PieData<Integer>("居住",v1))
                .addData(new Pie.PieData<Integer>("空置", v2));
        ((Pie)series1).setCenter("30%", "30%").setRadius("40%");


        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }
}
