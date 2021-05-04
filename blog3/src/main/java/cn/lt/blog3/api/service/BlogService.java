package cn.lt.blog3.api.service;

import cn.lt.blog3.api.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 16:25
 */
public interface BlogService extends IService<Blog> {
    List queryPage();
}
