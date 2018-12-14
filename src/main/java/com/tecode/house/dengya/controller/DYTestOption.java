package com.tecode.house.dengya.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.*;
import com.tecode.house.dengya.bean.Data;
import com.tecode.house.dengya.dao.MySQLDao;
import com.tecode.house.dengya.dao.impl.MySQLDaoImpl;
import com.tecode.house.dengya.showservice.PictureService;
import com.tecode.house.dengya.showservice.impl.PictureServiceImpl;
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
public class DYTestOption {
    private PictureServiceImpl pictureService = new PictureServiceImpl();
    /**
     * 柱状图测试
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/price_city", method = RequestMethod.POST)
    public Option testBar(String year) {
        Option option = pictureService.select(year,"市场价格分布图","城市规模");
        return option;
    }


    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/units_option", method = RequestMethod.POST)
    public Option testPie(String year) {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("建筑单元数分布图")
                .setSubtext("统计年份"+year);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a} <br/>{b} : {c} ({d}%)");

        // 图例
        Legend legend = new Legend()
                .setOrient(Orient.horizontal)
                .setAlign(Align.right);
        // 数据

        Series series1 = new Pie().setName("建筑单元数分布图");
        List<Data> legendData = pictureService.getData(year,"建筑单元分布图",2,"基础分析");
        for (Data data : legendData) {
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Double>(data.getX(),Double.parseDouble(data.getValue())));

        }

        ((Pie)series1).setCenter("50%", "40%").setRadius("40%");



        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }
}
