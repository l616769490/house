package com.tecode.house.zouchao.test;

import com.tecode.house.zouchao.bean.*;
import com.tecode.house.zouchao.dao.impl.MySQLDaoImpl;
import com.tecode.house.zouchao.hbase.FilleToHBase;
import com.tecode.house.zouchao.serivce.impl.PriceByBuildAnalysis;
import com.tecode.house.zouchao.serivce.impl.RoomsByBuildAnalysis;
import com.tecode.house.zouchao.util.HBaseUtil;
import com.tecode.house.zouchao.util.MySQLUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws IOException {
        //HBaseUtil util = new HBaseUtil();
        //util.deleteTable("thads:2011");
        //      System.out.println(util.isNameSpaceExits("thads"));

        //导入数据
        //FilleToHBase fth = new FilleToHBase();
        //fth.readFile("D:\\thads2011.txt","thads:2011");
        //fth.readFile("D:\\thads2013n.csv","thads:2013");

        //分析租金数据
        //RentAnalysis rentAnalysis = new RentAnalysis();
        //rentAnalysis.analysis("thads:2013");

        //分析价格
        //PriceByBuildAnalysis rentByBuildAnalysis = new PriceByBuildAnalysis();
        //System.out.println(rentByBuildAnalysis.analysis("thads:2013"));

        //分析房间数
        //RoomsByBuildAnalysis roomsByBuildAnalysis = new RoomsByBuildAnalysis();
        //System.out.println(roomsByBuildAnalysis.analysis("thads:2013"));

        //try {
        //    Connection conn = MySQLUtil.getConn();
        //    MySQLDaoImpl dao = new MySQLDaoImpl();



            /*
            name	varchar	50	报表名
create	bigint		创建时间的时间戳
year	int	4	报表所统计的年份
group	varchar	50	所属分组的名字
status	int	1	0：创建中，1：已创建
             */

            //dao.putInTableReport(conn,new Report("a",2018,2013,"aa",1));





            /*
            图表表（diagram）
字段名	格式	长度	说明	约束	默认值
id	int		自增id	主键、自增
name	varchar	50	图表名	非空
type	int	1	0：折线图，1：柱状图，2：饼图	非空
reportId	int		报表id，外键（FK_diagram_report_id）	非空、外键
subtext	varchar	100	描述

             */

            //dao.putInTableDiagram(conn,new Diagram("租金",1,1,"aa"));





        /*
        x轴（xAxis）
字段名	格式	长度	说明	约束	默认值
id	int		自增id	主键、自增
name	varchar	20	x轴显示的名字	非空
diagramId	int		表id，外键（FK_xAxis_diagram_id）	非空、外键
dimGroupName	varchar	50	维度组名字（对应维度表的维度组名）	非空

         */

            //dao.putInTableXaxis(conn,new Xaxis("zujin",1,"租金"));




/*
y轴（yAxis）
字段名	格式	长度	说明	约束	默认值
id	int		自增id	主键、自增
name	varchar	20	x轴显示的名字	非空
diagramId	int		表id，外键（FK_xAxis_diagram_id）	非空、外键

 */

//dao.putInTableYaxis(conn,new Yaxis(1,"aaa",1));



            /*
            图例（legend）
字段名	格式	长度	说明	约束	默认值
id	int		自增id，维度顺序按id排序	主键、自增
name	varchar	50	名字	非空
dimGroupName	varchar	50	维度组名字（对应维度表的维度组名）	非空
diagramId	int		表id，外键（FK_legend_diagram_id）	非空、外键

             */

            //dao.putInTableLegend(conn,new Legend("a","租金",1));


            /*
            数据（data）
字段名	格式	长度	说明	约束	默认值
id	int		自增id，维度顺序按id排序	主键、自增
value	varchar	50	数据值	非空
xId	int		x轴id	非空
legendId	int		图例id	非空

             */
            //dao.putInTableData(conn,new Data("12",1,1));



        //
        //} catch (ClassNotFoundException e) {
        //    e.printStackTrace();
        //} catch (SQLException e) {
        //    e.printStackTrace();
        //}




    }
}