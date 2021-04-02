package com.blog.excel.easyExcel.util.test;

import java.io.File;
import java.io.InputStream;

/**
 * @author lt
 * @date 2021/3/1 10:52
 */
public class TestFileUtil {
    public static  InputStream getResourcesFileInput(String fileName){
        return  Thread.currentThread().getContextClassLoader().getResourceAsStream(""+fileName);
    }

    public static String getPath2(){
        return  TestFileUtil.class.getResource("/").getPath();
    }

    public static String getPath(){
        return  "E:\\data\\file\\";
    }
    public static String getImage(){
        return  "E:\\data\\image\\";
    }

    public static File createNewFile(String pathName) {
        File file = new File(getPath() + pathName);
        if (file.exists()) {
            file.delete();
        } else {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
        return file;
    }

    public static File readFile(String pathName) {
        return new File(getPath() + pathName);
    }

    public static File readUserHomeFile(String pathName) {
        return new File(System.getProperty("user.home") + File.separator + pathName);
    }
}
