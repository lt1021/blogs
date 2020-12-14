package com.blog.controller.admin;

import com.blog.pojo.Picture;
import com.blog.pojo.Tag;
import com.blog.service.PictureService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 照片墙
 */
@Controller()
@RequestMapping("/admin")
public class PictureController {
    @Autowired
    private PictureService pictureService;

    /**上传地址*/
    @Value("${file.upload.path}")
    private String filePath;

    @GetMapping("/pictures")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model,
                         /* @PathVariable Long id,*/ @RequestParam(required = false,defaultValue = "1",value = "pagenum")int pagenum){
        PageHelper.startPage(pagenum, 5);  //开启分页
        List<Picture> pictures = pictureService.BlogPicture();
        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("pictures",pictures);
        return "admin/pictures";
    }


    // 执行上传
    @RequestMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = filePath+"rotPhoto/";
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(new File(path + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将src路径发送至html页面
        model.addAttribute("filename", "/images/rotPhoto/"+filename);
        return "Page";
    }


    @GetMapping("/pictures/input")
    @ApiOperation(value = "跳转添加")
    public String picture(Model model){
        model.addAttribute("picture", new Picture());//返回一个tag对象给前端th:object
        return "admin/pictures-input";
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
