package com.tecode.house.liuhao.control;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.liuhao.dao.ReadMyqslDao;
import com.tecode.house.liuhao.dao.impl.ReadMysqlDaoImpl;
import com.tecode.house.liuhao.utils.MySQLUtil;
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
public class PackageOption {

    @ResponseBody
    @RequestMapping(value = "/basics_structuretype_num_bar", method = RequestMethod.POST)
    public Option testBar(String year) {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText(year + "建筑结构类型柱状图")
                .setSubtext("建筑结构类型的分布");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("房屋数量");

        // X轴
        ReadMyqslDao read =  new ReadMysqlDaoImpl();
        Series<Integer> series1 = new Bar<Integer>();
        Axis x = new Axis();
        try {
            Connection conn = MySQLUtil.getConn();
            List list = read.ReadTableData(conn);
            for (Object l : list) {
                String[] split = l.toString().split(":");
                x.setType(AxisType.category)
                        .addData(split[0]);
                // 数据
                series1.setName("基础分析").addData(Integer.valueOf(split[1]));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1);
        return option;
    }

    /**
     * 饼图
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/basics_structuretype_num_pie", method = RequestMethod.POST)
    public Option testPie() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("饼状图")
                .setSubtext("建筑结构类型分布");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend();
        // 数据
        Series series1 = new Pie().setName("饼图");
        //调用方法从mysql中取出分析结果
        ReadMyqslDao read = new ReadMysqlDaoImpl();
        try {
            Connection conn = MySQLUtil.getConn();
            List list = read.ReadTableData(conn);
            for (Object l : list) {
                String[] split = l.toString().split(":");
                legend.setAlign(Align.left).addData(split[0]);
                series1.addData(new Pie.PieData<Integer>(split[0], Integer.valueOf(split[1])));

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ((Pie) series1).setCenter("50%", "60%").setRadius("55%");


        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1);
        return option;

    }
}
