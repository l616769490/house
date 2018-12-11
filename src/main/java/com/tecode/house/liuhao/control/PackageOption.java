package com.tecode.house.liuhao.control;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.liuhao.bean.City;
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
    /**
     * 建筑结构类型柱状图
     *
     * @param year
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/basics_structuretype_num_bar", method = RequestMethod.POST)
    public Option structureTypeBar(String year) {
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
        ReadMyqslDao read = new ReadMysqlDaoImpl();
        Series<Integer> series1 = new Bar<Integer>();
        Axis x = new Axis();
        try {
            Connection conn = MySQLUtil.getConn();
            String where = ">15";
            List list = read.ReadTableData(conn, where);
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
     * 建筑结构类型饼状图
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
            String where = ">15";
            List list = read.ReadTableData(conn, where);
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

    /**
     * 税务柱状图
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/CitySize_Tax_Avg_bar", method = RequestMethod.POST)
    public Option CityTaxBar(String year) {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("税务比例柱状图")
                .setSubtext("税务统计");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("房屋费用").addData("水电费用").addData("其他费用");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        try {
            Connection conn = MySQLUtil.getConn();
            ReadMyqslDao read = new ReadMysqlDaoImpl();
            //List lists = read.ReadTableData(conn, "<16");
            City city = read.ReadCityTax(conn);
            //List list = read.RangeList(lists);
            Series<Integer> series1 = new Bar<Integer>().
                    setName("房屋费用").addData(city.getOneHomeCost()).addData(city.getTwoHomeCost()).addData(city.getThreeHomeCost())
                    .addData(city.getFourHomeCost()).addData(city.getFiveHomeCost());

            Series<Integer> series2 = new Bar<Integer>().
                    setName("水电费用").addData(city.getOneUtityFee()).addData(city.getTwoUtityFee()).addData(city.getThreeUtityFee())
                    .addData(city.getFourUtityFee()).addData(city.getFiveUtityFee());

            Series<Integer> series3 = new Bar<Integer>().
                    setName("其他费用").addData(city.getOneOtherCost()).addData(city.getTwoOtherCost()).addData(city.getThreeOtherCost())
                    .addData(city.getFourOtherCost()).addData(city.getFiveOtherCost());

            option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                    .addSeries(series1).addSeries(series2).addSeries(series3);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return option;

    }

    /**
     * 税务饼状图
     *
     * @return 饼图
     */
    /**
     *
     * @param tablename hbase的表名
      * @return
     */
    @ResponseBody
    @RequestMapping(value = "/CitySize_Tax_Avg_pie", method = RequestMethod.POST)
    public Option testPies(String year) {

        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("税务饼状图")
                .setSubtext("各个规模城市的税务统计");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        try {
            Connection conn = MySQLUtil.getConn();
            ReadMyqslDao read = new ReadMysqlDaoImpl();
            City city = read.ReadCityTax(conn);
            Series series1 = new Pie().setName("房屋费用")
                    .addData(new Pie.PieData<Integer>("一线城市", city.getOneHomeCost()))
                    .addData(new Pie.PieData<Integer>("二线城市", city.getTwoHomeCost()))
                    .addData(new Pie.PieData<Integer>("三线城市", city.getThreeHomeCost()))
                    .addData(new Pie.PieData<Integer>("四线城市", city.getFourHomeCost()))
                    .addData(new Pie.PieData<Integer>("五线城市", city.getFiveHomeCost()));
            ((Pie) series1).setCenter("20%", "30%").setRadius("30%");

            // 数据
            Series series2 = new Pie().setName("水电费用")
                    .addData(new Pie.PieData<Integer>("一线城市", city.getOneUtityFee()))
                    .addData(new Pie.PieData<Integer>("二线城市", city.getTwoUtityFee()))
                    .addData(new Pie.PieData<Integer>("三线城市", city.getThreeUtityFee()))
                    .addData(new Pie.PieData<Integer>("四线城市", city.getFourUtityFee()))
                    .addData(new Pie.PieData<Integer>("五线城市", city.getFiveUtityFee()));
            ((Pie) series2).setCenter("70%", "30%").setRadius("30%");

            // 数据
            Series series3 = new Pie().setName("其他费用")
                    .addData(new Pie.PieData<Integer>("一线城市", city.getOneOtherCost()))
                    .addData(new Pie.PieData<Integer>("二线城市", city.getTwoOtherCost()))
                    .addData(new Pie.PieData<Integer>("三线城市", city.getThreeOtherCost()))
                    .addData(new Pie.PieData<Integer>("四线城市", city.getFourOtherCost()))
                    .addData(new Pie.PieData<Integer>("五线城市", city.getFiveOtherCost()));
            ((Pie) series3).setCenter("45%", "70%").setRadius("30%");


            option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                    .addSeries(series1).addSeries(series2).addSeries(series3);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return option;
    }
}
