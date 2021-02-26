package com.blog.base.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 集合通用工具
 */
public class CollectionUtils {

    /**
     * @param collection
     * @return true：集合为空，false：集合不为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (Objects.isNull(collection) || collection.isEmpty());
    }

    /**
     * @param map
     * @return true：map为空，false：map不为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (Objects.isNull(map) || map.isEmpty());
    }

    /**
     * @param collection
     * @return true：集合不为空
     */
    public static boolean notEmpty(Collection<?> collection) {
        return (Objects.nonNull(collection) && !collection.isEmpty());
    }

    /**
     * @param map
     * @return true：map不为空
     */
    public static boolean notEmpty(Map<?, ?> map) {
        return (Objects.nonNull(map) && !map.isEmpty());
    }

}
