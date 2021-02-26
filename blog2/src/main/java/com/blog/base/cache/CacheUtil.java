package com.blog.base.cache;


import com.blog.base.exception.ErpBaseException;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 缓存工具
 *
 * @author xtiger (xtiger@microsoul.com) 16-4-10
 */
public class CacheUtil {

    private static final String COMMON_CACHE_KEY = "ERP:";

    /**
     * 为key增加“*”返回utf-8 byte[]
     *
     * @param id
     * @return
     */
    public static byte[] getKeys(String id) {
        try {
            return (id + "*").getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取key的utf-8的byte[]
     *
     * @param id
     * @return
     */
    public static byte[] getKey(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ErpBaseException("Cache Key Is Null.");
        }
        try {
            return id.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
