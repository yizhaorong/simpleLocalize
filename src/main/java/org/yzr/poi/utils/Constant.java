package org.yzr.poi.utils;

import java.net.URL;

/**
 * Created by yizhaorong on 2017/3/28.
 */
public class Constant {
    // 类路径URL
    public static final URL CLASS_URL = Constant.class.getResource("");
    // 类路径
    public static final String CLASS_PATH = CLASS_URL.getPath();

    public static final String LANGUAGE_EX = "([a-zA-Z])+[-]?([a-zA-Z])*";
    public static final String SIMPLIFIED_CHINESE = "zh-CN";
    public static final String START_KEY = "[key]";
    public static final String END_KEY = "[end]";
    public static final String COMMENT_KEY = "[comment]";

    public static final String IOS_KEY = "iOS";
    public static final String ANDROID_KEY = "Android";
    public static final String SERVER_KEY = "JAVA";

    public static final String IGNORE_ENGLISH_SUFFIX = "ignoreEnglishSuffix";
    public static final String IGNORE_CHINESE = "ignoreChinese";
    public static final String FIX_ID_LANGUAGE = "fixIdLanguage";
    public static final String USE_NEW_ANDROID = "useNewAndroid";

    public static final String IOS_SWITCH = "iOSOpen";
    public static final String ANDROID_SWITCH = "AndroidOpen";
    public static final String SERVER_SWITCH = "serverOpen";

    public static final String USE_DEFAULT_VALUE = "useDefaultValue";
    public static final String DEFAULT_VALUE = "defaultValue";

    public static final String TRUE = "true";
    public static final String FALSE = "false";
}
