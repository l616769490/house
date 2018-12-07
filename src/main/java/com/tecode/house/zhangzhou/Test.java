package com.tecode.house.zhangzhou;


import com.tecode.house.zhangzhou.showService.ShowTables;

public class Test {
    public static void main(String[] args) {
        ShowTables st = new ShowTables();
        System.out.println("==============空置=============");
        st.showSingleBuildingTable(2013,3,"二线城市","独栋");
        System.out.println("==============独栋=============");
        st.showVacancyTable(2013,11,"空置");
        System.out.println("==============房产税=============");
        st.showHouseDutyTable(2013,2,"五线城市","其他");
    }
}
