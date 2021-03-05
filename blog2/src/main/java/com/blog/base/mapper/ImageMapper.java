package com.blog.base.mapper;

import com.blog.base.bean.IdImageNameBean;
import com.blog.base.bean.ImageBean;

/**
 * @author lt
 * @date 2021/3/5 11:25
 */
public interface ImageMapper {
    void updateImageByCode(ImageBean bean);

    void updateImageById(IdImageNameBean bean);
}
