package com.blog.util;

import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.PutObjectRequest;
//import com.aliyun.oss.model.PutObjectResult;
//import org.apache.log4j.Logger;
//import org.springframework.util.Base64Utils;
//import sun.misc.BASE64Decoder;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.UUID;


/**
 * @author lt
 * @date 2020/12/23 11:42
 */
@ApiModel("图片上传")
public class UploadPictureUtils {

//    static Logger logger = Logger.getLogger(UploadPictureUtils.class);
//    @ApiModelProperty("阿里云地址")
//    private static String endpoint = "";
//    @ApiModelProperty("阿里云信息")
//    private static String accessKeyId = "";
//    @ApiModelProperty("")
//    private static String accessKeySecret = "";
//    @ApiModelProperty("存到哪一个bucket（阿里云上的对象存储）")
//    private static String bucketName = "";
//
//    /**
//     * 图片上传（文件名重复不覆盖），保单号，图片类型，图片base64字符串
//     *
//     * @param fileName
//     * @param type
//     * @param pictureStr
//     * @return
//     * @throws IOException
//     */
//    public static String uploadPicture(String fileName, String type, String pictureStr, int count) throws IOException {
//        String backUrl = "";
//        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            // type和fileName可以自己定义
//            logger.info("开始上传图片，文件名称：" + fileName + "，类型：" + type);
//            fileName = fileName + "LSJ" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
//            String key = type + "-" + fileName + count;
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, createTempFile(fileName, type, pictureStr));
//            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
//            backUrl = "https://" + bucketName + "." + Constant.URLEndPoint + "/" + key;
//
//        } catch (OSSException oe) {
//            logger.info("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            logger.info("Error Message: " + oe.getErrorCode());
//            logger.info("Error Code:       " + oe.getErrorCode());
//            logger.info("Request ID:      " + oe.getRequestId());
//            logger.info("Host ID:           " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.info("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            logger.info("Error Message: " + ce.getMessage());
//        } finally {
//            client.shutdown();
//        }
//        return backUrl;
//    }
//
//    /**
//     * 图片上传（文件名重复覆盖），保单号，图片类型，图片base64字符串
//     *
//     * @param fileName
//     * @param type
//     * @param pictureStr
//     * @return
//     * @throws IOException
//     */
//    public static String uploadPictureRepeat(String fileName, String type, String pictureStr) throws IOException {
//        String backUrl = "";
//        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            logger.info("开始上传图片，文件名称：" + fileName + "，类型：" + type);
//            String key = type + "-" + fileName;
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, createTempFile(fileName, type, pictureStr));
//            PutObjectResult putObjectResult = client.putObject(putObjectRequest);
//            backUrl = "https://" + bucketName + "." + Constant.URLEndPoint + "/" + key;
//
//        } catch (OSSException oe) {
//            logger.info("Caught an OSSException, which means your request made it to OSS, but was rejected with an error response for some reason.");
//            logger.info("Error Message: " + oe.getErrorCode());
//            logger.info("Error Code:       " + oe.getErrorCode());
//            logger.info("Request ID:      " + oe.getRequestId());
//            logger.info("Host ID:           " + oe.getHostId());
//        } catch (ClientException ce) {
//            logger.info("Caught an ClientException, which means the client encountered a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            logger.info("Error Message: " + ce.getMessage());
//        } finally {
//            client.shutdown();
//        }
//        return backUrl;
//    }
//
//
//    private static File createTempFile(String fileName, String type, String pictureStr) throws IOException {
//        File file = File.createTempFile(type + "-" + fileName, ".jpg");
//        file.deleteOnExit();
//        OutputStream out = new FileOutputStream(file);
//        if (pictureStr == null) //图像数据为空
//        {
//            logger.info("图像数据为空");
//            return null;
//        }
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            //Base64解码
//            byte[] b = decoder.decodeBuffer(pictureStr);
//            for (int i = 0; i < b.length; ++i) {
//                if (b[i] < 0) {//调整异常数据
//                    b[i] += 256;
//                }
//            }
//            out.write(b);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            return null;
//        }
//        return file;
//    }
//
//    // 上传本地图片
//    public static void main(String[] args) {
//        String fileName = "white.png";
//        String type = "test";
//
//        File file = new File("C:\\test\\wizardmmp.jpg");
//        String imageBase64 = Base64Utils.ImageToBase64ByLocal(file);
//        String result = null;
//        try {
//            result = uploadPictureRepeat(fileName, type, imageBase64);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(result);
//    }



}
