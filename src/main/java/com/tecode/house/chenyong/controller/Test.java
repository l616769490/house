package com.tecode.house.chenyong.controller;

import com.tecode.house.chenyong.bean.Diagram;
import com.tecode.house.chenyong.bean.Report;
import com.tecode.house.chenyong.bean.XAxis;
import com.tecode.house.chenyong.dao.AgeAnalysis;
import com.tecode.house.chenyong.dao.MySQLDao;
import com.tecode.house.chenyong.dao.RoomsAnalysisByAge;
import com.tecode.house.chenyong.dao.UtilityAnalysisByAge;
import com.tecode.house.chenyong.dao.impl.MySQLDaoImpl;
import com.tecode.house.chenyong.utils.MySQLUtil;
import com.tecode.table.TablePost;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/9.
 */
public class Test {

    public static void main(String[] args) {
//        List<Integer> tablePostAge = getTablePostAge();
//        int Id = 0;
//            for (Integer id : tablePostAge) {
//                Id = id;
//                System.out.println(Id);
//            }
        System.out.println("正在插入数据....");
        AgeAnalysis ageAnalysis = new AgeAnalysis();
        ageAnalysis.analysis("2013");


        RoomsAnalysisByAge roomsAnalysisByAge = new RoomsAnalysisByAge();
        roomsAnalysisByAge.analysis("2013");

        UtilityAnalysisByAge utilityAnalysisByAge = new UtilityAnalysisByAge();
        utilityAnalysisByAge.analysis("2013");
        System.out.println("数据分析完成");


    }
//    public  static List<Integer> getTablePostAge(){
//        MySQLDao getDataFromMysql = new MySQLDaoImpl();
//        TablePost tablePost = new TablePost();
//        List<Integer> list = new ArrayList<>();
//        Integer year = tablePost.getYear();
//        //System.out.println(year);
//        try {
//            Report byTableReport = null;
//            try {
//                byTableReport = getDataFromMysql.getByTableReport(MySQLUtil.getConn(), "户主年龄极值分布","户主年龄极值分布图", 2013);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//            List<Diagram> byTableDiagram = getDataFromMysql.getByTableDiagram(MySQLUtil.getConn(), byTableReport.getId());
//            List<com.tecode.house.chenyong.bean.Legend> byTableLegend = null;
//            for (Diagram diagram : byTableDiagram) {
//                int diagramId = diagram.getId();
//                byTableLegend = getDataFromMysql.getByTableLegend(MySQLUtil.getConn(), diagramId);
//                XAxis xAxis = getDataFromMysql.getByTableXaxis(MySQLUtil.getConn(), diagramId);
//                for (com.tecode.house.chenyong.bean.Legend legend : byTableLegend) {
//                    int legenId = legend.getId();
//                    list.add(legenId);
//                    list.add(xAxis.getId());
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
}
