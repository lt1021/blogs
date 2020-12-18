package com.blog.dao;

import com.blog.pojo.Picture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface PictureDao {

    List<Picture> BlogPicture();

    int insertPicture(Picture picture);

    Picture query();

    Picture get(Integer id);

    long update(Picture picture);

    long detele(Integer id);
}
