package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:34
 */
public interface TagMapper extends BaseMapper<Tag> {
    @Select("select t.id,t.name from t_tag t , t_blog b where b.type_id = t.id")
//    @Results(id = "blogMap", value = {
//            @Result(column= "id" , property = "id",id = true),
//            @Result(column="name", property="name"),
//            @Result(property="roleList", javaType= List.class,column="id",
//                    many=@Many(select="cn.lt.blog3..mapper.BlogMapper.getBlogByType"))
//      })
    List<Tag> getBlogTag();
}
