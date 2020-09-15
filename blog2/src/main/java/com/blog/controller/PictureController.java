package com.blog.controller;

import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * 照片墙
 */
@Controller()
public class PictureController {
    @Autowired
    private PictureService pictureService;
    @GetMapping("/picture")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model){
//        List<Picture> pictures = pictureService.
//        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pictureService.BlogPicture());
        return "picture";
    }


    @PostMapping("/picture/insertOrUpdate")
    @ApiOperation(value = "添加一张照片记录")
    public String insertImage(@Valid Picture picture){

        return null;
    }

    @DeleteMapping("/picture/delete")
    @ApiOperation(value = "上传一张照片记录")
    public String deleteImage(@Valid Picture picture){

        return null;
    }

}

