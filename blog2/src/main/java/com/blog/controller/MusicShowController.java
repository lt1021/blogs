package com.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MusicShowController {
    @GetMapping("/music")
    public String music(){
        return "music";
    }
}
