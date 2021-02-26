package com.blog.base.util;

import com.blog.base.cache.CacheManager;
import org.springframework.beans.BeansException;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author tiger (tiger@microsoul.com) 16/9/18
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext;
    private static Map<String, Class> beans;


    public void setBeans(Map<String, Class> beans) {
        ApplicationContextUtil.beans = beans;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (Objects.isNull(event.getApplicationContext().getParent())) {
            CacheManager cacheManager = (CacheManager) applicationContext.getBean("cacheManager");
            if (applicationContext.containsBean("ehCacheCacheManager")) {
                cacheManager.setCacheManager((EhCacheCacheManager) applicationContext.getBean("ehCacheCacheManager"));
            }
            if (applicationContext.containsBean("redisTemplate")) {
//                cacheManager.setRedisTemplate((RedisTemplate) applicationContext.getBean("redisTemplate"));
            }
        }
    }


    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T get(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T get(Class<T> tClass) {
        return applicationContext.getBean(tClass);
    }
}
