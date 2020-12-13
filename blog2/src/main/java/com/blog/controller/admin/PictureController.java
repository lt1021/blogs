package com.blog.controller.admin;

import com.blog.pojo.Picture;
import com.blog.service.PictureService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * 照片墙
 */
@RestController()
@RequestMapping("/admin")
public class PictureController {
    @Autowired
    private PictureService pictureService;
    @GetMapping("/picture")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model,
                          @PathVariable Long id, @RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum){
        PageHelper.startPage(pagenum, 5);  //开启分页
        List<Picture> pictures = pictureService.BlogPicture();
        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("pictures",pictures);
        return "picture";
    }


    @PostMapping("/picture/insertOrUpdate")
    @ApiOperation(value = "添加一张照片记录")
    public String insertImage(@Valid Picture picture, RedirectAttributes attributes){
        if (pictureService.get(picture.getId())!= null ){
            pictureService.updatePictureById(picture);
            attributes.addFlashAttribute("msg", "修改成功");
        }else {
            if ( pictureService.insert(picture) == 0  ) {
                attributes.addFlashAttribute("msg", "新增失败");
            }
            attributes.addFlashAttribute("msg", "新增成功");
        }
        return "redirect :/admin/picture";
    }

    @DeleteMapping("/picture/delete/{id}")
    @ApiOperation(value = "删除一张照片记录")
    public String deleteImage(@PathVariable Integer id,RedirectAttributes attributes){
        pictureService.detelePictureById(id);
        attributes.addFlashAttribute("msg","图片删除成功");
        return "redirect :/admin/picture";
    }
}
