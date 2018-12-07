package com.tecode.house.jianchenfei.test;

import com.tecode.house.jianchenfei.service.HBaseServer;
import com.tecode.table.Row;
import com.tecode.table.Table;
import com.tecode.table.TablePost;

/**
 * Created by Administrator on 2018/12/7.
 */
public class TestTable {

    private static final String path = TestTable.class.getResource("/basics-per.xml").getPath();

    public static void main(String[] args) {
        test1();
    }

    /**
     * 家庭人数统计
     */
    private static void test1() {
        TablePost tp = new TablePost();
        tp.setPage(2);
        tp.setYear(2013);

        Table table = new HBaseServer(path, "家庭人数统计", tp).select();
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
