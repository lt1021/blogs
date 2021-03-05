package com.blog.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SCPOutputStream;
import com.blog.pojo.Picture;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.UUID;

/**
 * @author lt
 * @date 2021/1/23 11:35
 */
@Slf4j
@Configuration
public class FileUpDownload {

    @Value("${remoteServer.url}")
    private static String url = "/home/image";

    @Value("${remoteServer.password}")
    private static String passWord = "2000101LiuTao";

    @Value("${remoteServer.username}")
    private static String userName = "root";

    private static String UPLOAD_PATH = "images/upload";

    /**
     * 文件目录路径
     */
    private static String dirPath;

    /**
     * 模板路径
     */
    private static String template;

    private static String domain;

    @Value("${file.dirPath}")
    public void setDirPath(String dirPath) {
        FileUpDownload.dirPath = dirPath;
    }

    @Value("${file.template.path}")
    public void setTemplate(String template) {
        FileUpDownload.template = template;
    }

    public static String getDirPath() {
        return dirPath;
    }



    public static   Picture imagePath(Picture picture)throws IOException {
        MultipartFile file = picture.getPath();
        byte[] imgBytes = file.getBytes();
        // 获取上传文件名
        String fileName = file.getOriginalFilename();
        String dir = "blog";
        String name = picture.getPicturename();
        if (StringUtil.isEmpty(name)) {
            name = FilenameUtils.getBaseName(fileName);
        }
        //创建随机文件夹
        String path = UUID.randomUUID().toString();
        File fe = new File(FileUpDownload.dirPath, "/thumb/" + dir + "/" + path);
        if (!fe.exists()) {
            fe.mkdirs();
        }
        String extension = FilenameUtils.getExtension(fileName);
        File img = new File(fe.getPath() + "/" + name + "." + extension);
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(img))) {
            //保存原图
            outputStream.write(imgBytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //生成缩略图
        thumbnail(img, img.getParent() + "/Thumbnail-" + name + "." + extension);
        //返回缩略图路径
        String picPath = "thumb/" + dir + "/" + path + "/Thumbnail-" + name + "." + extension;
        picture.setImagePath(picPath);
        return picture;
    }

    /**
     * 生成缩略图
     *
     * @param img  原图
     * @param path 路径
     */
    public static void thumbnail(File img, String path) {
        try (FileInputStream fileInputStream = new FileInputStream(img)) {
            BufferedImage read = ImageIO.read(fileInputStream);
            double d = 1;
            int width = read.getWidth();
            int height = read.getHeight();
            if (width >= 350 && height >= 350) {
                d = width > height ? 350D / width : 350D / height;
            }
            Thumbnails.of(img).scale(d).toFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Async
//    public ResultEntity uploadFile(File file, String targetPath, String remoteFileName) throws Exception{
//        ScpConnectEntity scpConnectEntity=new ScpConnectEntity();
//        scpConnectEntity.setTargetPath(targetPath);
//        scpConnectEntity.setUrl(url);
//        scpConnectEntity.setPassWord(passWord);
//        scpConnectEntity.setUserName(userName);
//
//        String code = null;
//        String message = null;
//        try {
//            if (file == null || !file.exists()) {
//                throw new IllegalArgumentException("请确保上传文件不为空且存在！");
//            }
//            if(remoteFileName==null || "".equals(remoteFileName.trim())){
//                throw new IllegalArgumentException("远程服务器新建文件名不能为空!");
//            }
//            remoteUploadFile(null, file, remoteFileName);
//            code = "ok";
//            message = remoteFileName;
//        } catch (IllegalArgumentException e) {
//            code = "Exception";
//            message = e.getMessage();
//        } catch (JSchException e) {
//            code = "Exception";
//            message = e.getMessage();
//        } catch (IOException e) {
//            code = "Exception";
//            message = e.getMessage();
//        } catch (Exception e) {
//            throw e;
//        } catch (Error e) {
//            code = "Error";
//            message = e.getMessage();
//        }
//        return new ResultEntity(code, message, null);
//    }


    private static void remoteUploadFile(Picture picture,File file,String fileName) throws JSchException, IOException {
        Connection connection = null;
        ch.ethz.ssh2.Session session = null;
        SCPOutputStream scpo = null;
        FileInputStream fis = null;

        try {
            createDir(picture);
        }catch (JSchException e) {
            throw e;
        }

        try {
            connection = new Connection(url);
            connection.connect();
            if(!connection.authenticateWithPassword(userName,passWord)){
                throw new RuntimeException("SSH连接服务器失败");
            }
            session = connection.openSession();

            SCPClient scpClient = connection.createSCPClient();

            scpo = scpClient.put(fileName,file.length(), url, "0666");
            fis = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int hasMore = fis.read(buf);

            while(hasMore != -1){
                scpo.write(buf);
                hasMore = fis.read(buf);
            }
        } catch (IOException e) {
            throw new IOException("SSH上传文件至服务器出错"+e.getMessage());
        }finally {
            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != scpo){
                try {
                    scpo.flush();
//                    scpo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != session){
                session.close();
            }
            if(null != connection){
                connection.close();
            }
        }
    }


    private static boolean createDir(Picture picture ) throws JSchException {

        JSch jsch = new JSch();
        com.jcraft.jsch.Session sshSession = null;
        Channel channel= null;
        try {
            sshSession = jsch.getSession(userName,url, 22);
            sshSession.setPassword(passWord);
            sshSession.setConfig("StrictHostKeyChecking", "no");
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            throw new JSchException("SFTP连接服务器失败"+e.getMessage());
        }
        ChannelSftp channelSftp=(ChannelSftp) channel;
        if (isDirExist(url,channelSftp)) {
            channel.disconnect();
            channelSftp.disconnect();
            sshSession.disconnect();
            return true;
        }else {
            String pathArry[] = url.split("/");
            StringBuffer filePath=new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                try {
                    if (isDirExist(filePath.toString(),channelSftp)) {
                        channelSftp.cd(filePath.toString());
                    } else {
                        // 建立目录
                        channelSftp.mkdir(filePath.toString());
                        // 进入并设置为当前目录
                        channelSftp.cd(filePath.toString());
                    }
                } catch (SftpException e) {
                    e.printStackTrace();
                    throw new JSchException("SFTP无法正常操作服务器"+e.getMessage());
                }
            }
        }
        channel.disconnect();
        channelSftp.disconnect();
        sshSession.disconnect();
        return true;
    }

    private static boolean isDirExist(String directory,ChannelSftp channelSftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channelSftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 获取路径下文件文本
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static String readFileToString(String fileName) throws FileNotFoundException {
        if (null == fileName || fileName.isEmpty())
            return null;
        InputStreamReader input = null;
        try {
            // 一次读多个字节
            byte[] bytes = new byte[1024];
            int read = 0;
            input = new InputStreamReader(new FileInputStream(fileName));
            StringBuilder builder = new StringBuilder();
            while ((read = input.read()) != -1) {
                builder.append((char) read);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                close(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void close(Object... obj) throws Exception {
        for (Object object : obj) {
            if (null != object) {
                ((AutoCloseable) object).close();
            }
        }
    }

    /**
     * 下载网络文件
     *
     * @param url
     * @return
     */
    public static File downloadFile(String url, String dir, String name) throws IOException {
        File destination = new File(dir, name);
        FileUtils.copyURLToFile(new URL(url), destination);
        return destination;
    }


    /**
     * 获取路径下文件的字节数组
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static byte[] readFileByBytes(String fileName) {
        if (null == fileName || fileName.isEmpty())
            return null;
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            // 一次读多个字节
            byte[] bytes = new byte[1024];
            int read = 0;
            input = new FileInputStream(fileName);
            output = new ByteArrayOutputStream((int) new File(fileName).length());
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                close(input, output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 删除指定文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            //是文件
            if (temp.isFile()) {
                temp.delete();
                flag = true;
            }
            //是文件夹
            if (temp.isDirectory()) {
                //先删除文件夹里面的文件
                delAllFile(path + "/" + tempList[i]);
                flag = true;
            }
        }
        return flag;
    }




}
