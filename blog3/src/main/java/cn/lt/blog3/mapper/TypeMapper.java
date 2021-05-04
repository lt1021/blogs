package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:35
 */
public interface TypeMapper extends BaseMapper<Type> {

//    @Select("select t.id,t.name from t_type t ,t_blog b where t.id = b.type_id")
//    @ResultMap(value = "typeMap")
    @Select("select t.id tid, t.name, b.id bid, b.title, b.type_id " +
            "from t_type t, t_blog b " +
            "where t.id = b.type_id")
    @ResultMap(value = "type")
    @Results(id = "type",  value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "name", property = "name"),
            @Result(property = "blogs", javaType = List.class,
                    column = "id",
                    many = @Many(select = "cn.lt.blog3.mapper.mapper.BlogMapper.getBlogByType"))
    })
    List<Type> getBlogType();
}
