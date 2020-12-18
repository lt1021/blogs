package com.blog.service.impl;

import com.blog.dao.PictureDao;
import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl   implements PictureService<Picture> {
    @Autowired
    private PictureDao pictureDao;

    @Override
    public Picture query(Integer id) {
        return  pictureDao.query();
    }


    @Override
    public Picture get(Integer id) {
        return pictureDao.get(id);
    }

    @Override
    public long updatePictureById(Picture picture) {
        return pictureDao.update(picture);
    }

    @Override
    public long detelePictureById(Integer id) {

        return pictureDao.detele(id);
    }



    @Override
    public long insert(Picture picture) {
        return pictureDao.insertPicture(picture);
    }

    @Override
    public List<Picture> BlogPicture() {
        return pictureDao.BlogPicture();
    }
}
