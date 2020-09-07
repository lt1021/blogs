package com.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
