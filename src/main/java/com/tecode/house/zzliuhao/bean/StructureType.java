package com.tecode.house.zzliuhao.bean;

/**
 * Created by Administrator on 2018/12/3.
 */
public class StructureType {
    private String SingleFamily;
    private String SecondsFamily;
    private String ThreadFamily;
    private String FourFamily;
    private String FiveFamily;
    private String mobleFamily;

    public StructureType(String singleFamily, String secondsFamily, String threadFamily, String fourFamily, String fiveFamily, String mobleFamily) {
        SingleFamily = singleFamily;
        SecondsFamily = secondsFamily;
        ThreadFamily = threadFamily;
        FourFamily = fourFamily;
        FiveFamily = fiveFamily;
        this.mobleFamily = mobleFamily;
    }

    public String getSingleFamily() {
        return SingleFamily;
    }

    public String getSecondsFamily() {
        return SecondsFamily;
    }

    public String getThreadFamily() {
        return ThreadFamily;
    }

    public String getFourFamily() {
        return FourFamily;
    }

    public String getFiveFamily() {
        return FiveFamily;
    }

    public String getMobleFamily() {
        return mobleFamily;
    }

    public void setSingleFamily(String singleFamily) {
        SingleFamily = singleFamily;
    }

    public void setSecondsFamily(String secondsFamily) {
        SecondsFamily = secondsFamily;
    }

    public void setThreadFamily(String threadFamily) {
        ThreadFamily = threadFamily;
    }

    public void setFourFamily(String fourFamily) {
        FourFamily = fourFamily;
    }

    public void setFiveFamily(String fiveFamily) {
        FiveFamily = fiveFamily;
    }

    public void setMobleFamily(String mobleFamily) {
        this.mobleFamily = mobleFamily;
    }
}
