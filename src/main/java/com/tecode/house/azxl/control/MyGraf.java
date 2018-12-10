package com.tecode.house.azxl.control;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.azxl.server.MaketPriceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
public class MyGraf {

    @Autowired
    MaketPriceServer mps;

    /**
     * 年收入
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/zxl_income", method = RequestMethod.POST)
    public Option incomeBar(String year) {

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

        //获取年收入
        Map<String, Integer> income = mps.getincome(Integer.valueOf(year));
//        Map<String, Integer> income = getIncome();

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
       /* Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("5万以下").addData("5万-10万").addData("10万-15万").addData("15万-20万").addData("20万-25万")
                .addData("25万-30万").addData("30万-35万").addData("35万-40万").addData("40万-45万").addData("45万-50万").addData("50万以上");*/
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


        return option;
    }

    /**
     * 市场价
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/zxl_value", method = RequestMethod.POST)
    public Option valuePie(String year) {
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

//        Map<String, Integer> map = get();
        //获取数据
        Map<String, Integer> map = mps.getMaket(Integer.valueOf(year));

        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("0-50万").addData("50-100万").addData("100-150万").addData("250万以上");

        // 数据
        Series series1 = new Pie().setName("市场价概况")
                .addData(new Pie.PieData<Integer>("最高房价", map.get("max")))
                .addData(new Pie.PieData<Integer>("最低房价", map.get("min")))
                .addData(new Pie.PieData<Integer>("平均房价", map.get("avg")));
        ((Pie)series1).setCenter("30%", "50%").setRadius("20%");



        // 数据
        Series series3 = new Pie().setName("市场价分布")
                .addData(new Pie.PieData<Integer>("0-50万", map.get("0-50万")))
                .addData(new Pie.PieData<Integer>("50万-100万", map.get("50万-100万")))
                .addData(new Pie.PieData<Integer>("100万-150万", map.get("100万-150万")))
                .addData(new Pie.PieData<Integer>("250万以上", map.get("250万以上")));
        ((Pie)series3).setCenter("70%", "50%").setRadius("20%");




        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series1).addSeries(series3);
        return option;
    }


    /**
     * 家庭人数
     *
     * @return 饼图
     */
    @ResponseBody
    @RequestMapping(value = "/zxl_person", method = RequestMethod.POST)
    public Option personPie(String year) {
        Option option = new Option();
        // 标题

        Title title = new Title()
                .setText("家庭人数")
                .setSubtext("副标题");

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

        //获取家庭人数
        Map<String, Integer> person = mps.getPerson(Integer.valueOf(year));


        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("1人").addData("2人").addData("3人").addData("4人").addData("5人").addData("6人").addData("6人以上");

        // 数据
        Series series2 = new Pie().setName("家庭人数")
                .addData(new Pie.PieData<Integer>("1人", person.get("1人")))
                .addData(new Pie.PieData<Integer>("2人", person.get("2人")))
                .addData(new Pie.PieData<Integer>("3人", person.get("3人")))
                .addData(new Pie.PieData<Integer>("4人", person.get("4人")))
                .addData(new Pie.PieData<Integer>("5人", person.get("5人")))
                .addData(new Pie.PieData<Integer>("6人", person.get("6人")))
                .addData(new Pie.PieData<Integer>("6人以上", person.get("6人以上")));
        ((Pie)series2).setCenter("30%", "50%").setRadius("20%");



        // 数据
        Series series4 = new Pie().setName("家庭人数概况")
                .addData(new Pie.PieData<Integer>("最大人数", person.get("max")))
                .addData(new Pie.PieData<Integer>("最小人数", person.get("min")))
                .addData(new Pie.PieData<Integer>("平均人数", person.get("avg")));
        ((Pie)series4).setCenter("70%", "50%").setRadius("20%");

        option.setTitle(title).setTooltip(tooltip).setLegend(legend)
                .addSeries(series2).addSeries(series4);
        return option;
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


}
