package com.blog.base.util;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.base.bean.BaseInfo;
import com.blog.base.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 当配置变动后改变各个档案的搜索字段
 */
@Slf4j
@Component
public class SearchApplication {

    /**
     * 线程数
     */
    private static final Integer THREAD_SIZE = 10;


    /**
     * @param ids   修改了的对象
     * @param clazz 受影响的档案service
     */
    public static void resetSearch(List<Long> ids, Class<?> clazz) {
        log.info("开始重置搜索字段" + clazz.getName());
        SearchApplication application = ApplicationContextUtil.get(SearchApplication.class);
        application.reset(ids, clazz);
    }

    /**
     * @param t        修改了的对象
     * @param function 修改对象在对方表中的字段
     * @param clazz    受影响的档案service
     */
    public static <E extends BaseInfo, T extends BaseInfo, R> void resetSearch(E t, SFunction<T, R> function, Class<?> clazz) {
        log.info("开始重置搜索字段" + clazz.getName());
        SearchApplication application = ApplicationContextUtil.get(SearchApplication.class);
        application.reset(t, function, clazz);
    }

    @Async
    public <E extends BaseInfo, T extends BaseInfo, R> void reset(E t, SFunction<T, R> function, Class<?> clazz) {
        //如果是配置相关
        Long id = t.getId();
        //获取影响对象的service
        Object service = ApplicationContextUtil.get(clazz);
        if (service instanceof SearchService) {
            IService iService = (IService) service;
            SearchService searchService = (SearchService) service;
            //构建查询条件
            //查出id
            List<BaseInfo> list = iService.list(Wrappers.<T>lambdaQuery()
                    .setEntityClass((Class<T>) iService.getEntityClass())
                    .eq(function, id)
                    .select(BaseInfo::getId)
            );
            if (!list.isEmpty()) {
                List<Long> ids = list.stream().map(BaseInfo::getId).collect(Collectors.toList());
                //重构查询信息
                //将id集合平均分为10个线程执行
                int size = ids.size() / THREAD_SIZE;
                size = size <= 0 ? 1 : size;
                for (int i = 0; i < THREAD_SIZE - 1; i++) {
                    List<Long> sid = new ArrayList<>(size);
                    for (int j = 0; j < size && !ids.isEmpty(); j++) {
                        sid.add(ids.remove(0));
                    }
                    searchService.buildSearch(sid);
                }
                //剩下的放在最后一个线程
                searchService.buildSearch(ids);
            }
        }
    }


    @Async
    public void reset(List<Long> ids, Class<?> clazz) {
        //获取影响对象的service
        Object service = ApplicationContextUtil.get(clazz);
        if (service instanceof SearchService) {
            SearchService searchService = (SearchService) service;
            //构建查询条件
            searchService.buildSearch(ids);
        }
    }

}
