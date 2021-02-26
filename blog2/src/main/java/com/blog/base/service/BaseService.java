package com.blog.base.service;

public interface BaseService<T> {

    /**
     * 判断当前数据是否已存在,并返回已存在对象的id
     *
     * @param t
     * @return
     */
    default Long check(T t) {
        return null;
    }
}
