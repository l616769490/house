package com.tecode.house.zxl.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.Map;

public interface MaketPriceServer {


    Table getValueTable();

    Table getValueTable(TablePost tablePost);

    Map<String,Integer> getMaket(int year);

    Table getPersonTable();

    Table getPersonTable(TablePost tablePost);

    Table getIncomeTable();

    Table getIncomeTable(TablePost tablePost);

    Map<String, Integer> getincome(int year);

    Map<String, Integer> getPerson(int year);
}
