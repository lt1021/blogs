package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.entity.Tag;
import cn.lt.blog3.api.entity.Type;
import cn.lt.blog3.api.query.BlogQuery;
import cn.lt.blog3.api.service.BlogService;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.api.service.TypeService;
import cn.lt.blog3.base.em.ResponseStatus;
import cn.lt.blog3.base.result.PageResult;
import cn.lt.blog3.base.result.ResponseData;
import cn.lt.blog3.base.util.QueryWrapperHelp;
import cn.lt.blog3.mapper.BlogMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public ResponseData queryPage(BlogQuery query) {
        query.setPageSize(5);
        query.setSelect("id, title, content, first_picture, flag, views, appreciation, share_statement, commentabled, published, recommend, create_time, update_time, type_id, user_id, description, tag_ids");
        QueryWrapper<Blog> wrapper = QueryWrapperHelp.formatWrapper(query);
        IPage<Blog> page = page(query.page(), wrapper);
        return PageResult.data(page.getTotal(),page.getRecords());
    }

    @Override
    public ResponseStatus search(String query) {
        return null;
    }

    @Override
    public Object recommendBlogs() {
        return list(Wrappers.<Blog>lambdaQuery().orderByDesc(Blog::getUpdateTime));
    }
}
