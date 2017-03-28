package org.yzr.poi.utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2016/3/26.
 */
public class FileUtils {

     /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static boolean fileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static void showInFinder() {
        try {
            File file = new File(PropertiesManager.getProperty("basePath"));
            if(file.exists() && file.isDirectory()) {
                java.awt.Desktop.getDesktop().open(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
