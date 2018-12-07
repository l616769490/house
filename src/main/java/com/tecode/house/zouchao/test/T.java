package com.tecode.house.zouchao.test;

import java.util.ArrayList;
import java.util.List;

public class T {
    public static void main(String[] args) {
        String a = "15.12";
        int i = (int)Double.parseDouble(a);
        List<String> l = new ArrayList<>();
        l.add("1663-1666");
        l.add("1662-1666");
        System.out.println(l.contains("1"));
    }
}
