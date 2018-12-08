package com.tecode.house.zxl.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.Map;

public interface MaketPriceServer {

    boolean intoMysql(String year);

    Table getValueTable(int year);

    Table getValueTable(TablePost tablePost);

    Map<String,Integer> getMaket();

    Table getPersonTable(int year);

    Table getPersonTable(TablePost tablePost);

    Table getIncomeTable(int year);

    Table getIncomeTable(TablePost tablePost);

    Map<String, Integer> getincome();

    Map<String, Integer> getPerson();
}
