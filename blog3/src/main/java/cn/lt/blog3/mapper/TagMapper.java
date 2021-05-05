package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:34
 */
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> getBlogTag();
}
