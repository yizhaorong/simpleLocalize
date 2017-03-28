package org.yzr.poi.utils;

import java.io.*;
import java.util.Properties;

/**
 * Created by Administrator on 2016/3/26.
 */
public class PropertiesManager {
    // 配置文件路径
    private static final String CONFIG_FILE_NAME = "/config/config.properties";
    // 内存中的配置
    private static Properties props = new Properties();

    static {
        InputStream inputStream = null;
        try {
            String userHome = System.getProperty("user.home");
            String configPath = userHome + CONFIG_FILE_NAME;
            if (FileUtils.fileExist(configPath)) {
                // 初始化
                inputStream = new FileInputStream(new File(configPath));
            } else {
                // 初始化
                inputStream = PropertiesManager.class.getResourceAsStream(CONFIG_FILE_NAME);
            }

            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {

            }

        }
    }

    private PropertiesManager() {
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        OutputStream outputStream = null;
        try {
            // 获取输出流
            String userHome = System.getProperty("user.home");
            String configPath = userHome + CONFIG_FILE_NAME;
            File file = new File(configPath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            // 设置属性
            props.setProperty(key, value);
            // 存储
            props.store(outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {

            }

        }

    }
}