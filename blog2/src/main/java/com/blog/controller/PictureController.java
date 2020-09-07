package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 照片墙
 */
@Controller()
public class PictureController {
    @GetMapping("/picture")
    public String picture(){
        return "picture";
    }

}

