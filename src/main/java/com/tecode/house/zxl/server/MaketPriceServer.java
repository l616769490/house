package com.tecode.house.zxl.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.Map;

public interface MaketPriceServer {

    boolean intoMysql(String year);

    Table getValueTable();

    Table getValueTable(TablePost tablePost);

    Map<String,Integer> getMaket();

    Table getPersonTable();

    Table getPersonTable(TablePost tablePost);

    Table getIncomeTable();

    Table getIncomeTable(TablePost tablePost);

    Map<String, Integer> getincome();

    Map<String, Integer> getPerson();
}
