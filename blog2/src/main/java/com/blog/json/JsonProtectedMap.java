package com.blog.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-3-15 21:01:31
 */
public class JsonProtectedMap<K, V> extends HashMap<K, V> implements ProtectedBean {
    public JsonProtectedMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public JsonProtectedMap(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonProtectedMap() {
    }


    public JsonProtectedMap(Map<? extends K, ? extends V> m) {
        super(m);
    }
}
