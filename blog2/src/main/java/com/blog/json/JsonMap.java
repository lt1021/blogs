package com.blog.json;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-3-15 21:01:31
 */
public class JsonMap<K, V> extends HashMap<K, V> implements Bean {
    public JsonMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public JsonMap(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonMap() {
    }


    public JsonMap(Map<? extends K, ? extends V> m) {
        super(m);
    }
}
