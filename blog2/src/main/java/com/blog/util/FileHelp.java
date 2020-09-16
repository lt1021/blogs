package com.blog.util;



import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileHelp {
    /**
     * 这个函数的功能是获取前端的数据集合，将文件打包成File以便后续操作
     */
    public static List<FileItem> getRequsetFileItems(HttpServletRequest  req, ServletContext context){
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            String str = "javax.servelet.context.tempdir";
            File file = (File) context.getAttribute(str);
            factory.setRepository(file);
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                return upload.parseRequest(req);
            } catch (FileUploadException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }

    }

    /**
     * 这个函数的功能是获取当前时间点与1970年的间隔秒数
     * @param date
     * @return
     */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return  0;
        }
        String timestamp = String.valueOf(date.getTime());
        System.out.println(timestamp);
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        }else{
            return 0;
        }

    }
    public static String getPhotoNewName(){
        Date date = new Date();
        int second = getSecondTimestamp(date);
        String fileName = String.valueOf(second) + ".jpg";
        return fileName;
    }

    public static boolean isGif(FileItem item){
        String fileFullName = item.getName();
        File fileInfo = new File(fileFullName);
        String suffix = fileInfo.getName().substring(fileInfo.getName().lastIndexOf(".") + 1);
        if (suffix!= null) {
            return true ;
        }else {
            return false;
        }


    }


}
