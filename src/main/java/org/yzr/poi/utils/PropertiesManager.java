package org.yzr.poi.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PropertiesManager {
    private static Properties props = new Properties();

    static {
        try {
            props.load(PropertiesManager.class.getClassLoader().getResourceAsStream(
                    "config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PropertiesManager() {
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        props.setProperty(key, value);
    }
}