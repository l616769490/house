package com.tecode.house.lijun.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        String path = ConfigUtil.class.getClassLoader()
                .getResource("conf.properties").getPath();
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
