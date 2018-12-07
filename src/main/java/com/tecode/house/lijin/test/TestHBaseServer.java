package com.tecode.house.lijin.test;

import com.tecode.house.lijin.service.HBaseServer;
import com.tecode.table.Row;
import com.tecode.table.Search;
import com.tecode.table.Table;
import com.tecode.table.TablePost;
import org.apache.spark.sql.execution.SortPrefixUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 测试HBaseserver
 * 版本：2018/12/7 V1.0
 * 成员：李晋
 */
public class TestHBaseServer {
    private static String path1 = TestHBaseServer.class.getResource("/table/basics-rooms.xml").getPath();
    private static String path2 = TestHBaseServer.class.getResource("/table/region-zinx2.xml").getPath();
    private static String path3 = TestHBaseServer.class.getResource("/table/region-zsmhc.xml").getPath();

    public static void main(String[] args) {
//        test1();
        test3();
    }

    /**
     * 测试基础-房间数
     */
    private static void test1() {
        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);
        List<Search> searches = new ArrayList<>();
        tp.setSearches(searches);

        Search s1 = new Search();
        s1.setTitle("房间数");
        s1.setValues(Arrays.asList("5"));
        searches.add(s1);

        Search s2 = new Search();
        s2.setTitle("卧室数");
        s2.setValues(Arrays.asList("2"));
        searches.add(s2);

        Table table = new HBaseServer(path1, "基础-房间数分析", tp).select();
        for (String top : table.getTop()) {
            System.out.print(top + "\t\t");
        }
        System.out.println();
        for (Row row : table.getData()) {
            for (String s : row.getRow()) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }


    /**
     * 按区域-家庭收入分析
     */
    private static void test2() {
        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);
        List<Search> searches = new ArrayList<>();
        tp.setSearches(searches);

        Search s1 = new Search();
        s1.setTitle("1");
        s1.setValues(Arrays.asList("L30"));
        searches.add(s1);

        Search s2 = new Search();
        s2.setTitle("2");
        s2.setValues(Arrays.asList("L50"));
        searches.add(s2);

        Search s3 = new Search();
        s3.setTitle("3");
        s3.setValues(Arrays.asList("贫困线"));
        searches.add(s3);

        Search s4 = new Search();
        s4.setTitle("4");
        s4.setValues(Arrays.asList("L80+"));
        searches.add(s4);

        Table table = new HBaseServer(path2, "按区域-家庭收入分析", tp).select();
        for (String top : table.getTop()) {
            System.out.print(top + "\t\t");
        }
        System.out.println();
        for (Row row : table.getData()) {
            for (String s : row.getRow()) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 按区域-房产税分析
     */
    private static void test3() {
        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);
        List<Search> searches = new ArrayList<>();
        tp.setSearches(searches);

        Search s1 = new Search();
        s1.setTitle("1");
        s1.setValues(Arrays.asList("0-500"));
        searches.add(s1);

        Search s2 = new Search();
        s2.setTitle("2");
        s2.setValues(Arrays.asList("500-1000"));
        searches.add(s2);

        Search s3 = new Search();
        s3.setTitle("3");
        s3.setValues(Arrays.asList("1000-1500"));
        searches.add(s3);

        Search s4 = new Search();
        s4.setTitle("4");
        s4.setValues(Arrays.asList("1500-2000"));
        searches.add(s4);

        Table table = new HBaseServer(path3, "按区域-房产税分析", tp).select();
        for (String top : table.getTop()) {
            System.out.print(top + "\t\t");
        }
        System.out.println();
        for (Row row : table.getData()) {
            for (String s : row.getRow()) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }
}
