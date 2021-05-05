package cn.lt.blog3.api.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.query.BlogQuery;
import cn.lt.blog3.base.em.ResponseStatus;
import cn.lt.blog3.base.result.ResponseData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 16:25
 */
public interface BlogService extends IService<Blog> {
    //
    ResponseData queryPage(BlogQuery query);

    ResponseStatus search(String query);

    /**
     * 获取推荐博客
     * @return
     */
    Object recommendBlogs();
}
