package com.blog.base.service;

import java.util.List;

public interface SearchService {

    /**
     * 构建查询字段
     *
     * @param ids
     */
    void buildSearch(List<Long> ids);

}
