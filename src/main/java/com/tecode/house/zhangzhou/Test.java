package com.tecode.house.zhangzhou;


import com.tecode.house.zhangzhou.showService.ShowTables;

public class Test {
    public static void main(String[] args) {
        ShowTables st = new ShowTables();
        st.showSingleBuildingTable(2013,3,"二线城市","独栋");
        System.out.println("===========================");
        st.showVacancyTable(2013,11,"空置");

    }
}
