package com.tecode.house.lijin.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TestMapToList {

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < 1024000; i++) {
            map.put(i, "test" + i);
        }

        long t1 = System.currentTimeMillis();
        new ArrayList<>(map.values());
        long t2 = System.currentTimeMillis();
        new ArrayList<>(map.keySet());
        long t3 = System.currentTimeMillis();
        map.keySet().stream().collect(Collectors.toList());
        long t4 = System.currentTimeMillis();
        map.values().stream().collect(Collectors.toList());
        long t5 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
        System.out.println(t4 - t3);
        System.out.println(t5 - t4);


    }
}
