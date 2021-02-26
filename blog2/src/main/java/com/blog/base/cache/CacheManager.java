package com.blog.base.cache;

import com.blog.base.bean.BaseInfo;
import com.blog.base.util.BeanHelp;
import com.blog.base.util.CollectionUtils;
import com.blog.base.util.ProtostuffSerializer;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tiger (tiger@microsoul.com) 2017/8/14
 */
@Slf4j
@Component
public class CacheManager {

    private EhCacheCacheManager cacheManager;

    private RedisTemplate<String, Object> redisTemplate;

    private ProtostuffSerializer serializer = new ProtostuffSerializer();

    /**
     * 添加缓存
     *
     * @param type 缓存类型
     * @param sid  key
     * @param bean value
     * @param <T>  内容对象泛型
     */
    public <T extends BaseInfo> void put(String type, long sid, T bean) {
        log.info(String.format("添加缓存%s:%s:%s", type, String.valueOf(sid), bean.toString()));
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.execute((RedisCallback) redisConnection -> {
                redisConnection.hSet(CacheUtil.getKey(type), CacheUtil.getKey(String.valueOf(sid)), serializer.serialize(bean));
                return null;
            });
//            TransactionSynchronizationManager.unbindResource(redisTemplate.getConnectionFactory());
            return;
        }
        cacheManager.getCacheManager().getCache(type).put(BeanHelp.clone(new Element(sid, bean)));
    }

    /**
     * 添加缓存
     *
     * @param type 类型
     * @param key  key
     * @param bean value
     */
    public <T extends BaseInfo> void put(String type, String key, T bean) {
        log.info(String.format("添加缓存%s:%s:%s", type, key, bean.toString()));
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.execute((RedisCallback) redisConnection -> {
                redisConnection.hSet(CacheUtil.getKey(type), CacheUtil.getKey(key), serializer.serialize(bean));
                return null;
            });
//            TransactionSynchronizationManager.unbindResource(redisTemplate.getConnectionFactory());
            return;
        }
        cacheManager.getCacheManager().getCache(type).put(BeanHelp.clone(new Element(key, bean)));
    }

    /**
     * 获取缓存，返回泛型。
     *
     * @param type 类型
     * @param sid  key
     * @param <T>  内容类型
     * @return
     */
    public <T extends BaseInfo> T get(String type, Serializable sid) {
        log.info(String.format("获取缓存%s:%s", type, String.valueOf(sid)));
        if (Objects.nonNull(redisTemplate)) {
            return (T) redisTemplate.execute((RedisCallback) redisConnection -> {
                byte[] r = redisConnection.hGet(CacheUtil.getKey(type), CacheUtil.getKey(String.valueOf(sid)));
                return Objects.nonNull(r) ? serializer.deserialize(r) : null;
            });
        }
        Element element = cacheManager.getCacheManager().getCache(type).get(sid);
        return Objects.nonNull(element) ? BeanHelp.clone((T) element.getObjectValue()) : null;
    }

    /**
     * 获取缓存
     *
     * @param type 类型
     * @param key  key
     * @param <T>  valueType
     * @return
     */
    public <T extends BaseInfo> T get(String type, String key) {
        log.info(String.format("获取缓存%s:%s", type, key));
        if (Objects.nonNull(redisTemplate)) {
            return (T) redisTemplate.execute((RedisCallback) redisConnection -> {
                byte[] r = redisConnection.hGet(CacheUtil.getKey(type), CacheUtil.getKey(key));
                return Objects.nonNull(r) ? serializer.deserialize(r) : null;
            });
        }
        Element element = cacheManager.getCacheManager().getCache(type).get(key);
        return Objects.nonNull(element) ? BeanHelp.clone((T) element.getObjectValue()) : null;

    }

    /**
     * 移除缓存
     *
     * @param type 类型
     * @param sid  key
     */
    public void remove(String type, Serializable sid) {
        log.info(String.format("移除缓存%s:%s", type, String.valueOf(sid)));
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.execute((RedisCallback) redisConnection -> {
                redisConnection.hDel(CacheUtil.getKey(type), CacheUtil.getKey(String.valueOf(sid)));
                return null;
            });
            return;
        }
        cacheManager.getCacheManager().getCache(type).remove(sid);
    }

    public void remove(String type, String sid) {
        log.info(String.format("移除缓存%s:%s", type, String.valueOf(sid)));
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.execute((RedisCallback) redisConnection -> {
                redisConnection.hDel(CacheUtil.getKey(type), CacheUtil.getKey(sid));
                return null;
            });
            return;
        }
        cacheManager.getCacheManager().getCache(type).remove(sid);
    }

    /**
     * 清空某个缓存
     *
     * @param type
     */
    public void clear(String type) {
        log.info(String.format("清空缓存%s", type));
        if (Objects.nonNull(redisTemplate)) {
            redisTemplate.execute((RedisCallback) redisConnection -> {
                return redisConnection.hDel(CacheUtil.getKeys(type));
            });
        }
        Cache cache = cacheManager.getCacheManager().getCache(type);
        for (Object key : cache.getKeys()) {
            cache.remove(key);
        }
    }

    /**
     * 获取某个类型的所有缓存
     *
     * @param type 类型
     * @param <T>  valueType
     * @return
     */
    public <T extends BaseInfo> List<T> all(String type) {
        log.info(String.format("获取All缓存%s", type));
        List<T> rs = new ArrayList<>();
        if (Objects.nonNull(redisTemplate)) {
            return (List<T>) redisTemplate.execute((RedisCallback) redisConnection -> {
                Map<byte[], byte[]> map = redisConnection.hGetAll(CacheUtil.getKey(type));
                if (CollectionUtils.notEmpty(map)) {
                    map.forEach((k, v) -> {
                        rs.add((T) serializer.deserialize(v));
                    });
                }
                return rs;
            });
        }
        Cache cache = cacheManager.getCacheManager().getCache(type);
        for (Object key : cache.getKeys()) {
            rs.add((T) cache.get(key).getObjectValue());
        }
        return BeanHelp.deepCopy(rs);
    }


    /**
     * 获取某个类型的缓存数量
     *
     * @param type 类型
     * @return
     */
    public int getCacheCount(String type) {
        log.info(String.format("获取缓存数量%s", type));
        if (Objects.nonNull(redisTemplate)) {
            return (int) redisTemplate.execute((RedisCallback) redisConnection -> {
                return redisConnection.hGetAll(CacheUtil.getKey(type)).size();
            });
        }
        Cache cache = cacheManager.getCacheManager().getCache(type);
        return cache.getSize();
    }

    public void setCacheManager(EhCacheCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
