package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.service.BlogService;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.api.service.TypeService;
import cn.lt.blog3.mapper.BlogMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lt
 * @date 2021/4/30 16:35
 */
@Service
public class BlogServiceImpl extends BaseServiceImpl<BlogMapper,Blog> implements BlogService {

    static {
        System.out.println("测试Service");
    }
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @Override
    public Object queryPage() {
//        Object data = new Object();
        List<Object> data = new ArrayList<>();
        data.add(list());
        data.add(typeService.list());
        data.add(tagService.list());
//        List<Blog> recommendBlog =blogService.getAllRecommendBlog();  //获取推荐博客
        return data;
    }
}
