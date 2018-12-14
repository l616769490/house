package com.tecode.house.liaolian.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.Map;

public interface LLMaketPriceServer {



    Table getValueTable(TablePost tablePost);

    Map<String,Integer> getMaket(int year);


    Table getPersonTable(TablePost tablePost);


    Table getIncomeTable(TablePost tablePost);

    Map<String, Integer> getincome(int year);

    Map<String, Integer> getPerson(int year);
}
