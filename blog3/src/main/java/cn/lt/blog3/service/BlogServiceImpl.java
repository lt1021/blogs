package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.service.BlogService;
import cn.lt.blog3.mapper.BlogMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

}
