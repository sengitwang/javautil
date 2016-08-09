package com.senit.javautil.util;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by sen on 2016/8/9.
 * 读取配置文件
 */
public class PropertiesUtil {
    /**
     * 传入配置文件名称
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("PropName");

    /**
     * 根据key获取配置文件属性
     * @param key
     * @return
     */
    public static String getValue(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
          //  log.info("configUtil获取配置文件属性值为null");
            return null;
        }
    }

    /**
     * 根据key获取配置文件属性，如果key返回值为null，则返回第二个参数
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getValue(String key, String defaultValue) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    public static Enumeration<String> keys() {
        return RESOURCE_BUNDLE.getKeys();
    }

}
