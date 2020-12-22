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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

//    /**上传地址*/
//    @Value("${file.upload.path}")
//    private String filePath;

    @GetMapping("/pictures")
    @ApiOperation(value = "查询所有照片")
    public String picture(Model model,
                         /* @PathVariable Long id,*/   @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageHelper.startPage(pageNum, 5);  //开启分页
        List<Picture> pictures = pictureService.BlogPicture();
        PageInfo<Picture> pageInfo = new PageInfo<>(pictures);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("pictures",pictures);
        return "admin/pictures";
    }



    @GetMapping("/pictures/input")
    @ApiOperation(value = "跳转添加")
    public String picture(Model model){
        model.addAttribute("picture", new Picture());//返回一个tag对象给前端th:object
        return "admin/pictures-input";
    }

    @PostMapping("/addImage")
    @ResponseBody
    public Object addImage(MultipartFile file)throws IOException {
//        System.out.println("---" + fileUploadPath);//我这里用的springboot 在application.properties中配置，使用@Value 获取的文件上传目录
//
//        MultipartFile file = multiReq.getFile("file");
//        String originalFilename = file.getOriginalFilename();
//        String suffix = originalFilename.substring(originalFilename.indexOf("."));
//        String localFileName = MD5Util.md5(file.getInputStream()) + suffix;
//        File localFile = new File(fileUploadPath + localFileName);
//        if (!localFile.exists()) {
//            localFile.createNewFile();
//
//            FileOutputStream fos = new FileOutputStream(
//                    localFile);
//            FileInputStream fs = (FileInputStream) multiReq.getFile("img").getInputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = fs.read(buffer)) != -1) {
//                fos.write(buffer, 0, len);
//            }
//            fos.close();
//            fs.close();
//
//        } else {
//            log.info("文件已存在！！");
//        }
        //        isMultiple =(MultipartFile) picture.getImagePath();
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = "static/images/picture/";

        File filepath = new File(path, filename);
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(new File(path + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @PostMapping("/picture/insert")
    @ApiOperation(value = "添加一张照片记录")
    public String insertImage(@Valid Picture picture,  RedirectAttributes attributes)throws IOException{
        if ( pictureService.insert(picture) == 0  ) {
            attributes.addFlashAttribute("msg", "新增失败");
        }
        attributes.addFlashAttribute("msg", "新增成功");

        return "redirect:/admin/pictures";
    }


    @GetMapping("/pictures/{id}/delete")
    @ApiOperation(value = "删除一张照片记录")
    public String deleteImage(@PathVariable Integer id,RedirectAttributes attributes){
        pictureService.detelePictureById(id);
        attributes.addFlashAttribute("msg","图片删除成功");
        return "redirect:/admin/pictures";
    }

    @GetMapping("/pictures/{id}/input")
    @ApiOperation(value = "跳转编辑相册")
    public String editInput(@PathVariable Integer id, Model model) {
        model.addAttribute("picture", pictureService.get(id));

        return "admin/pictures-input";
    }


    @PostMapping("/pictures/{id}")
    @ApiOperation(value = "编辑相册")
    public String editPost(@Valid Picture picture, RedirectAttributes attributes) {

        int P = pictureService.updatePictureById(picture);
        if (P == 0 ) {
            attributes.addFlashAttribute("message", "编辑失败");
        } else {
            attributes.addFlashAttribute("message", "编辑成功");
        }
        return "redirect:/admin/pictures";
    }



}


