package com.tecode.house.lijin.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 创建HBaseBean
 */
public class CreateHBaseBean {
    private static String str = "CONTROL,AGE1,METRO3,REGION,LMED,FMR,L30,L50,L80,IPOV,BEDRMS,BUILT,STATUS,TYPE,VALUE,VACANCY,TENURE,NUNITS,ROOMS,WEIGHT,PER,ZINC2,ZADEQ,ZSMHC,STRUCTURETYPE,OWNRENT,UTILITY,OTHERCOST,COST06,COST12,COST08,COSTMED,TOTSAL,ASSISTED,GLMED,GL30,GL50,GL80,APLMED,ABL30,ABL50,ABL80,ABLMED,BURDEN,INCRELAMIPCT,INCRELAMICAT,INCRELPOVPCT,INCRELPOVCAT,INCRELFMRPCT,INCRELFMRCAT,COST06RELAMIPCT,COST06RELAMICAT,COST06RELPOVPCT,COST06RELPOVCAT,COST06RELFMRPCT,COST06RELFMRCAT,COST08RELAMIPCT,COST08RELAMICAT,COST08RELPOVPCT,COST08RELPOVCAT,COST08RELFMRPCT,COST08RELFMRCAT,COST12RELAMIPCT,COST12RELAMICAT,COST12RELPOVPCT,COST12RELPOVCAT,COST12RELFMRPCT,COST12RELFMRCAT,COSTMedRELAMIPCT,COSTMedRELAMICAT,COSTMedRELPOVPCT,COSTMedRELPOVCAT,COSTMedRELFMRPCT,COSTMedRELFMRCAT,FMTZADEQ,FMTMETRO3,FMTBUILT,FMTSTRUCTURETYPE,FMTBEDRMS,FMTOWNRENT,FMTCOST06RELPOVCAT,FMTCOST08RELPOVCAT,FMTCOST12RELPOVCAT,FMTCOSTMEDRELPOVCAT,FMTINCRELPOVCAT,FMTCOST06RELFMRCAT,FMTCOST08RELFMRCAT,FMTCOST12RELFMRCAT,FMTCOSTMEDRELFMRCAT,FMTINCRELFMRCAT,FMTCOST06RELAMICAT,FMTCOST08RELAMICAT,FMTCOST12RELAMICAT,FMTCOSTMEDRELAMICAT,FMTINCRELAMICAT,FMTASSISTED,FMTBURDEN,FMTREGION,FMTSTATUS";

    public static void main(String[] args) throws IOException {
        String rootPath = System.getProperty("user.dir");
        BufferedWriter bw = new BufferedWriter(new FileWriter(rootPath + "/src/main/java/com/tecode/house/d01/bean/HBaseBean.java"));
        bw.write("package com.tecode.house.d01.bean; \n");
        bw.write("public class HBaseBean { \n");

        String[] fields = str.split(",");
        for (String field : fields) {
            if (field.isEmpty()) {
                continue;
            }
            String fieldUpper = field.substring(0, 1).toUpperCase() + (field.length() > 1 ? field.substring(1, field.length()) : "");

            // 添加字段
            bw.write("private String " + field + ";");


            // 添加get方法
            bw.write("public String get" + fieldUpper + "() { return " + field + ";} \n");
            // 添加set方法
            bw.write("public void set" + fieldUpper + "(String " + field + ") {this." + field + " = " + field + ";}\n");

        }

        bw.write("}");
        bw.close();

    }


}
