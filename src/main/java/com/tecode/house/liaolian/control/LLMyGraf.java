package com.tecode.house.liaolian.control;

import com.tecode.echarts.*;
import com.tecode.echarts.enums.Align;
import com.tecode.echarts.enums.AxisType;
import com.tecode.echarts.enums.Trigger;
import com.tecode.house.liaolian.server.LLMaketPriceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
public class LLMyGraf {

    @Autowired
    LLMaketPriceServer mps;

    /**
     * 年收入
     *
     * @return 柱状图
     */
    @ResponseBody
    @RequestMapping(value = "/ll_income", method = RequestMethod.POST)
    public Option incomeBar(String year) {
        //获取年收入
        Map<String, Integer> income = mps.getincome(Integer.valueOf(year));

        Option option = new Option();
        // 标题
        Title title = new Title()
                .setText("家庭总人数分布情况")
                .setSubtext("家庭总人数："+year);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

//        Map<String, Integer> income = sort(getIncome());


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
                .addData("2人以下").addData("2-3人").addData("3-4人").addData("4人以上");

        // X轴
        Axis x = new Axis()
                .setType(AxisType.category)
                .addData("普查区域1").addData("普查区域2").addData("普查区域3").addData("普查区域4").addData("普查区域5");
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
            if("2人以下".equals(cityIn)){
           set(list1,city,value);
            }else if("2-3人".equals(cityIn)){

                set(list2,city,value);

            }else if("3-4人".equals(cityIn)){

                set(list3,city,value);

            }else if("4人以上".equals(cityIn)){

                set(list4,city,value);
            }

        }

        for (Integer integer : list1) {
            series1.setName("2人以下")
                    .addData(integer);
        }
        for (Integer integer : list2) {
            series2.setName("2-3人")
                    .addData(integer);
        }

        for (Integer integer : list3) {
            series3.setName("3-4人")
                    .addData(integer);
        }
        for (Integer integer : list4) {
            series4.setName("4人以上")
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
    @RequestMapping(value = "/ll_value", method = RequestMethod.POST)
    public Option valuePie(String year) {
        //获取数据
        Map<String, Integer> map = mps.getMaket(Integer.valueOf(year));

        Option option = new Option();

        // 标题
        Title title = new Title()
                .setText("家庭收入")
                .setSubtext("家庭收入："+year);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");

//        Map<String, Integer> map = get();


        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("0-1万").addData("2万-3万").addData("3万-4万").addData("4万以上");

        // 数据
        Series series1 = new Pie().setName("家庭收入概况")
                .addData(new Pie.PieData<Integer>("最高收入", map.get("max")))
                .addData(new Pie.PieData<Integer>("最低收入", map.get("min")))
                .addData(new Pie.PieData<Integer>("平均收入", map.get("avg")));
        ((Pie)series1).setCenter("30%", "50%").setRadius("20%");



        // 数据
        Series series3 = new Pie().setName("家庭收入分布")
                .addData(new Pie.PieData<Integer>("0-1万", map.get("0-1万")))
                .addData(new Pie.PieData<Integer>("2万-3万", map.get("2万-3万")))
                .addData(new Pie.PieData<Integer>("3万-4万", map.get("3万-4万")))
                .addData(new Pie.PieData<Integer>("4万以上", map.get("4万以上")));
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
    @RequestMapping(value = "/ll_person", method = RequestMethod.POST)
    public Option personPie(String year) {
        //获取家庭人数
        Map<String, Integer> person = mps.getPerson(Integer.valueOf(year));

        Option option = new Option();
        // 标题

        Title title = new Title()
                .setText("税费统计")
                .setSubtext("税费统计："+year);

        // 提示框
        Tooltip tooltip = new Tooltip()
                .setTrigger(Trigger.item)
                // {a}（系列名称），{b}（类目值），{c}（数值）
                .setFormatter("{a}-{b} : {c}");




        // 图例
        Legend legend = new Legend()
                .setAlign(Align.left)
                .addData("1万以内").addData("2万以内").addData("3万以内").addData("4万以内").addData("5万以内").addData("6万以内").addData("6万以上");

        // 数据
        Series series2 = new Pie().setName("家庭人数")
                .addData(new Pie.PieData<Integer>("1万以内", person.get("1万以内")))
                .addData(new Pie.PieData<Integer>("2万以内", person.get("2万以内")))
                .addData(new Pie.PieData<Integer>("3万以内", person.get("3万以内")))
                .addData(new Pie.PieData<Integer>("4万以内", person.get("4万以内")))
                .addData(new Pie.PieData<Integer>("5万以内", person.get("5万以内")))
                .addData(new Pie.PieData<Integer>("6万以内", person.get("6万以内")))
                .addData(new Pie.PieData<Integer>("6万以上", person.get("6万以上")));
        ((Pie)series2).setCenter("30%", "50%").setRadius("20%");



        // 数据
        Series series4 = new Pie().setName("税费概况")
                .addData(new Pie.PieData<Integer>("最大税费", person.get("max")))
                .addData(new Pie.PieData<Integer>("最小税费", person.get("min")))
                .addData(new Pie.PieData<Integer>("平均税费", person.get("avg")));
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
