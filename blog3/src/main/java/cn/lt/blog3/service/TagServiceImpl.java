package cn.lt.blog3.service;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.entity.Tag;
import cn.lt.blog3.api.service.TagService;
import cn.lt.blog3.mapper.TagMapper;
import cn.lt.blog3.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:32
 */
@Service
public class TagServiceImpl extends BaseServiceImpl<TagMapper,Tag> implements TagService {
    @Override
    public List<Tag> getBlogTag() {
        return baseMapper.getBlogTag();
    }
}
