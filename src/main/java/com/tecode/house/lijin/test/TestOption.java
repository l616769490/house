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
    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public Option testBar(String year) {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("房产税")
                .setSubtext("按年份统计");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("0-500").addData("500-1000").addData("1000-1500").addData("1500-2000").addData("2000-2500")
                .addData("2500-3000").addData("3000-3500").addData("3500+");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("1900-2000").addData("2000-2010").addData("2010-2018");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        DataImpl mysqlDao = new DataImpl();
        String v1 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "0-500"}).get(0).getValue();
        String v2 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "500-1000"}).get(0).getValue();
        String v3 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "1000-1500"}).get(0).getValue();
        String v4 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "1500-2000"}).get(0).getValue();
        String v5 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "2000-2500"}).get(0).getValue();
        String v6 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "2500-3000"}).get(0).getValue();
        String v7 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "3000-3500"}).get(0).getValue();
        String v8 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"1900-2000", "3500+"}).get(0).getValue();
        String v9 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "0-500"}).get(0).getValue();

        String v10 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "500-1000"}).get(0).getValue();
        String v11 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "1000-1500"}).get(0).getValue();
        String v12 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "1500-2000"}).get(0).getValue();
        String v13 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "2000-2500"}).get(0).getValue();
        String v14 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "2500-3000"}).get(0).getValue();
        String v15 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "3000-3500"}).get(0).getValue();
        String v16 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "3500+"}).get(0).getValue();

        String v17 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "0-500"}).get(0).getValue();
        String v18 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "500-1000"}).get(0).getValue();
        String v19 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "1000-1500"}).get(0).getValue();
        String v20 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "1500-2000"}).get(0).getValue();
        String v21 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "2000-2500"}).get(0).getValue();
        String v22 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "2500-3000"}).get(0).getValue();
        String v23 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "3000-3500"}).get(0).getValue();
        String v24 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2000-2010", "3500+"}).get(0).getValue();


        Series<String> series1 = new Bar<String>()
                .setName("0-500")
                .addData(v1).addData(v9).addData(v17);
        Series<String> series2 = new Bar<String>()
                .setName("500-1000")
                .addData(v2).addData(v10).addData(v18);
        Series<String> series3 = new Bar<String>()
                .setName("1000-1500")
                .addData(v3).addData(v11).addData(v19);
        Series<String> series4 = new Bar<String>()
                .setName("1500-2000")
                .addData(v4).addData(v12).addData(v20);
        Series<String> series5 = new Bar<String>()
                .setName("2000-2500")
                .addData(v5).addData(v13).addData(v21);
        Series<String> series6 = new Bar<String>()
                .setName("2500-3000")
                .addData(v6).addData(v14).addData(v22);
        Series<String> series7 = new Bar<String>()
                .setName("3000-3500")
                .addData(v7).addData(v15).addData(v23);
        Series<String> series8 = new Bar<String>()
                .setName("3500+")
                .addData(v8).addData(v16).addData(v24);


        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                 .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4)
                .addSeries(series5).addSeries(series6).addSeries(series7).addSeries(series8);
        return option;
    }

    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/per", method = RequestMethod.POST)
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
                .addData(new Pie.PieData<Double>("1人", Double.parseDouble(v1) / sum))
                .addData(new Pie.PieData<Double>("2人", Double.parseDouble(v2) / sum))
                .addData(new Pie.PieData<Double>("3人", Double.parseDouble(v3) / sum))
                .addData(new Pie.PieData<Double>("4人", Double.parseDouble(v4) / sum))
                .addData(new Pie.PieData<Double>("5人", Double.parseDouble(v5) / sum))
                .addData(new Pie.PieData<Double>("6人", Double.parseDouble(v6) / sum))
                .addData(new Pie.PieData<Double>("6+人", Double.parseDouble(v7) / sum));
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


    @ResponseBody
    @RequestMapping(value = "/first", method = RequestMethod.POST)
    public Option testPi() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋比例")
                .setSubtext("第一区域");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c} %");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("独栋").addData("非独栋");

        // 数据

        DataImpl mysqlDao = new DataImpl();

        String  v1 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"1","独栋"}).get(0).getValue();

        String v2 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"2","独栋"}).get(0).getValue();

        String v3 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"3","独栋"}).get(0).getValue();

        String v4 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"4","独栋"}).get(0).getValue();

        String v5 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"1","非独栋"}).get(0).getValue();

        String v6 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"2","非独栋"}).get(0).getValue();

        String v7 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"3","非独栋"}).get(0).getValue();

        String v8 = mysqlDao.findByColums(new String[]{"x","legend"},new String[]{"4","非独栋"}).get(0).getValue();

        // 数据
        Series series1 = new Pie().setName("第一区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v1) ))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v5) ));
        ((Pie)series1).setCenter("50%", "50%").setRadius("50%");

        // 数据
        Series series2 = new Pie().setName("第二区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v2)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v6)));
        ((Pie)series2).setCenter("70%", "30%").setRadius("10%");

        // 数据
        Series series3 = new Pie().setName("第三区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v3)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v7)));
        ((Pie)series3).setCenter("70%", "30%").setRadius("10%");

        // 数据
        Series series4 = new Pie().setName("第四区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v4)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v8)));
        ((Pie)series3).setCenter("70%", "30%").setRadius("10%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }
    @ResponseBody
    @RequestMapping(value = "/second", method = RequestMethod.POST)
    public Option testP() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋比例")
                .setSubtext("第二区域");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c} %");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("独栋").addData("非独栋");

        // 数据

        DataImpl mysqlDao = new DataImpl();
        String v2 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2", "独栋"}).get(0).getValue();
        String v6 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"2", "非独栋"}).get(0).getValue();

        // 数据
        Series series1 = new Pie().setName("第二区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v2)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v6)));
        ((Pie) series1).setCenter("50%", "50%").setRadius("50%");
        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return option;
    }
    @ResponseBody
    @RequestMapping(value = "/third", method = RequestMethod.POST)
    public Option testPiee() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋比例")
                .setSubtext("第三区域");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c} %");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("独栋").addData("非独栋");

        // 数据

        DataImpl mysqlDao = new DataImpl();

        String v3 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"3", "独栋"}).get(0).getValue();

        String v7 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"3", "非独栋"}).get(0).getValue();


        // 数据
        Series series1 = new Pie().setName("第三区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v3)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v7)));
        ((Pie) series1).setCenter("50%", "50%").setRadius("50%");
        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return option;
    }
    @ResponseBody
    @RequestMapping(value = "/forth", method = RequestMethod.POST)
    public Option testPieee() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("独栋比例")
                .setSubtext("按区域分布");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c} %");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("独栋").addData("非独栋");

        // 数据

        DataImpl mysqlDao = new DataImpl();

        String v4 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"4", "独栋"}).get(0).getValue();

        String v8 = mysqlDao.findByColums(new String[]{"x", "legend"}, new String[]{"4", "非独栋"}).get(0).getValue();

        // 数据
        Series series1 = new Pie().setName("第四区域")
                .addData(new Pie.PieData<Double>("独栋", Double.parseDouble(v4)))
                .addData(new Pie.PieData<Double>("非独栋", Double.parseDouble(v8)));
        ((Pie) series1).setCenter("50%", "50%").setRadius("50%");
        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);

        return option;
    }

}
