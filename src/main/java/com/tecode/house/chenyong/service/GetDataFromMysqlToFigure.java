package com.tecode.house.chenyong.service;

import com.tecode.house.chenyong.bean.Diagram;
import com.tecode.house.chenyong.bean.Param;
import com.tecode.house.chenyong.bean.Report;
import com.tecode.house.chenyong.bean.XAxis;
import com.tecode.house.chenyong.dao.CYMySQLDao;
import com.tecode.house.chenyong.dao.impl.CYMySQLDaoImpl;
import com.tecode.house.chenyong.utils.MySQLUtil;
import com.tecode.table.TablePost;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/10.
 */
@Service
public class GetDataFromMysqlToFigure {

    CYMySQLDao getDataFromMysql = new CYMySQLDaoImpl();

    //获取年龄分布参数的方法
    public List<Param> getTablePostAge(int year){
        List<Param> list = new ArrayList<>();

        try {
            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "户主年龄分布","基础分析", year);
            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
            for (Diagram diagram : byTableDiagram) {
                int diagramId = diagram.getId();
                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
                    int legenId = legend.getId();
                    Param param = new Param();
                    param.setLegendId(legenId);
                    param.setxId(xAxis.getId());
                    list.add(param);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取年龄极值分布参数的方法
    public List<Param> getTablePostExtreme(int year){
        List<Param> list = new ArrayList<>();

        try {
            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "户主年龄极值分布","基础分析", year);
            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
            for (Diagram diagram : byTableDiagram) {
                int diagramId = diagram.getId();
                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
                    int legenId = legend.getId();
                    Param param = new Param();
                    param.setLegendId(legenId);
                    param.setxId(xAxis.getId());
                    list.add(param);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取房间分布参数的方法
    public List<Param> getTablePostRooms(int year){
        List<Param> list = new ArrayList<>();
        try {
            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "房间数统计","户主年龄", year);
            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
            for (Diagram diagram : byTableDiagram) {
                int diagramId = diagram.getId();
                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
                    int legenId = legend.getId();
                    Param param = new Param();
                    param.setLegendId(legenId);
                    param.setxId(xAxis.getId());
                    list.add(param);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //获取卧室分布参数的方法
    public List<Param> getTablePostBedrms(int year){
        Param param = new Param();
        List<Param> list = new ArrayList<>();
        try {
            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "卧室数统计","户主年龄", year);
            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
            for (Diagram diagram : byTableDiagram) {
                int diagramId = diagram.getId();
                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
                    int legenId = legend.getId();
                    param.setLegendId(legenId);
                    param.setxId(xAxis.getId());
                }
                list.add(param);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    //获取水电费分布参数的方法
    public List<Param> getTablePostUtility(int year){
        Param param = new Param();
        List<Param> list = new ArrayList<>();
        try {
            Report byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "水电费统计","户主年龄", year);
            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
            for (Diagram diagram : byTableDiagram) {
                int diagramId = diagram.getId();
                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
                    int legenId = legend.getId();
                    param.setLegendId(legenId);
                    param.setxId(xAxis.getId());
                }
                list.add(param);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
