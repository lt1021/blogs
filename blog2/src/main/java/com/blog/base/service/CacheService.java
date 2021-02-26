package com.blog.base.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.base.bean.BaseInfo;
import com.blog.base.cache.CacheManager;
import com.blog.base.util.CollectionUtils;
import com.blog.base.util.SearchApplication;
import com.blog.base.util.SysAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CacheService<M extends BaseMapper<T>, T extends BaseInfo> extends ServiceImpl<M, T> {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public T getById(Serializable id) {
        if (Objects.isNull(id)) {
            return null;
        }
        T t = cacheManager.get(getCacheName(), id);
        if (Objects.isNull(t)) {
            t = super.getById(id);
            if (Objects.nonNull(t)) {
                cacheManager.put(getCacheName(), t.getId(), t);
            }
        }
        return t;
    }

    @Override
    public boolean save(T entity) {
        exists(entity);
        boolean save = super.save(entity);
        SysAssert.isTrue("save_error", save);
        cacheManager.remove(getCacheName(), entity.getId());
        //构建查询条件
        SearchApplication.resetSearch(Arrays.asList(entity.getId()), getClass());
        return save;
    }

    /**
     * 在批量保存前一定要先将集合去重
     *
     * @param entityList
     * @return
     */
    @Override
    public boolean saveBatch(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        boolean b = super.saveBatch(entityList);
        for (T t : entityList) {
            exists(t);
            cacheManager.remove(getCacheName(), t.getId());
        }
        //构建查询条件
        SearchApplication.resetSearch(entityList.stream().map(T::getId).collect(Collectors.toList()), getClass());
        return b;
    }

    @Override
    public boolean removeById(Serializable id) {
        if (Objects.isNull(id)) {
            return false;
        }
        cacheManager.remove(getCacheName(), id);
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            cacheManager.remove(getCacheName(), id);
        }
        return super.removeByIds(idList);
    }

    @Override
    public boolean updateById(T entity) {
        exists(entity);
        cacheManager.remove(getCacheName(), entity.getId());
        boolean update = super.updateById(entity);
        if (update) {
            //构建查询条件
            SearchApplication.resetSearch(Arrays.asList(entity.getId()), getClass());
        }
        return update;
    }

    /**
     * 在批量保存前一定要先将集合去重
     *
     * @param entityList
     * @return
     */
    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        for (T t : entityList) {
            exists(t);
            cacheManager.remove(getCacheName(), t.getId());
        }
        //构建查询条件
        SearchApplication.resetSearch(entityList.stream().map(T::getId).collect(Collectors.toList()), getClass());
        return super.updateBatchById(entityList);
    }

    @Override
    public List<T> listByIds(Collection<? extends Serializable> idList) {
        List<T> list = super.listByIds(idList);
        if (CollectionUtils.notEmpty(list)) {
            for (T t : list) {
                cacheManager.put(getCacheName(), t.getId(), t);
            }
        }
        return list;
    }

    @Override
    public List<T> list() {
        List<T> list = cacheManager.all(getCacheName());
        if (CollectionUtils.isEmpty(list)) {
            list = super.list();
            if (CollectionUtils.notEmpty(list)) {
                for (T t : list) {
                    cacheManager.put(getCacheName(), t.getId(), t);
                }
            }
        }
        return CollectionUtils.isEmpty(list) ? Collections.EMPTY_LIST : list;
    }

    private void exists(T t) {
        Long id = check(t);
        if (Objects.nonNull(id)) {
            SysAssert.msg(getById(id), "exists");
        }
    }


    public Long check(T t) {
        return null;
    }

    protected CacheManager cacheManager() {
        return this.cacheManager;
    }

    protected abstract String getCacheName();
}
