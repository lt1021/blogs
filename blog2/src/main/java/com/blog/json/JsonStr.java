package com.blog.json;

/**
 * Created by openxtiger.org.
 * User: xtiger
 * Date: 2010-2-1
 * Time: 21:50:55
 */
public class JsonStr implements Json {
    private String v;

    public JsonStr(String v) {
        this.v = v;
    }

    public String toString() {
        return v;
    }
}
