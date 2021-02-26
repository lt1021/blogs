package com.blog.base.em;

/**
 * @author lt
 * @date 2021/2/26 15:07
 */
public enum  WhereType {
    /**
     * =
     */
    EQ,
    /**
     * !=
     */
    NE,
    /**
     * >
     */
    GT,
    /**
     * >=
     */
    GE,
    /**
     * <
     */
    LT,
    /**
     * <=
     */
    LE,
    /**
     * in()
     */
    IN,
    /**
     * not in ()
     */
    NOT_IN,
    /**
     * find_in_set()
     */
    FIND_IN_SET,
    /**
     * !find_in_set()
     */
    NOT_FIND_IN_SET,
    /**
     * is null
     */
    IS_NULL,
    /**
     * is not null
     */
    IS_NOT_NULL,
    /**
     * like
     */
    LIKE,
    /**
     * group by
     */
    GROUP_BY;

}
