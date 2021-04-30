package cn.lt.blog3.api.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.base.em.ResponseStatus;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author lt
 * @date 2021/4/30 16:25
 */
public interface BlogService extends IService<Blog> {
    Object queryPage();
}
