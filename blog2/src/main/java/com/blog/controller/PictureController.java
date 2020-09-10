package com.blog.controller;

import com.blog.pojo.Blog;
import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import com.github.pagehelper.PageInfo;
import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 照片墙
 */
@Controller()
public class PictureController {
    @Autowired
    private PictureService pictureService;
    @GetMapping("/picture")
    public String picture(Model model){
//        List<Picture> pictures = pictureService.
//        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pictureService.BlogPicture());
        return "picture";
    }

}

