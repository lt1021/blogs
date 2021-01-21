package com.blog.base.util;

/**
 * @author lt
 * @date 2021/1/21 12:07
 */

import org.springframework.util.StringUtils;

import java.util.Hashtable;

/**
 * 系统上下文
 *
 * @author xtiger (xtiger@microsoul.com) 2010-7-12 22:57:57
 */
public class ThreadMapUtil {
    /**
     * 平台token
     */
    public static String PLATFORM_TOKEN;

    private final static ThreadMapUtil mdc = new ThreadMapUtil();
    private static final int HT_SIZE = 7;
    private InheritableThreadLocal tlm;

    private ThreadMapUtil() {
        tlm = new InheritableThreadLocal() {
            public final Object childValue(Object parentValue) {
                Hashtable ht = (Hashtable) parentValue;
                if (ht != null) {
                    return ht.clone();
                } else {
                    return null;
                }
            }
        };
    }

    public static void putUserId(long userId) {
        put("userId", userId);
    }

    public static String getLanguage() {
        return mdc.get0("lang") == null ? "CN" : (String) mdc.get0("lang");
    }

    public static void setLanguage(String lang) {
        mdc.put0("lang", lang);
    }

    public static long getStaffId() {
        Object o = get("staffId");
        return StringUtils.isEmpty(o) ? 0 : Long.valueOf(o.toString());
    }

    public static void putStaffId(String staffId) {
        put("staffId", staffId);
    }

    public static long getUserId() {
        Object o = get("userId");
        return o == null ? 0 : (long) o;
    }

    public static void putToken(String token) {
        put("token", token);
    }

    public static String getToken() {
        Object o = get("token");
        return o == null ? "" : o.toString();
    }

    public static void putSelect(String select) {
        put("select", select == null ? "" : select);
    }

    public static void putField(String field) {
        put("field", field == null ? "" : field);
    }

    public static void putEField(String efield) {
        put("efield", efield == null ? "" : efield);
    }

    public static void putLang(String lang) {
        put("lang", lang == null ? "" : lang);
    }

    public static String getSelect() {
        Object o = get("select");
        return o == null ? "" : o.toString();
    }

    public static String getField() {
        Object o = get("field");
        return o == null ? "" : o.toString();
    }

    public static String getEField() {
        Object o = get("efield");
        return o == null ? "" : o.toString();
    }

    public static String getLang() {
        Object o = get("lang");
        return o == null ? "" : o.toString();
    }

    public static void putTaskId(long id) {
        put("taskId", id);
    }

    public static long getTaskId() {
        Object o = get("taskId");
        return o == null ? 0 : (long) o;
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

    /**
     * Remove the the context identified by the <code>key</code>
     * parameter.
     *
     * @param key
     */

    public static void remove(String key) {
        mdc.remove0(key);
    }

    public static void remove() {
        mdc.clear0();
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
        Hashtable ht = (Hashtable) tlm.get();
        if (ht == null) {
            ht = new Hashtable(HT_SIZE);
            tlm.set(ht);
        }
        ht.put(key, o);
    }

    private Object get0(String key) {
        Hashtable ht = (Hashtable) tlm.get();
        if (ht != null && key != null) {
            return ht.get(key);
        } else {
            return null;
        }
    }

    private boolean containsKey0(String key) {
        Hashtable ht = (Hashtable) tlm.get();
        return ht != null && key != null && ht.containsKey(key);
    }

    private void remove0(String key) {
        Hashtable ht = (Hashtable) ((InheritableThreadLocal) tlm).get();
        if (ht != null) {
            ht.remove(key);
        }
    }


    private Hashtable getContext0() {
        return (Hashtable) tlm.get();
    }

    private void clear0() {
        Hashtable ht = (Hashtable) tlm.get();
        if (ht != null) {
            ht.clear();
        }
    }
}
