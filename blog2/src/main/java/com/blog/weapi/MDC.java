package com.blog.weapi;


import com.blog.weapi.helpers.ThreadLocalMap;

import java.util.Hashtable;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-7-12 22:57:57
 */
public class MDC {
    final static MDC mdc = new MDC();
    static final int HT_SIZE = 7;
    Object tlm;

    private MDC() {
        tlm = new ThreadLocalMap();
    }

    /**
     * Put a context value (the <code>o</code> parameter) as identified
     * with the <code>key</code> parameter into the current thread's
     * context map.
     * <p/>
     * <p>If the current thread does not have a context map it is
     * created as a side effect.
     *
     * @param key
     * @param o
     */

    public static void put(String key, Object o) {
        mdc.put0(key, o);
    }

    public static void setUser(User user) {
        mdc.put0("XWEOSO_USER", user);
    }


    /**
     * Get the context identified by the <code>key</code> parameter.
     * <p/>
     * <p>This method has no side effects.
     */

    public static Object get(String key) {
        return mdc.get0(key);
    }

    public static boolean containsKey(String key) {
        return mdc.containsKey0(key);
    }

    public static User getUser() {
        return (User) mdc.get0("XWEOSO_USER");
    }


    public static String getSessionId() {
        return ((User) mdc.get0("XWEOSO_USER")).getUserid();
    }


    /**
     * Remove the the context identified by the <code>key</code>
     * parameter.
     *
     * @param key
     */

    public static void remove(String key) {
        mdc.remove0(key);
    }


    /**
     * Get the current thread's MDC as a hashtable. This method is
     * intended to be used internally.
     *
     * @return Hashtable
     */
    public static Hashtable getContext() {
        return mdc.getContext0();
    }

    /**
     * Remove all values from the MDC.
     *
     * @since 1.2.16
     */
    public static void clear() {
        mdc.clear0();
    }


    private void put0(String key, Object o) {
        Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
        if (ht == null) {
            ht = new Hashtable(HT_SIZE);
            ((ThreadLocalMap) tlm).set(ht);
        }
        ht.put(key, o);
    }

    private Object get0(String key) {
        Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
        if (ht != null && key != null) {
            return ht.get(key);
        } else {
            return null;
        }
    }

    private boolean containsKey0(String key) {
        Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
        return ht != null && key != null && ht.containsKey(key);
    }

    private void remove0(String key) {
        Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
        if (ht != null) {
            ht.remove(key);
        }
    }


    private Hashtable getContext0() {
        return (Hashtable) ((ThreadLocalMap) tlm).get();
    }

    private void clear0() {
        Hashtable ht = (Hashtable) ((ThreadLocalMap) tlm).get();
        if (ht != null) {
            ht.clear();
        }
    }


}
