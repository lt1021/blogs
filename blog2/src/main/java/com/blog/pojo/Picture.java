package com.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 照片墙实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    private Integer id;
    private String picturename;//照片名字
    private Date picturetime;//照片时间
    private String picturedescription;//照片描述
    private String imagePath; //图片路径
    private MultipartFile path; //图片路径



}
