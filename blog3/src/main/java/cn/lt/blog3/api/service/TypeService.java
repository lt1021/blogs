package cn.lt.blog3.api.service;

import cn.lt.blog3.api.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author lt
 * @date 2021/4/30 18:31
 */
public interface TypeService extends IService<Type> {

    List<Type> getBlogType();
}
