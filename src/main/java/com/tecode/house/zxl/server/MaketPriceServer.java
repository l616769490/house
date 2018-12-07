package com.tecode.house.zxl.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

import java.util.Map;

public interface MaketPriceServer {

    void getValue();

    Table getTable();

    Table getTable(TablePost tablePost);

    Map<String,Integer> getMaket();
}
