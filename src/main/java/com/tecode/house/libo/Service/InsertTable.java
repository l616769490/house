package com.tecode.house.libo.Service;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface InsertTable {
    Table insertSelfTable(TablePost tablePost);

    Table insertRoomTable(TablePost tablePost);

    Table insertSingleTable(TablePost tablePost);
}
