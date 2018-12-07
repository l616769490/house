package com.tecode.house.liuhao.server;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

/**
 * Created by Administrator on 2018/12/7.
 */
public interface getTableServersDao {

    Table getCityTax(TablePost tablePost);

    Table getStructureType(TablePost tablePost);


}
