package com.tecode.house.lijin.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
/**
 * 读取配置文件
 * <br>
 * @date: 2018/10/25
 * @version: V1.0
 * @author: 李晋
 */
public class ConfigUtil {
    private static Properties props = new Properties();

    static{
        try ( InputStreamReader is = new InputStreamReader(ConfigUtil.class.getResourceAsStream("/conf.properties"), "UTF-8")){
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件
     * @param key   键
     * @return  值
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

}
