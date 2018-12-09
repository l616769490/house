package com.tecode.house.lijin.test;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


@Controller
public class TestOption {

    /**
     * 折线图测试
     *
     * @return 折线图
     */
    @ResponseBody
    @RequestMapping(value = "/test-line", method = RequestMethod.POST)
    public Option testLine() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("折线图")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.right)
                .addData("数据一").addData("数据二").addData("数据三").addData("数据四");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("2011").addData("2012").addData("2013").addData("2014").addData("2015");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);

        // 数据
        Series<Integer> series1 = new Line<Integer>()
                .setName("数据一")
                .addData(10).addData(12).addData(17).addData(15).addData(13);
        Series<Integer> series2 = new Line<Integer>()
                .setName("数据二")
                .addData(15).addData(11).addData(13).addData(16).addData(12);
        Series<Integer> series3 = new Line<Integer>()
                .setName("数据三")
                .addData(17).addData(11).addData(12).addData(11).addData(10);
        Series<Integer> series4 = new Line<Integer>()
                .setName("数据四")
                .addData(15).addData(13).addData(11).addData(13).addData(11);
        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);
        return option;
    }


   /* /*
    @ResponseBody
    @RequestMapping(value = "/test-bar", method = RequestMethod.POST)
    public Option testBar(String year) {
        System.out.println(year);
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("年收入分布情况")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

//        Map<String, Integer> income = sort(getIncome());

        Map<String, Integer> income = getIncome();

        List<Integer> list1=new ArrayList<>();
        List<Integer> list2=new ArrayList<>();
        List<Integer> list3=new ArrayList<>();
        List<Integer> list4=new ArrayList<>();
        for(int i=0;i<5;i++){
            list1.add(0);
            list2.add(0);
            list3.add(0);
            list4.add(0);
        }


        // 图例
       *//* Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("5万以下").addData("5万-10万").addData("10万-15万").addData("15万-20万").addData("20万-25万")
                .addData("25万-30万").addData("30万-35万").addData("35万-40万").addData("40万-45万").addData("45万-50万").addData("50万以上");*//*
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("10万以下").addData("10万-30万").addData("30万-50万").addData("50万以上");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("一线城市").addData("二线城市").addData("三线城市").addData("四线城市").addData("五线城市");
        // Y轴
        Axis y = new Axis()
                .setType(AxisType.value);


        // 数据
        Series<Integer> series1 = new Bar<Integer>();
        Series<Integer> series2 = new Bar<Integer>();
        Series<Integer> series3 = new Bar<Integer>();
        Series<Integer> series4 = new Bar<Integer>();
       *//* Series<Integer> series5 = new Bar<Integer>();
        Series<Integer> series6 = new Bar<Integer>();
        Series<Integer> series7 = new Bar<Integer>();
        Series<Integer> series8 = new Bar<Integer>();
        Series<Integer> series9 = new Bar<Integer>();
        Series<Integer> series10 = new Bar<Integer>();
        Series<Integer> series11 = new Bar<Integer>();*//*




        Set<Map.Entry<String, Integer>> entries = income.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String key=entry.getKey();
            String cityIn=key.split("_")[1];
            String city=key.split("_")[0];
            int value=entry.getValue();
            if("10万以下".equals(cityIn)){

           set(list1,city,value);

            }else if("10万-30万".equals(cityIn)){

                set(list2,city,value);

            }else if("30万-50万".equals(cityIn)){

                set(list3,city,value);

            }else if("50万以上".equals(cityIn)){

                set(list4,city,value);

            }
            *//*else if("20万-25万".equals(cityIn)){
                set(series5,cityIn,city,entry);

            }else if("25万-30万".equals(cityIn)){
                set(series6,cityIn,city,entry);
            }else if("30万-35万".equals(cityIn)){
                set(series7,cityIn,city,entry);
            }else if("35万-40万".equals(cityIn)){
                set(series8,cityIn,city,entry);
            }else if("40万-45万".equals(cityIn)){
                set(series9,cityIn,city,entry);
            }else if("45万-50万".equals(cityIn)){
                set(series10,cityIn,city,entry);
            }else if("50万以上".equals(cityIn)){
                set(series11,cityIn,city,entry);
            }*//*




        }

        for (Integer integer : list1) {
            series1.setName("10万以下")
                    .addData(integer);
        }
        for (Integer integer : list2) {
            series2.setName("10万-30万")
                    .addData(integer);
        }

        for (Integer integer : list3) {
            series3.setName("30万-50万")
                    .addData(integer);
        }
        for (Integer integer : list4) {
            series4.setName("50万以上")
                    .addData(integer);
        }

        option.setTitle(title).setTooltip(tooltip).setLegend(legend).addxAxis(x).addyAxis(y)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);

              *//*  .addSeries(series5).addSeries(series6).addSeries(series7).addSeries(series8)
                .addSeries(series9).addSeries(series10).addSeries(series11);*//*
        return option;
    }
*/
   /* *//**
     * 饼图测试
     *
     * @return 饼图
     *//*
    @ResponseBody
    @RequestMapping(value = "/test-pie", method = RequestMethod.POST)
    public Option testPie() {
        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("市场价")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        Map<String, Integer> map = get();
        Map<String, Integer> person = getPerson();


        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("0-50万").addData("50-100万").addData("100-150万").addData("250万以上");

        // 数据
        Series series1 = new Pie().setName("市场价概况")
                .addData(new Pie.PieData<Integer>("最高房价", map.get("max")))
                .addData(new Pie.PieData<Integer>("最低房价", map.get("min")))
                .addData(new Pie.PieData<Integer>("平均房价", map.get("avg")));
        ((Pie)series1).setCenter("30%", "30%").setRadius("20%");

        // 数据
        Series series2 = new Pie().setName("家庭人数")
                .addData(new Pie.PieData<Integer>("1人", person.get("1人")))
                .addData(new Pie.PieData<Integer>("2人", person.get("2人")))
                .addData(new Pie.PieData<Integer>("3人", person.get("3人")))
                .addData(new Pie.PieData<Integer>("4人", person.get("4人")))
                .addData(new Pie.PieData<Integer>("5人", person.get("5人")))
                .addData(new Pie.PieData<Integer>("6人", person.get("6人")))
                .addData(new Pie.PieData<Integer>("6人以上", person.get("6人以上")));
        ((Pie)series2).setCenter("30%", "60%").setRadius("20%");

        // 数据
        Series series3 = new Pie().setName("市场价分布")
                .addData(new Pie.PieData<Integer>("0-50万", map.get("0-50万")))
                .addData(new Pie.PieData<Integer>("50万-100万", map.get("50万-100万")))
                .addData(new Pie.PieData<Integer>("100万-150万", map.get("100万-150万")))
                .addData(new Pie.PieData<Integer>("250万以上", map.get("250万以上")));
        ((Pie)series3).setCenter("70%", "30%").setRadius("20%");


        // 数据
        Series series4 = new Pie().setName("家庭人数概况")
                .addData(new Pie.PieData<Integer>("最大人数", person.get("max")))
                .addData(new Pie.PieData<Integer>("最小人数", person.get("min")))
                .addData(new Pie.PieData<Integer>("平均人数", person.get("avg")));
        ((Pie)series4).setCenter("70%", "60%").setRadius("20%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1).addSeries(series2).addSeries(series3).addSeries(series4);
        return option;
    }*/

  /*  private Map<String,Integer> get(){
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://166.166.1.10/house";
        String user="root";
        String password="root";

        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
            String sql="select d1.dimName,d.value,d.x from `data` d right join dimension d1 on d.legendId=d1.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("市场价".equals(s)) {
                    map.put(rs.getString("dimName"), Integer.valueOf(rs.getString("value")));

                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }




        return map;
    }

    private Map<String,Integer> getPerson(){
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://166.166.1.10/house";
        String user="root";
        String password="root";

        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
            String sql="select d1.dimName,d.value,d.x from `data` d right join dimension d1 on d.legendId=d1.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("家庭人数".equals(s)) {
                    map.put(rs.getString("dimName"), Integer.valueOf(rs.getString("value")));

                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }




        return map;
    }

    private Map<String,Integer> getIncome(){
        Map<String,Integer> map=new HashMap<>();
        Connection conn;
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://166.166.1.10/house";
        String user="root";
        String password="root";

        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
            String sql="select d1.dimName,d.value,d.x from `data` d right join dimension d1 on d.legendId=d1.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String s=rs.getString("x");
                if("年收入".equals(s)) {
                    map.put(rs.getString("dimName"), Integer.valueOf(rs.getString("value")));

                }
            }

            conn.close();

        }catch (Exception e){
            e.printStackTrace();

        }

        return map;
    }



    private void set(List<Integer> list,String city,int value){
        if("1".equals(city)){
            list.set(0,value);
        }else if("2".equals(city)){
            list.set(1,value);
        }else if("3".equals(city)){
            list.set(2,value);
        }else if("4".equals(city)){
            list.set(3,value);
        }else if("5".equals(city)){
            list.set(4,value);
        }

    }

*/
}
