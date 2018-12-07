package com.tecode.house.zouchao.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.Orient;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.zouchao.bean.Data;
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl;
import com.tecode.house.zouchao.showSerivce.ShowSerivceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OptionController {
    private ShowSerivceImpl showSerivce = new ShowSerivceImpl();
    private MySQLDaoImpl dao = new MySQLDaoImpl();

    @ResponseBody
    @RequestMapping(value = "/rent", method = RequestMethod.POST)
    public Option rent(String year) {
        Option option = new Option();
        Title title = new Title()
                .setText("公平市场租金分布")
                .setSubtext("统计年份：" + 2013);
        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a} <br/>{b} : {c} ({d}%)");

        // 图例
        Legend legend = new Legend()
                .setOrient(Orient.vertical)
                .setAlign(Align.left);
        // 数据
        Series series1 = new Pie().setName("租金区间");

        List<Data> legendData = showSerivce.getData("2013", "公平市场租金");
        //System.out.println(legendData.size());
        for (Data data : legendData) {
            //System.out.println("x:  " + data.getX());
            legend.addData(data.getX());
            series1.addData(new Pie.PieData<Integer>(data.getX(), Integer.valueOf(data.getValue())));

        }

        ((Pie) series1).setCenter("30%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/priceByBuild", method = RequestMethod.POST)
    public Option priceByBuild(String year) {
        Option option = showSerivce.select("2013", "家庭收入");
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/roomByBuild", method = RequestMethod.POST)
    public Option roomByBuild(String year) {
        Option option = showSerivce.select("2011", "房间数统计");
        return option;
    }

}
