package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.entity.Tag;
import cn.lt.blog3.api.entity.Type;
import cn.lt.blog3.api.service.BlogService;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.api.service.TypeService;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.mapper.BlogMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * @author lt
 * @date 2021/4/30 16:35
 */
@Service
public class BlogServiceImpl extends BaseServiceImpl<BlogMapper, Blog> implements BlogService {

    static {
        System.out.println("测试Service");
    }

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @Override
    public List queryPage() {
        Map<String, Object> data = new HashMap<>();
        List<Blog> blog =this.list();
        data.put("blog", blog);
        data.put("type", typeService.getBlogType());  //获取博客的类型(联表查询)
//        data.put("tag", tagService.getBlogTag());//获取博客的标签(联表查询)
        data.put("recommendBlogs", list(Wrappers.<Blog>lambdaQuery().orderByDesc(Blog::getUpdateTime))); //获取推荐博客
        List list = new ArrayList();
        list.add(data);
        return list;
    }
}
