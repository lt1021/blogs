package cn.lt.blog3.service.base;

import cn.lt.blog3.api.entity.Blog;
import cn.lt.blog3.base.bean.BaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;

/**
 * @author lt
 * @date 2021/4/30 17:24
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseInfo> extends ServiceImpl<M,T> {

//    @Autowired
//    private CacheManager cacheManager;

    static {
        System.out.println("测试BaseService");
    }
    @Override
    public boolean save(T entity) {
        return false;
    }
}
