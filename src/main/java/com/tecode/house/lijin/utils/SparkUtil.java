package com.tecode.house.lijin.utils;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;

/**
 * 版本：2018/12/7 V1.0
 * 成员：李晋
 */
public class SparkUtil {
    /**
     * 获取sparkContext
     * @return sparkContext
     */
    public static SparkContext getSparkContext() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private static SparkConf conf = new SparkConf().setAppName("SparkUtil").setMaster(ConfigUtil.get("spark_master"));
        private static SparkContext sc = new SparkContext(conf);

        public SparkContext getInstance() {
            return sc;
        }
    }
}
