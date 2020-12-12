package com.blog.controller;

import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 照片墙
 */
@Controller()
public class PictureShowController {
    @Autowired
    private PictureService<Picture> pictureService;
    @GetMapping("/picture")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model ){
//        PageHelper.startPage(pagenum, 5);  //开启分页
//        List<Picture> pictures = pictureService.BlogPicture();
//        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
//        model.addAttribute("pageInfo",pageInfo);
//        model.addAttribute("pictures",pictures);
        model.addAttribute("pictures",pictureService.BlogPicture());
        return "picture";
    }



}

