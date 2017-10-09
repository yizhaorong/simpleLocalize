package org.yzr.poi.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Version;
import freemarker.template.Template;
import org.apache.poi.ss.usermodel.*;
import org.yzr.poi.model.CopyWriteContainer;
import org.yzr.poi.model.Localize;

/**
 * Created by yizhaorong on 2017/3/28.
 */
public class ExcelUtils {

    // 读取Excel
    public static List<CopyWriteContainer> read(String filePath) throws Exception {
        List<CopyWriteContainer> copyWriteContainers = new ArrayList<>();

        if(filePath == null || filePath.length() < 1) {
            return copyWriteContainers;
        }

        File file = new File(PropertiesManager.getProperty("basePath"));
        if(file.exists()) {
            FileUtils.deleteDir(file);
        }


        Workbook wb = WorkbookFactory.create(new File(filePath));
        for (Sheet sheet : wb ) {
            List<Localize> list = new ArrayList<>();
            boolean started = false;
            Integer keyColumn = 0;
            Integer keyRow = 0;
            Integer langCount = 0;

            parseExcel(sheet, list, started, keyColumn, keyRow, langCount);

            if (list.size() < 1) {
                break;
            }

            Localize localize = list.get(0);
            int languageCount = localize.getValues().size();

            if (languageCount < 1) {
                break;
            }

            if (!localize.getKey().equalsIgnoreCase(Constant.START_KEY)) {
                break;
            }


            for (int i = 0; i < languageCount; i++) {
                String languageKey = localize.getValues().get(i);
                // 是文案 Key
                if (isLanguageKey(languageKey)) {
                    CopyWriteContainer copyWriteContainer = new CopyWriteContainer();
                    copyWriteContainer.setLanguage(languageKey);



                    for(int l = 1; l < list.size(); l++) {
                        Localize currentLocalize = list.get(l);


                        Localize dataLocalize = new Localize();
                        Localize androidDataLocalize = new Localize();

                        String key = currentLocalize.getKey().trim();
                        String localValue = "";
                        if (currentLocalize.getValues().size() > i) {
                            localValue = currentLocalize.getValues().get(i).trim();
                        }

                        if (localValue.trim().equalsIgnoreCase("")) {
                            if (!key.toLowerCase().equals(Constant.COMMENT_KEY)) {
                                copyWriteContainer.getLostCopyWrites().add(key);
                            }
                            continue;
                        }
                        localValue = localValue.replaceAll("\n", "");
                        localValue = localValue.replaceAll("\"", "\\\\\"");
                        dataLocalize.setKey(key);
                        dataLocalize.putValue(localValue);
                        copyWriteContainer.getCopyWrites().add(dataLocalize);

                        String androidValue = localValue;
                        androidValue = androidValue.replaceAll("&", "&amp;");
                        androidValue = androidValue.replaceAll("<", "&lt;");
                        androidValue = androidValue.replaceAll("'", "\\\\'");
                        androidDataLocalize.setKey(key);
                        androidDataLocalize.putValue(androidValue);
                        copyWriteContainer.getAndroidCopyWrites().add(androidDataLocalize);

                    }
                    copyWriteContainers.add(copyWriteContainer);
                }
            }
            return copyWriteContainers;
        }

        return copyWriteContainers;
    }

    /**
     * 生成动画配置文件
     * @param filePath
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> read4Effects(String filePath) throws Exception {
        List<Map<String, Object>> effects = new ArrayList<>();

        if(filePath == null || filePath.length() < 1) {
            return effects;
        }

        File file = new File(PropertiesManager.getProperty("basePath"));
        if(file.exists()) {
            FileUtils.deleteDir(file);
        }

        Workbook wb = WorkbookFactory.create(new File(filePath));
        for (Sheet sheet : wb ) {
            List<Localize> list = new ArrayList<>();
            boolean started = false;
            Integer keyColumn = 0;
            Integer keyRow = 0;
            Integer langCount = 0;

            parseExcel(sheet, list, started, keyColumn, keyRow, langCount);

            if (list.size() < 1) {
                break;
            }

            Localize localize = list.get(0);
            int languageCount = localize.getValues().size();

            if (languageCount < 1) {
                break;
            }

            if (!localize.getKey().equalsIgnoreCase(Constant.START_KEY)) {
                break;
            }

            for (int i = 1; i < list.size(); i++) {
                Localize currentLocalize = list.get(i);
                String key = currentLocalize.getKey();
                if (key.equalsIgnoreCase(Constant.COMMENT_KEY)) {
                    continue;
                }
                Map<String , Object> keyWords = new HashMap<>();
                List<Map<String , Object>> words = new ArrayList<>();
                keyWords.put("words", words);
                keyWords.put("image", currentLocalize.getKey());

                for (int j = 0; j < languageCount; j++) {
                    String localValue = "";
                    if (currentLocalize.getValues().size() > j) {
                        localValue = currentLocalize.getValues().get(j).trim();
                    }
                    localValue = localValue.replaceAll("\n", "");
                    localValue = localValue.replaceAll("\"", "\\\\\"");
                    String languageKey = localize.getValues().get(j);
                    if (!localValue.equalsIgnoreCase("") && !languageKey.equalsIgnoreCase(Constant.SIMPLIFIED_CHINESE)) {
                        Map<String , Object> word = new HashMap<>();
                        word.put("language", languageKey);
                        word.put("word", localValue);
                        words.add(word);
                    }
                }

                effects.add(keyWords);
            }
            return effects;
        }

        return effects;
    }

    /**
     * 解析 Excel
     * @param sheet
     * @param list
     * @param started
     * @param keyColumn
     * @param keyRow
     * @param langCount
     */
    private static void parseExcel(Sheet sheet, List<Localize> list, boolean started, Integer keyColumn, Integer keyRow, Integer langCount) {
        for (Row row : sheet) {
            Localize localize = new Localize();
            Integer lastColumn = 0;
            for (Cell cell : row) {
                if(!started && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    if (Constant.START_KEY.equalsIgnoreCase(cell.getStringCellValue())) {
                        started = true;
                        keyColumn = cell.getColumnIndex();
                        keyRow = row.getRowNum();
                    } else {
                        continue;
                    }
                }
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_BLANK:
                    case Cell.CELL_TYPE_BOOLEAN:
                    case Cell.CELL_TYPE_ERROR:
                    case Cell.CELL_TYPE_FORMULA:
                    case Cell.CELL_TYPE_NUMERIC:
                    case Cell.CELL_TYPE_STRING:
                        String value = "";
                        try {
                            value = cell.getStringCellValue();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        int columnIndex = cell.getColumnIndex();
                        if (row.getRowNum() > keyRow && columnIndex - lastColumn > 1 && columnIndex < (keyColumn + langCount)) {
                            for (int i = lastColumn + 1; i < columnIndex; i++) {
                                localize.putValue("");
                            }
                        }
                        lastColumn = columnIndex;
                        if (row.getRowNum() == keyRow && !value.trim().equalsIgnoreCase("") && isLanguageKey(value)) {
                            langCount = columnIndex - keyColumn;
                        }

                        if (Constant.END_KEY.equalsIgnoreCase(value)) {
                            started = false;
                            continue;

                        } else if (keyColumn == columnIndex && !value.trim().equalsIgnoreCase("")) {
                            localize.setKey(value);
                        } else if(columnIndex > keyColumn && localize.getKey() != null) {
                            localize.putValue(value);
                        }
                        break;
                    default:
                        break;
                }
                // Do something here
            }
            if(localize.getKey() != null) {
                list.add(localize);
            }

        }
    }


    public static void generate(CopyWriteContainer copyWriteContainer, List<CopyWriteContainer> defaultCopyWriteContainers) throws Exception  {
        String language = copyWriteContainer.getLanguage();
//        if (language.equals(Constant.SIMPLIFIED_CHINESE)) {
//            Boolean ignoreChinese = Boolean.valueOf(PropertiesManager.getProperty(Constant.IGNORE_CHINESE));
//            if (ignoreChinese) return;
//        }

        Boolean useDefaultValue = new Boolean(PropertiesManager.getProperty(Constant.USE_DEFAULT_VALUE));
        if (useDefaultValue) {
            for (String key : copyWriteContainer.getLostCopyWrites()) {
                String defaultValue = getDefaultValue(key, defaultCopyWriteContainers);
                Localize localize = new Localize();
                localize.setKey(key);
                localize.putValue(defaultValue);

                String androidDefaultValue = getAndroidDefaultValue(key, defaultCopyWriteContainers);
                Localize androidLocalize = new Localize();
                androidLocalize.setKey(key);
                androidLocalize.putValue(androidDefaultValue);

                copyWriteContainer.getCopyWrites().add(localize);
                copyWriteContainer.getAndroidCopyWrites().add(androidLocalize);
            }
        }

        Boolean iOSIsOpen = new Boolean(PropertiesManager.getProperty(Constant.IOS_SWITCH));
        if (iOSIsOpen) {
            createLocalizeFile(language, Constant.IOS_KEY, copyWriteContainer.getCopyWrites());
        }

        Boolean serverIsOpen = new Boolean(PropertiesManager.getProperty(Constant.SERVER_SWITCH));
        if (serverIsOpen) {
            createLocalizeFile(language, Constant.SERVER_KEY, copyWriteContainer.getCopyWrites());
        }

        Boolean androidIsOpen = new Boolean(PropertiesManager.getProperty(Constant.ANDROID_SWITCH));
        if (androidIsOpen) {
            createLocalizeFile(language, Constant.ANDROID_KEY, copyWriteContainer.getAndroidCopyWrites());
        }
    }

    public static void createEffectsFile(List<Map<String, Object>> effetcs) {
        FileWriter writer;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(effetcs);
            String filePath = PropertiesManager.getProperty("basePath") + File.separator + "chatEffects.json";
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            writer = new FileWriter(file);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * 生成本地化文件
     * @param code
     * @param language
     * @param localizes
     * @return
     * @throws Exception
     */
    private static void createLocalizeFile(String language, String code, List<Localize> localizes) throws Exception {
        Writer out = null;
        try {
            out = new StringWriter();
            String filePath = null;
            String basePath = PropertiesManager.getProperty("basePath") + File.separator + PropertiesManager.getProperty(code) + File.separator;
            if(code.equalsIgnoreCase(Constant.IOS_KEY)) {
                filePath = basePath +language+ ".lproj" + File.separator + PropertiesManager.getProperty(code+"FileName");
            } else if(code.equalsIgnoreCase(Constant.ANDROID_KEY)) {
                if (language.equals("en")) {
                    Boolean ignoreEnglishSuffix = Boolean.valueOf(PropertiesManager.getProperty(Constant.IGNORE_ENGLISH_SUFFIX));
                    if (ignoreEnglishSuffix) {
                        language = "";
                    } else {
                        language = "en";
                    }
                } else if (language.equals("zh-CN")) {
                    language = "zh-rCN";
                } else if (language.equals("zh-TW")) {
                    language = "zh-rTW";
                } else if (language.equals("id")) {
                    Boolean fixIdLanguage = Boolean.valueOf(PropertiesManager.getProperty(Constant.FIX_ID_LANGUAGE));
                    if (fixIdLanguage) {
                        language = "in";
                    }
                }

                String values = "values-";
                if (language.length() < 1) {
                    values = "values";
                }
                filePath = basePath + values + language + File.separator + PropertiesManager.getProperty(code+"FileName");

            } else if (code.equalsIgnoreCase(Constant.SERVER_KEY)) {
                filePath = basePath + PropertiesManager.getProperty(code+"FileName") + language + ".properties";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Configuration cfg = new Configuration(new Version(2, 3, 21));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(ExcelUtils.class, "/templete");
            Template template = cfg.getTemplate(code + ".ftl");
            // 静态页面要存放的路径
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            // 处理模版 map数据 ,输出流
            Map<String, List<Localize>> dataModel = new HashMap<>();
            dataModel.put("list", localizes);
            template.process(dataModel, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /***
     * 是否是语言Key
     * @param key
     * @return true or false
     */
    private static boolean isLanguageKey(String key) {
        Pattern pattern = Pattern.compile(Constant.LANGUAGE_EX);
        Matcher matcher = pattern.matcher(key);
        return matcher.matches();
    }

    private static String getDefaultValue(String key, List<CopyWriteContainer> defaultCopyWriteContainers) {
        for (CopyWriteContainer copyWriteContainer:defaultCopyWriteContainers) {
            for (Localize localize : copyWriteContainer.getCopyWrites()) {
                if (localize.getKey().equals(key)) {
                    return localize.getValue();
                }
            }
        }

        return PropertiesManager.getProperty(Constant.DEFAULT_VALUE);
    }

    private static String getAndroidDefaultValue(String key, List<CopyWriteContainer> defaultCopyWriteContainers) {
        for (CopyWriteContainer copyWriteContainer:defaultCopyWriteContainers) {
            for (Localize localize : copyWriteContainer.getAndroidCopyWrites()) {
                if (localize.getKey().equals(key)) {
                    return localize.getValue();
                }
            }
        }

        return PropertiesManager.getProperty(Constant.DEFAULT_VALUE);
    }
}
