package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:35
 */
public interface TypeMapper extends BaseMapper<Type>  {
//    @Select("select t.id,t.name from t_type t ,t_blog b where t.id = b.type_id")
    List<Type> getBlogType();
}
