package com.tecode.house.jianchenfei.test;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.jianchenfei.jdbc.dao.impl.DataImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2018/12/6.
 */
public class BarOption {
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
//        Series<String> series2 = new Bar<String>()
//                .setName("500-1000")
//                .addData(15).addData(11).addData(13).addData(16).addData(12);
//        Series<String> series3 = new Bar<String>()
//                .setName("1000-1500")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
//        Series<String> series4 = new Bar<String>()
//                .setName("1500-2000")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
//        Series<String> series5 = new Bar<String>()
//                .setName("2000-2500")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
//        Series<String> series6 = new Bar<String>()
//                .setName("2500-3000")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
//        Series<String> series7 = new Bar<String>()
//                .setName("3000-3500")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
//        Series<String> series8 = new Bar<String>()
//                .setName("3500+")
//                .addData(17).addData(11).addData(12).addData(11).addData(10);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1);
        return option;
    }
}
