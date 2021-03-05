package com.blog.json;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-3-15 21:02:15
 */
public class JsonList<E> extends ArrayList<E> implements Bean {
    public JsonList(int initialCapacity) {
        super(initialCapacity);
    }

    public JsonList() {
    }

    public JsonList(Collection<? extends E> c) {
        super(c);
    }
}
