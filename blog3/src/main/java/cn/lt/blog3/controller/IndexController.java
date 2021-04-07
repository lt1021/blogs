package cn.lt.blog3.controller;


import cn.lt.blog3.base.result.ResponseData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lt
 * @date 2021/4/7 17:56
 */
@RestController
@RequestMapping("")
public class IndexController {

    public ResponseData index() {


        return ResponseData.data();
    }
}
