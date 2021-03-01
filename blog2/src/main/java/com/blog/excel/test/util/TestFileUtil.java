package com.blog.excel.test.util;

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
}
