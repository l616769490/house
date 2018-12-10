package com.tecode.house.zhangzhou;


import com.tecode.house.zhangzhou.service.SparkService;
import com.tecode.house.zhangzhou.showService.ShowTablesImpl;
import com.tecode.table.Search;
import com.tecode.table.TablePost;

import java.util.ArrayList;
import java.util.List;

public class Test {
    /*public static void main(String[] args) {
        ShowTablesImpl st = new ShowTablesImpl();
        System.out.println("==============空置=============");
        st.showSingleBuildingTable(2013,3,"二线城市","独栋");
        System.out.println("==============独栋=============");
        st.showVacancyTable(2013,11,"空置");
        System.out.println("==============房产税=============");
        st.showHouseDutyTable(2013,2,"五线城市","其他");
    }*/


    public static void main(String[] args) {
        /*SparkService ss = new SparkService();
        ss.selectVacancyTable("thads:2013","居住",3);*/
        ShowTablesImpl st = new ShowTablesImpl();
        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);
        List<Search> list = new ArrayList<>();
        list.add(new Search().setTitle("空置").addValue("空置").addValue("居住"));
        tp.setSearches(list);
        st.showVacancyTable(tp);
    }
}
