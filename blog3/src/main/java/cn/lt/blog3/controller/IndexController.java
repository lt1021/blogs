package cn.lt.blog3.controller;


import cn.lt.blog3.api.query.BlogQuery;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.api.service.TypeService;
import cn.lt.blog3.base.bean.BaseInfo;
import cn.lt.blog3.base.bean.QueryInfo;
import cn.lt.blog3.base.result.PageResult;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.controller.base.BaseController;
import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.service.BlogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 * @author lt
 * @date 2021/4/7 17:56
 */
@RestController
public class IndexController extends BaseController<Blog,QueryInfo> {
    @Autowired
    private BlogService service;

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    static {
        System.out.println("测试");
    }
    @GetMapping("/")
    public ResponseData index(BlogQuery query) {
        Map<String, Object> data = new HashMap<>();
        List<Blog> blogs = (List<Blog>) service.queryPage(query).getData();
        data.put("blog", blogs);
        data.put("type", typeService.getBlogType());  //获取博客的类型(联表查询)
        data.put("tag", tagService.getBlogTag());//获取博客的标签(联表查询)
        data.put("recommendBlogs", service.recommendBlogs()); //获取推荐博客
        return ResponseData.data(data);
    }

    @ApiOperation(value = "搜索")
    @PostMapping("/search")
    public ResponseData search(BlogQuery query){
        return ResponseData.data(service.queryPage(query));
    }

    @Override
    protected IService getService() {
        return service;
    }
}
