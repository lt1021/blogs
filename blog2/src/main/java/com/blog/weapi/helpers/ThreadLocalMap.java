package com.blog.weapi.helpers;

import java.util.Hashtable;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-7-12 22:56:27
 */
public class ThreadLocalMap extends InheritableThreadLocal {

    public
    final Object childValue(Object parentValue) {
        Hashtable ht = (Hashtable) parentValue;
        if (ht != null) {
            return ht.clone();
        } else {
            return null;
        }
    }

}
