package com.tecode.house.zhangzhou.showService;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface ShowTables {
    Table showVacancyTable(TablePost tablePost);
    Table showSingleBuildingTable(TablePost tablePost);
    Table showHouseDutyTable(TablePost tablePost);
}
