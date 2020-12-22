package com.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 照片墙实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    private Integer id;
    private String picturename;//照片名字
    private String picturetime;//照片时间
    private String pictureaddress;//照片地址
    private String picturedescription;//照片描述
//    @NotBlank(message = "请上传图片")
    private String imagePath; //图片路径

}
