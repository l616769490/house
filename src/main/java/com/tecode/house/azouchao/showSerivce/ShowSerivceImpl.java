package com.tecode.house.azouchao.showSerivce;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.azouchao.bean.*;
import com.tecode.house.azouchao.bean.Legend;
import com.tecode.house.azouchao.dao.MySQLDao;
import com.tecode.house.azouchao.dao.impl.MySQLDaoImpl;
import com.tecode.house.azouchao.util.MySQLUtil;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Service
public class ShowSerivceImpl implements ShowSerivce {

    private MySQLDao dao = new MySQLDaoImpl();
    private Connection conn;

    @Override
    public List<Data> getData(String year, String reportName, int type, String group) {
        List<Data> byTableDimension = new ArrayList<>();
        try {
            int yea = Integer.parseInt(year);
            conn = MySQLUtil.getConn();
            //获取报表ID
            int reportId = dao.getByTableReport(conn, reportName, yea, group).getId();
            //根据报表ID获取图标
            List<Diagram> diagrams = dao.getByTableDiagram(conn, reportId);
            //获取传入图标类型的图标ID
            int diagramId = -1;
            for (Diagram diagram : diagrams) {
                //System.out.println(diagram);
                //
                if (diagram.getType() == type) {
                    diagramId = diagram.getId();
                }
            }
            //获取X轴对象
            Xaxis x = dao.getByTableXaxis(conn, diagramId);
            //获取数据集对象
            List<Legend> legends = dao.getByTableLegend(conn, diagramId);
            for (Legend legend : legends) {
                int id = legend.getId();
                //根据获取的数据集ID及x轴ID获取Data对象
                List<Data> datas = dao.getByTableData(conn, id, x.getId());
                //System.out.println("s:  "+datas.size());
                byTableDimension.addAll(datas);
                //System.out.println("size:   "+byTableDimension.size());
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.close(conn);
        }
        return byTableDimension;
    }


    @Override
    public Option select(String year, String reportName, String group) {
        Option option = new Option();
        try {
            int yea = Integer.parseInt(year);
            conn = MySQLUtil.getConn();
            //获取report
            Report report = dao.getByTableReport(conn, reportName, yea, group);
            //获取diagrams表
            List<Diagram> diagrams = dao.getByTableDiagram(conn, report.getId());
            //获取xaxis表
            Xaxis xaxis = null;
            //获取yaxis表
            Yaxis yaxis = null;
            //获取legend表
            List<Legend> legends = new ArrayList<>();
            for (Diagram diagram : diagrams) {
                xaxis = dao.getByTableXaxis(conn, diagram.getId());
                yaxis = dao.getByTableYaxis(conn, diagram.getId());
                legends = dao.getByTableLegend(conn, diagram.getId());
            }
            //获取x维度组
            List<String> xDimension = dao.getByTableDimension(conn, xaxis.getDimGroupName());

            //获取legend维度
            List<String> legendDimension = new ArrayList<>();
            for (Legend legend : legends) {
                legendDimension = dao.getByTableDimension(conn, legend.getDimGroupName());
            }

            //获取data表
            List<Data> data = new ArrayList<>();
            for (Legend legend : legends) {
                data.addAll(dao.getByTableData(conn, legend.getId(), xaxis.getId()));
            }
            Set<String> set = new HashSet<>();
            for (Data d : data) {
                set.add(d.getLegend());
            }
            //封装Option对象
            //System.out.println("size:   "+set.size());

            // 标题
            Title title = new Title()
                    .setText(report.getName())
                    .setSubtext("统计年份：" + year);

            // 提示框
            Tooltip tooltip = new Tooltip()
                    .setTrigger(Trigger.item)
                    // {a}（系列名称），{b}（类目值），{c}（数值）
                    .setFormatter("{a}-{b} : {c}");


            // 图例
            com.tecode.echarts.Legend legend = new com.tecode.echarts.Legend()
                    .setAlign(Align.right);
            for (String s : legendDimension) {
                legend.addData(s);
            }

            // X轴
            Axis x = new Axis()
                    .setType(AxisType.category);
            for (String s : xDimension) {
                x.addData(s);
            }

            // Y轴
            Axis y1 = new Axis()
                    .setType(AxisType.value);
            Axis y2 = new Axis()
                    .setType(AxisType.value);

            // 数据

            option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y1).addyAxis(y2);
            int count = 0;
            for (String s : set) {
                Series<Integer> series1 = new Line<Integer>().setName(s);
                series1.setyAxisIndex(count);
                count++;
                //System.out.println("s:  " + s);
                for (String s1 : xDimension) {
                    //System.out.println("s1: " + s1);
                    for (Data d : data) {
                        //System.out.println("==" + d);
                        if (s.equals(d.getLegend()) && s1.equals(d.getX())) {
                            //System.out.println(d);
                            int i = (int) Double.parseDouble(d.getValue());
                            series1.addData(Integer.valueOf(i));
                        }
                    }
                }
                option.addSeries(series1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            MySQLUtil.close(conn);
        }
        return option;
    }


}
