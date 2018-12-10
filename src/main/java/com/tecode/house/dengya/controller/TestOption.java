package com.tecode.house.dengya.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.*;
import com.tecode.house.dengya.bean.Data;
import com.tecode.house.dengya.dao.MySQLDao;
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl;
import com.tecode.house.dengya.utils.MySQLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @RequestMapping(value = "/price_city", method = RequestMethod.POST)
    public Option testBar(String year) {
        System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("城市规模市场价格统计图")
                .setSubtext("统计年份"+2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("房屋租金").addData("房屋售价");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y1 = new Axis()
                .setType(AxisType.value);
        Axis y2 = new Axis()
                .setType(AxisType.value);

        Connection conn = null;
        List<String> value = new ArrayList<>();

        List<String>  rent = new ArrayList<>();

        List<String> price = new ArrayList<>();


        try {
            conn = MySQLUtil.getConn();
            MySQLDao dao = new MySQLDaoImpl();
            List<Data> data = dao.getByTableData(conn, 2, 2);
            for (Data datum : data) {
                value.add(datum.getValue()+"_"+datum.getLegend());

            }
            System.out.println(value.size());
            for (String s : value) {
                String[] s1 = s.split("_");

                if(s1[1].equals("平均租金")){
                    rent.add(s1[0]);

                }else{
                    price.add(s1[0]);

                }
            }
            System.out.println(rent.size());
            System.out.println(price.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 数据
        Series<String> series1 = new Bar<String>()
                .setName("房屋租金")
                .addData(rent.get(0)).addData(rent.get(1)).addData(rent.get(2)).addData(rent.get(3)).addData(rent.get(4));
        series1.setyAxisIndex(1);
        Series<String> series2 = new Bar<String>()
                .setName("房屋售价")
                .addData(price.get(0)).addData(price.get(1)).addData(price.get(2)).addData(price.get(3)).addData(price.get(4));

        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y1).addyAxis(y2)
                .addSeries(series1).addSeries(series2);
        return option;
    }


    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/units_option", method = RequestMethod.POST)
    public Option testPie() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("建筑单元数分布图")
                .setSubtext("统计年份"+2013);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        Axis xAxis = new Axis().setType(AxisType.category).setShow(false)
                .addData("singleFamliy").addData("2-4units").addData("5-19units").addData("20-49units").addData("50+units").addData("MobileHome");

        // 图例
        Legend legend = new Legend()
                .setOrient(Orient.horizontal)
                .setAlign(Align.right)
                .setType(LegendType.scroll)
                .addData("singleFamliy").addData("2-4units").addData("5-19units").addData("20-49units").addData("50+units").addData("MobileHome");
        // 数据
        Connection conn = null;
        List<String> value = new ArrayList<>();
        try {
            conn = MySQLUtil.getConn();
            MySQLDao dao = new MySQLDaoImpl();
            List<Data> data = dao.getByTableData(conn, 1, 1);
            System.out.println(data.size());
            for (Data datum : data) {
                value.add(datum.getValue());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Series series1 = new Pie().setName("建筑单元数分布图")
                .addData(new Pie.PieData<String>("singleFamliy",value.get(1)))
                .addData(new Pie.PieData<String>("2-4units", value.get(5)))
                .addData(new Pie.PieData<String>("5-19units", value.get(3)))
                .addData(new Pie.PieData<String>("20-49units", value.get(0)))
                .addData(new Pie.PieData<String>("50+units", value.get(4)))
                .addData(new Pie.PieData<String>("MobileHome", value.get(2)));

        ((Pie)series1).setCenter("50%", "40%").setRadius("40%");



        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(xAxis)
                .addSeries(series1);
        return option;
    }
}
