package com.blog.controller;

import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 照片墙
 */
@Controller()
public class PictureShowController {
    @Autowired
    private PictureService<Picture> pictureService;

    /**
     * 文件目录路径
     */
    private static String dirPath;

    /**
     * 模板路径
     */
    private static String template;

    private static String domain;

    @Value("${file.dirPath}")
    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    @Value("${file.template.path}")
    public void setTemplate(String template) {
        this.template = template;
    }

    @GetMapping("/picture")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model ){
//        PageHelper.startPage(pagenum, 5);  //开启分页
//        List<Picture> pictures = pictureService.BlogPicture();
//        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
//        model.addAttribute("pageInfo",pageInfo);
//        model.addAttribute("pictures",pictures);

        List<Picture> picture = pictureService.BlogPicture();
        model.addAttribute("pictures",picture);
        return "picture";
    }



}

