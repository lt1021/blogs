package cn.lt.blog3.mapper;

import cn.lt.blog3.api.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 16:55
 */
public interface BlogMapper extends BaseMapper<Blog> {

}
