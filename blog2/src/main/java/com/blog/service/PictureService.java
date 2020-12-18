package com.blog.service;

import com.blog.pojo.Picture;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("照片实体")
public interface PictureService<T> {
    /**
     * 根据id查询
     * @return
     */
    @ApiModelProperty("根据id查询")
    Picture get(Integer id);

    Picture query(Integer id);

    @ApiModelProperty("根据id修改")
    long updatePictureById(T t);

    @ApiModelProperty("根据id删除")
    long detelePictureById(Integer id);

    @ApiModelProperty("根据id添加")
    long insert(T  t);

    @ApiModelProperty("查询全部")
    List<T> BlogPicture();


}
