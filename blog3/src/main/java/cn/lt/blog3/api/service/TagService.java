package cn.lt.blog3.api.service;

import cn.lt.blog3.api.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 17:23
 */
public interface TagService extends IService<Tag> {
    List<Tag> getBlogTag();
}
