package com.blog.controller;

import com.blog.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 照片墙
 */
@Controller()
public class PictureController {
    @Autowired
    private PictureService pictureService;
    @GetMapping("/picture")
    public String picture(Model model){

//        model.addAllAttributes("",null);
        return "picture";
    }

}

