package com.blog.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-3-15 21:02:15
 */
public class JsonProtectedList<E> extends ArrayList<E> implements ProtectedBean {
    public JsonProtectedList(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonProtectedList() {
    }

    public JsonProtectedList(Collection<? extends E> c) {
        super(c);
    }
}
