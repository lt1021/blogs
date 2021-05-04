package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:34
 */
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select t.id,t.name from t_tag t , t_blog b where b.type_id = t.id")
    List<Tag> getBlogTag();
}
