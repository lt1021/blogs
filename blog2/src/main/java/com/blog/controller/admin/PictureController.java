package com.blog.controller.admin;

import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import com.blog.util.FileUpDownload;
import com.blog.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 照片墙
 */
@Controller()
@RequestMapping("/admin")
public class PictureController {
    @Autowired
    private PictureService pictureService;


    @GetMapping("/pictures")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model,
            /* @PathVariable Long id,*/ @RequestParam(required = false, defaultValue = "1", value = "pagenum") int pagenum) {
        PageHelper.startPage(pagenum, 5);  //开启分页
        List<Picture> pictures = pictureService.BlogPicture();
        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("pictures",pictures);
        return "admin/pictures";
    }


    @GetMapping("/pictures/input")
    @ApiOperation(value = "跳转添加")
    public String picture(Model model) {
        model.addAttribute("picture", new Picture());//返回一个tag对象给前端th:object
        return "admin/pictures-input";
    }


    @PostMapping("/picture/insert")
    @ApiOperation(value = "添加一张照片记录")
    public String insertImage(@Valid Picture picture, RedirectAttributes attributes)throws IOException {
        if (picture.getPath()==null) {
            attributes.addFlashAttribute("msg", "图片不能为空");
            return "redirect :/admin/picture";
        }
        Picture pic = FileUpDownload.imagePath(picture);
        pic.setPicturetime(new Date());
        if (pictureService.insert(pic) == 0) {
            attributes.addFlashAttribute("msg", "新增失败");
        }
        attributes.addFlashAttribute("msg", "新增成功");
        return "redirect:/admin/pictures";
    }


    @ApiOperation(value = "跳转照片编辑页面")
    @GetMapping("/pictures/{id}/input")
    public String editInput(@PathVariable Integer id, Model model) {
        model.addAttribute("picture", pictureService.get(id));
        return "admin/pictures-input";
    }

    @PostMapping("/picture/insert/{id}")
    public String editPost(@Valid Picture picture, RedirectAttributes attributes)throws IOException  {
        Picture pic = picture;
        if (!StringUtil.isEmpty(picture.getPath().getOriginalFilename())) {
             pic = FileUpDownload.imagePath(picture);
        }
        pic.setPicturetime(new Date());
        Long P = pictureService.updatePictureById(pic);
        if (P == 0 ) {
            attributes.addFlashAttribute("message", "编辑失败");
        } else {
            attributes.addFlashAttribute("message", "编辑成功");
        }
        return "redirect:/admin/pictures";
    }

    @DeleteMapping("/picture/delete/{id}")
    @ApiOperation(value = "删除一张照片记录")
    public String deleteImage(@PathVariable Integer id, RedirectAttributes attributes) {
        pictureService.detelePictureById(id);
        attributes.addFlashAttribute("msg","图片删除成功");
        return "redirect :/admin/pictures";
    }
}
