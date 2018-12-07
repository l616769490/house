package com.tecode.house.zouchao.test;

import java.util.ArrayList;
import java.util.List;

public class T {
    public static void main(String[] args) {
        String a = "15.12";
        int i = (int)Double.parseDouble(a);
        List<String> l = new ArrayList<>();
        l.add("1661-1666");
        l.add("1662-1666");
        l.add("1663-1666");
        l.add("1664-1666");
        l.add("1665-1666");
        l.add("1666-1666");
        l.add("1667-1666");
        List<String> strings = l.subList(l.size()-5, l.size());
        System.out.println(strings.toString());
    }
}
