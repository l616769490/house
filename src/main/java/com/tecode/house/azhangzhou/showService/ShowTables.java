package com.tecode.house.azhangzhou.showService;

import com.tecode.table.Table;
import com.tecode.table.TablePost;

public interface ShowTables {
    Table showVacancyTable(TablePost tablePost);
    Table showSingleBuildingTable(TablePost tablePost);
    Table showHouseDutyTable(TablePost tablePost);
}
