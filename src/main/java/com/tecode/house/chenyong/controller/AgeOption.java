package com.tecode.house.chenyong.controller;

import com.tecode.echarts.*;
import com.tecode.echarts.Legend;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.chenyong.bean.*;
import com.tecode.house.chenyong.dao.CYMySQLDao;
import com.tecode.house.chenyong.dao.impl.CYMySQLDaoImpl;
import com.tecode.house.chenyong.service.GetDataFromMysqlToFigure;
import com.tecode.house.chenyong.utils.MySQLUtil;
import com.tecode.table.TablePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AgeOption {


//
//    //获取年龄分布参数的方法
//    public List<Param> getTablePostAge(TablePost tablePost){
//        List<Param> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        try {
//            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "户主年龄分布","户主年龄分布图", year);
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    Param param = new Param();
//                    param.setLegendId(legenId);
//                    param.setxId(xAxis.getId());
//                    list.add(param);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    //获取年龄极值分布参数的方法
//    public List<Param> getTablePostExtreme(TablePost tablePost){
//        List<Param> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        try {
//            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "户主年龄极值分布","户主年龄极值分布图", year);
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    Param param = new Param();
//                    param.setLegendId(legenId);
//                    param.setxId(xAxis.getId());
//                    list.add(param);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//    //获取房间分布参数的方法
//    public List<Param> getTablePostRooms(TablePost tablePost){
//        List<Param> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        try {
//            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "房间数统计","房间统计", year);
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    Param param = new Param();
//                    param.setLegendId(legenId);
//                    param.setxId(xAxis.getId());
//                    list.add(param);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

//    //获取卧室分布参数的方法
//    public List<Param> getTablePostBedrms(TablePost tablePost){
//        Param param = new Param();
//        List<Param> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        try {
//            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "卧室数统计","卧室统计", year);
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    param.setLegendId(legenId);
//                    param.setxId(xAxis.getId());
//                }
//                list.add(param);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
//
//
//    //获取水电费分布参数的方法
//    public List<Param> getTablePostUtility(TablePost tablePost){
//        Param param = new Param();
//        List<Param> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        try {
//            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "水电费统计","水电费统计", year);
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    param.setLegendId(legenId);
//                    param.setxId(xAxis.getId());
//                }
//                list.add(param);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    @Autowired
    GetDataFromMysqlToFigure gftf = new GetDataFromMysqlToFigure();

    CYMySQLDao getDataFromMysql = new CYMySQLDaoImpl();

    /**
     * 饼图测试
     *
     * @return 年龄分布饼图，年龄极值分布
     */
    @ResponseBody
    @RequestMapping(value = "/ageAnalysis", method = RequestMethod.POST)
    public Option agePie(int year) throws SQLException {
        List<List<Data>> extremeAge = new ArrayList<>();
        List<Data> extremeData = new ArrayList<>();
        List<Data> ageData = new ArrayList<>();
        //年龄分布参数
        List<Param> tablePostAge = gftf.getTablePostAge(year);
        for (Param param : tablePostAge) {
            ageData = getDataFromMysql.getByTableData(MySQLUtil.getConn(), param.getLegendId(), param.getxId());
        }
        //年龄极值分布参数
        List<Param> tablePostExtreme = gftf.getTablePostExtreme(year);
        for (Param param : tablePostExtreme) {
            extremeData = getDataFromMysql.getByTableData(MySQLUtil.getConn(), param.getLegendId(), param.getxId());
            extremeAge.add(extremeData);
        }

        Option option = new Option();

        // 标题
        Title title = new Title()
                .setText("户主年龄分布")
                .setSubtext("户主年龄分布图");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend();
//                .setAlign(Align.left)
//                .addData("18以下").addData("18-40").addData("40-65").addData("65以上");
        // 数据
        Series series = new Pie().setName("年龄区间");
            for (Data byTableDatum : ageData) {
                legend.setAlign(Align.left).addData(byTableDatum.getX());
                series.addData(new Pie.PieData<Integer>(byTableDatum.getX(), Integer.parseInt(byTableDatum.getValue())));
            }
        ((Pie)series).setCenter("65%", "30%").setRadius("30%");

        // 图例
        Legend legend1 = new Legend();

        // X轴
        Axis x = new Axis();

        // Y轴
        Axis y = new Axis().setType(AxisType.value);

        // 数据
        Series<Integer> series1 = new Bar<Integer>();
//        String getLegend1 = null;
//        String getvalue = null;
        for (List<Data> datas : extremeAge) {
            for (Data data : datas) {
                String getLegend1 = data.getLegend();
                String getvalue = data.getValue();
                series1.addData((int)Double.parseDouble(getvalue));
                x.addData(getLegend1);
            }

        }
        //添加数据进柱状图
        x.setType(AxisType.category);//.addData(getLegend1);
        series1.setName("极值");//.addData((int)Double.parseDouble(getvalue));

       option.setTitle(title).setTooltip(tooltip).setLegend(legend).addSeries(series).
               addxAxis(x).addyAxis(y).addSeries(series1);
        return option;
    }

    /**
     * 饼图测试
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/roomsAnalysisByAge", method = RequestMethod.POST)
    public Option roomsPie(int year) {
        List<Data> roomsData = new ArrayList<>();
        List<Data> bedroomData = new ArrayList<>();

        //房间分布参数
        List<Param> tablePostRooms = gftf.getTablePostRooms(year);
        for (Param tablePostRoom : tablePostRooms) {
            try {
                roomsData = getDataFromMysql.getByTableData(MySQLUtil.getConn(), tablePostRoom.getLegendId(), tablePostRoom.getxId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //卧室分布参数
        List<Param> tablePostBedrms = gftf.getTablePostBedrms(year);
        for (Param tablePostBedrm : tablePostBedrms) {
            try {
                bedroomData = getDataFromMysql.getByTableData(MySQLUtil.getConn(), tablePostBedrm.getLegendId(), tablePostBedrm.getxId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("房间和卧室分布")
                .setSubtext("各年龄段房间和卧室分布");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend roomLegend = new Legend();
//                .setAlign(Align.left)
//                .addData("数据一");
        Legend bedroomLegend = new Legend();

        // 数据
        Series roomReries = new Pie().setName("房间统计图");
//                .addData(new Pie.PieData<Integer>("数据一", 20));


        // 数据
        Series bedroomReries = new Pie().setName("卧室统计图");
//                .addData(new Pie.PieData<Integer>("数据一", 20));


        for (Data roomsDatum : roomsData) {
            roomLegend.setAlign(Align.left).addData(roomsDatum.getX());
            roomReries.addData(new Pie.PieData<Integer>(roomsDatum.getX(), Integer.parseInt(roomsDatum.getValue())));
        }

        for (Data bedroomDatum : bedroomData) {

            bedroomLegend.setAlign(Align.left).addData(bedroomDatum.getX());
            bedroomReries.addData(new Pie.PieData<Integer>(bedroomDatum.getX(), Integer.parseInt(bedroomDatum.getValue())));
        }

        ((Pie) roomReries).setCenter("25%", "50%").setRadius("30%");
        ((Pie) bedroomReries).setCenter("75%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(roomLegend).setLegend(bedroomLegend)
                .addSeries(roomReries).addSeries(bedroomReries);
        return option;
    }

    @ResponseBody
    @RequestMapping(value = "/utilityAnalysisByAge", method = RequestMethod.POST)
    public Option utilityPie(int year) {
        List<Data> utilityData = new ArrayList<>();

        //水电费分布参数
        List<Param> tablePostUtility = gftf.getTablePostUtility(year);
        for (Param utilityParam : tablePostUtility) {
            try {
                utilityData = getDataFromMysql.getByTableData(MySQLUtil.getConn(), utilityParam.getLegendId(), utilityParam.getxId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("水电费分布")
                .setSubtext("各年龄段水电费分布");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend utilityLegend = new Legend();
//                .setAlign(Align.left)
//                .addData("数据一");

        // 数据
        Series utilityReries = new Pie().setName("水电费统计图");
//                .addData(new Pie.PieData<Integer>("数据一", 20));

        for (Data utilityDatum : utilityData) {
            utilityLegend.setAlign(Align.left).addData(utilityDatum.getX());

            utilityReries.addData(new Pie.PieData<Integer>(utilityDatum.getX(), (int)Double.parseDouble(utilityDatum.getValue())));
        }

        ((Pie) utilityReries).setCenter("50%", "50%").setRadius("30%");

        option.setTitle(title).setTooltip(tooltip).setLegend(utilityLegend)
                .addSeries(utilityReries);
        return option;
    }
}
