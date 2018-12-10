package com.tecode.house.libo.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.libo.dao.impl.MysqlDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PictureController {

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
     * 房屋数、卧室数柱状图
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/test-bar", method = RequestMethod.POST)
    public Option testBar(String year) {
        // System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("房间、卧室比例柱状图")
                .setSubtext("统计不同城市规模中的总房间数和卧室数");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("总房数").addData("卧室数");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String a = "select value from data where x ='1' and legend = '总房数'";
        String b = "select value from data where x ='1' and legend = '卧室数'";
        String c = "select value from data where x ='2' and legend = '总房数'";
        String d = "select value from data where x ='2' and legend = '卧室数'";
        String e = "select value from data where x ='3' and legend = '总房数'";
        String f = "select value from data where x ='3' and legend = '卧室数'";
        String g = "select value from data where x ='4' and legend = '总房数'";
        String h = "select value from data where x ='4' and legend = '卧室数'";
        String i = "select value from data where x ='5' and legend = '总房数'";
        String j = "select value from data where x ='5' and legend = '卧室数'";

        int a1 =mysqlDao.selectData(a);
        int b1  =mysqlDao.selectData(b);
        int c1  =mysqlDao.selectData(c);
        int d1  =mysqlDao.selectData(d);
        int e1  =mysqlDao.selectData(e);
        int f1  =mysqlDao.selectData(f);
        int g1  =mysqlDao.selectData(g);
        int h1  =mysqlDao.selectData(h);
        int i1  =mysqlDao.selectData(i);
        int j1  =mysqlDao.selectData(j);

        Series<Integer> series1 = new Bar<Integer>()
                .setName("总房间数")
                .addData(a1).addData(c1).addData(e1).addData(g1).addData(i1);
        Series<Integer> series2 = new Bar<Integer>()
                .setName("总卧室数")
                .addData(b1).addData(d1).addData(f1).addData(h1).addData(j1);
//        Series<Integer> series3 = new Bar<Integer>()
//                .setName("其他建筑")
//                .addData(v1).addData(v2).addData(v3).addData(v4).addData(v5);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2);
        return option;
    }

    /**
     * 独栋建筑柱状图
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/test-barr", method = RequestMethod.POST)
    public Option testBar2(String year) {
        //System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋建筑柱状图")
                .setSubtext("统计不同年份区间中的独栋建筑与非独栋建筑数量");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("独栋建筑").addData("其他建筑");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("1900-2000").addData("2000-2010").addData("2010-2018");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);
        Axis z = new Axis()
                .setType(AxisType.value);

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String a = "select value from data where x ='1900-2000' and legend = '独栋建筑'";
        String b = "select value from data where x ='1900-2000' and legend = '其他建筑'";
        String c = "select value from data where x ='2000-2010' and legend = '独栋建筑'";
        String d = "select value from data where x ='2000-2010' and legend = '其他建筑'";
        String e = "select value from data where x ='2010-2018' and legend = '独栋建筑'";
        String f = "select value from data where x ='2010-2018' and legend = '其他建筑'";


        int a1  =mysqlDao.selectData(a);
        int b1  =mysqlDao.selectData(b);
        int c1  =mysqlDao.selectData(c);
        int d1  =mysqlDao.selectData(d);
        int e1  =mysqlDao.selectData(e);
        int f1  =mysqlDao.selectData(f);

        Series<Integer> series1 = new Bar<Integer>()

                .setName("独栋建筑")
                .addData(a1).addData(c1).addData(e1);
        int count = 0;

        series1.setyAxisIndex(1);

        Series<Integer> series2 = new Bar<Integer>()
                .setName("其他建筑")
                .addData(b1).addData(d1).addData(f1);

//        Series<Integer> series3 = new Bar<Integer>()
//                .setName("其他建筑")
//                .addData(v1).addData(v2).addData(v3).addData(v4).addData(v5);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y).addyAxis(z)
                .addSeries(series1).addSeries(series2);
        return option;
    }


    /**
     * 自住租赁状态饼图
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/test-pie", method = RequestMethod.POST)
    public Option testPie() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("自住、租赁饼图")
                .setSubtext("统计房屋的自住租赁状态");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("自住").addData("租赁");

        // 数据
        MysqlDaoImpl mysqlDao = new MysqlDaoImpl();
        String sql1 = "select value from data where x='1' and legend = '自住、租赁图例'";
        String sql2 = "select value from data where x != '1' and legend = '自住、租赁图例'";
        int v1 = mysqlDao.selectData(sql1);
        //System.out.println("feikong:"+v1);
        int v2 = mysqlDao.selectData(sql2);
        //System.out.println("kongzhi:"+v2);
        Series series1 = new Pie().setName("房屋数量")
                .addData(new Pie.PieData<Integer>("自住",v1))
                .addData(new Pie.PieData<Integer>("租赁", v2));
        ((Pie)series1).setCenter("30%", "30%").setRadius("40%");


        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }

}
