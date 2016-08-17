package org.yzr.poi;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;
import org.apache.poi.ss.usermodel.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

import org.yzr.poi.model.Localizable;
import org.yzr.poi.ui.ImageButton;
import org.yzr.poi.ui.ImageFrame;
import org.yzr.poi.ui.StateButton;
import org.yzr.poi.utils.FileChooser;
import org.yzr.poi.utils.FileUtils;
import org.yzr.poi.utils.PropertiesManager;

import javax.swing.*;

/**
 * App
 *
 */

    public class App {
    private static final String START_KEY = "[key]";
    private static final String END_KEY = "[end]";

    private static final String IOS_SWITCH = "iOSOpen";
    private static final String ANDROID_SWITCH = "AndroidOpen";
    private static final String SERVER_SWITCH = "serverOpen";

    private static final String IOS_KEY = "iOS";
    private static final String ANDROID_KEY = "Android";
    private static final String SERVER_KEY = "JAVA";
    
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private static boolean processing = false;

    // 初始化一个frame,并设置title为"SimpleLocalizable"
    private JFrame frm;
    // 打开文件按钮
    private JButton  openFileBtn;
    private JButton generateBtn;
    private JLabel filePathLabel;

    private  DragAdapter dropTargetAdapter = new DragAdapter();

      //下面是一个构造方法
    public void init() throws Exception {
        // 初始化一个frame,并设置title为"SimpleLocalizable"
        frm = new ImageFrame("SimpleLocalizable");

        new DropTarget(frm,DnDConstants.ACTION_COPY_OR_MOVE,this.dropTargetAdapter);
        openFileBtn = new ImageButton("images/openFile.png", "images/openFile.png");
        openFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    openFile();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        int openFileBtnX = frm.getWidth()  - openFileBtn.getWidth() - 40;
        int openFileBtnY = 50;
        openFileBtn.setLocation(openFileBtnX, openFileBtnY);
        frm.add(openFileBtn);

        filePathLabel = new JLabel();
        filePathLabel.setFont(new Font(null, 0, 25));
        filePathLabel.setVisible(false);
        filePathLabel.setForeground(Color.white);
        filePathLabel.setSize(openFileBtn.getWidth() * 2, openFileBtn.getHeight());
        filePathLabel.setLocation(183, 75);
        frm.add(filePathLabel);

        generateBtn = new ImageButton("images/generate.png", "images/generate.png");
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (this) {
                    try {
                        if (processing) return;
                        processing = true;
                        readExcel();
                        String filePath = filePathLabel.getText();
                        if (filePath != null && filePath.length() > 0) {
                            FileUtils.showInFinder();
                        }
                        processing = false;
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        processing = false;
                    }
                }

            }
        });
        generateBtn.setLocation(920, 323);
        generateBtn.setVisible(true);
        frm.add(generateBtn);

        final StateButton iOSButton = new StateButton("images/iOS", "images/iOS_disable");
        iOSButton.setLocation(60, 300);
        iOSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iOSButton.setSelected(!iOSButton.isSelected());
                PropertiesManager.setProperty(IOS_SWITCH, iOSButton.isSelected() ? FALSE : TRUE);
            }
        });
        iOSButton.setSelected(!PropertiesManager.getProperty(IOS_SWITCH).equals(TRUE));
        frm.add(iOSButton);

        final StateButton androidButton = new StateButton("images/Android", "images/Android_disable");
        androidButton.setLocation(350, 300);
        androidButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                androidButton.setSelected(!androidButton.isSelected());
                PropertiesManager.setProperty(ANDROID_SWITCH, androidButton.isSelected() ? FALSE : TRUE);
            }
        });
        androidButton.setSelected(!PropertiesManager.getProperty(ANDROID_SWITCH).equals(TRUE));
        frm.add(androidButton);

        final StateButton serverButton = new StateButton("images/server", "images/server_disable");
        serverButton.setLocation(640, 300);
        serverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverButton.setSelected(!serverButton.isSelected());
                PropertiesManager.setProperty(SERVER_SWITCH, serverButton.isSelected() ? FALSE : TRUE);
            }
        });
        serverButton.setSelected(!PropertiesManager.getProperty(SERVER_SWITCH).equals(TRUE));
        frm.add(serverButton);
        //设置框架可见.
        frm.setVisible(true);
    }

    public static void main( String[] args ) throws Exception
    {
        App app = new App();
        app.init();
    }

    /***
     * 打开文件选择器
     */
    public void openFile() {
        File excelFile = FileChooser.getFileFromFileChooser();
        if(excelFile == null) return;
        this.setFilePath(excelFile.getAbsolutePath());
    }

    /**
     * 设置文件路径
     * @param absolutePath
     */
    private void setFilePath(String absolutePath) {
        this.filePathLabel.setText(absolutePath);
        this.filePathLabel.setVisible(true);
    }

    /***
     * 读取Excel
     * @throws Exception
     */
    public void readExcel() throws Exception {
        String filePath = filePathLabel.getText();
        if(filePath == null || filePath.length() < 1) {
            return;
        }
        Workbook wb = WorkbookFactory.create(new File(filePath));

        Locale locales[] = Locale.getAvailableLocales();
        Set<String> languages = new HashSet<>();
        for (int i = 0; i < locales.length; i++) {
            Locale locale = locales[i];
            if(locale.getCountry() != null && !locale.getCountry().trim().equals("")) {
                languages.add(locale.getLanguage() +"_"+ locale.getCountry());
            }
            languages.add(locale.getLanguage());
        }

        File file = new File(PropertiesManager.getProperty("basePath"));
        if(file.exists()) {
            FileUtils.deleteDir(file);
        }
        for (Sheet sheet : wb ) {
            List<Localizable> list = new ArrayList<Localizable>();
            boolean started = false;
            Integer keyColumn = 0;
            for (Row row : sheet) {
                Localizable localizable = new Localizable();
                for (Cell cell : row) {
                    if(!started && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                        if (START_KEY.equalsIgnoreCase(cell.getStringCellValue())) {
                            started = true;
                            keyColumn = cell.getColumnIndex();
                        } else {
                            continue;
                        }
                    }
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BLANK:
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            break;
                        case Cell.CELL_TYPE_ERROR:
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            break;
                        case Cell.CELL_TYPE_STRING:
                            if (END_KEY.equalsIgnoreCase(cell.getStringCellValue())) {
                                started = false;
                                continue;
                            }
                            if (keyColumn == cell.getColumnIndex()) {
                                localizable.setKey(cell.getStringCellValue());
                            } else {
                                localizable.putValue(cell.getStringCellValue());
                            }
                            break;
                        default:
                            break;
                    }
                    // Do something here
                }
                if(localizable.getKey() != null) {
                    list.add(localizable);
                }

            }
            if (list.size() > 0) {
                Localizable localizable = list.get(0);
                if(localizable.getKey().equalsIgnoreCase(START_KEY)) {
                    int valueLength = localizable.getValues().size();
                    if (valueLength > 0) {
                        for (int i = 0; i < valueLength; i++) {
                            String value = localizable.getValues().get(i);
                            if (languages.contains(value)) {
                                Map<String, Object> dataModel = new HashMap<>();
                                Map<String, Object> androidDataModel = new HashMap<>();

                                List<Localizable> currentLocalizables = new ArrayList<>();
                                List<Localizable> androidCurrentLocalizables = new ArrayList<>();
                                for(int l = 1; l < list.size(); l++) {
                                    Localizable currentLocalizable = list.get(l);
                                    Localizable dataLocalizable = new Localizable();

                                    Localizable androidDataLocalizable = new Localizable();
                                    if(currentLocalizable.getValues().size() >= i) {
                                        String key = currentLocalizable.getKey();
                                        String localValue = currentLocalizable.getValues().get(i);

                                        dataLocalizable.setKey(key);
                                        dataLocalizable.putValue(localValue);
                                        currentLocalizables.add(dataLocalizable);

                                        String androidValue = localValue;
                                        androidValue = androidValue.replaceAll("&", "&amp;");
                                        androidValue = androidValue.replaceAll("<", "&lt;");
                                        androidValue = androidValue.replaceAll(">", "&gt;");
                                        androidValue = androidValue.replaceAll("'", "&apos;");
                                        androidValue = androidValue.replaceAll("\"", "&quot;");
                                        androidDataLocalizable.setKey(key);
                                        androidDataLocalizable.putValue(androidValue);
                                        androidCurrentLocalizables.add(androidDataLocalizable);
                                    }
                                }
                                dataModel.put("list", currentLocalizables);
                                androidDataModel.put("list", androidCurrentLocalizables);
                                if(PropertiesManager.getProperty(IOS_SWITCH).equals(TRUE)){
                                    generate(IOS_KEY, value, dataModel);
                                }
                                if(PropertiesManager.getProperty(ANDROID_SWITCH).equals(TRUE)) {
                                    generate(ANDROID_KEY, value, androidDataModel);
                                }
                                if(PropertiesManager.getProperty(SERVER_SWITCH).equals(TRUE)) {
                                    generate(SERVER_KEY, value, dataModel);
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            break;
        }
    }

    /***
     * 生成本地化文件
     * @param code
     * @param language
     * @param dataModel
     * @return
     * @throws Exception
     */
    public String generate(String code, String language, Map<String, Object> dataModel) throws Exception  {
        Writer out = null;
        try {
            out = new StringWriter();
            String filePath = null;
            String basePath = PropertiesManager.getProperty("basePath") + File.separator + PropertiesManager.getProperty(code) + File.separator;
            if(code.equalsIgnoreCase(IOS_KEY)) {
                filePath = basePath +language+ ".lproj" + File.separator + PropertiesManager.getProperty(code+"FileName");
            } else if(code.equalsIgnoreCase(ANDROID_KEY)) {
                filePath = basePath +"values-" + language + File.separator + PropertiesManager.getProperty(code+"FileName");
            } else if (code.equalsIgnoreCase(SERVER_KEY)) {
                filePath = basePath + PropertiesManager.getProperty(code+"FileName") + language + ".properties";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Configuration cfg = new Configuration(new Version(2, 3, 21));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(App.class, "/templete");
            Template template = cfg.getTemplate(code + ".ftl");
            // 静态页面要存放的路径
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            // 处理模版 map数据 ,输出流
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
        return null;
    }

    /***
     * 拖拽适配器
     */
    private class DragAdapter extends DropTargetAdapter {

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                Transferable tf=dtde.getTransferable();
                if(tf.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List<File> lt=(List<File>)tf.getTransferData(DataFlavor.javaFileListFlavor);
                    for (int i = 0; i < lt.size(); i++) {
                        File file=lt.get(i);
                        String absolutePath = file.getAbsolutePath();

                        System.out.println(file.getAbsoluteFile());
                        if (!file.isDirectory()) {
                            String fileExt = absolutePath.substring(absolutePath.lastIndexOf(".")).toLowerCase();
                            if (Arrays.asList(FileChooser.fileExtNames).contains(fileExt)) {
                                setFilePath(absolutePath);
                                dtde.dropComplete(true);
                                return;
                            }
                        }

                    }
                    dtde.rejectDrop();

                } else {
                    dtde.rejectDrop();
                }
            } catch(Exception e) { }
        }
    }

}
