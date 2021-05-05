package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.api.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:35
 */
public interface TypeMapper extends BaseMapper<Type> {

//
//    @Select("select t.id tid, t.name, b.id bid, b.title, b.type_id " +
//            "from t_type t, t_blog b " +
//            "where t.id = b.type_id")
//    @Results(id = "typeMap",value = {
//            @Result(id = true, column = "tid",property = "id"),
//            @Result(column = "name", property = "name"),
//            @Result(column = "bid", property = "blogs", javaType = List.class,many = @Many(select = "getBlogByType"))
//    })
//    @ResultMap(value = "type")
    List<Type> getBlogType();

//    @Select("select * from t_blog b " +
//            "where b.type_id =#{id}")
//    List<Blog> getBlogByType(@Param("id") Integer id);

}
