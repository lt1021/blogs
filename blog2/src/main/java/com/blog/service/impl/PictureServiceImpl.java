package com.blog.service.impl;

import com.blog.dao.PictureDao;
import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl  implements PictureService {
    @Autowired
    private PictureDao pictureDao;

    @Override
    public Object queryPictureById(Integer id) {
        return null;
    }

    @Override
    public long updatePictureById(Object o) {
        return 0;
    }

    @Override
    public long detelePictureById(Integer id) {
        return 0;
    }

    @Override
    public long insert(Object o) {
        return 0;
    }

    @Override
    public List<Picture> BlogPicture() {
        return pictureDao.BlogPicture();
    }
}
