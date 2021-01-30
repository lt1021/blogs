package com.blog.base.util;

/**
 * @author lt
 * @date 2021/1/21 12:05
 */

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 国际化语言工具
 */
@Slf4j
public class LanguageUtils {
    /**
     * 语言类型
     */
    private final static Locale[] LOCALES = new Locale[]{Locale.SIMPLIFIED_CHINESE, Locale.US};

    public final static Map<String, ResourceBundle> LANGUAGE_MAP = new HashMap<>(LOCALES.length);

    private final static Pattern placeholderPattern = Pattern.compile("\\{\\d+\\}");

    /**
     * 默认语言为中文
     */
    private final static String DEFAULT_LANGUAGE = "CN";


    /**
     * 初始化
     */
    public static void init() {
        log.info("初始化语言！！！！！");
        for (Locale locale : LOCALES) {
            LANGUAGE_MAP.put(locale.getCountry(), ResourceBundle.getBundle("language/message", locale));
        }
        log.info("初始化语言完成！！！！！");
    }

    public static String msg(String msg) {
        ResourceBundle resource = getResource();
        if (resource == null) {
            return msg;
        }
        return format(resource.getLocale(), convert(resource, msg));
    }

    public static String msg(String msg, Object... params) {
        if (params == null) {
            return msg(msg);
        }
        ResourceBundle resource = getResource();
        if (resource == null) {
            return msg;
        }
        return format(resource.getLocale(), convert(resource, msg), params);
    }

    private static ResourceBundle getResource() {
        ResourceBundle resourceBundle = LANGUAGE_MAP.get(ThreadMapUtil.getLanguage());
        //如果没有获取到当前语言则返回默认语言
        return resourceBundle == null ? LANGUAGE_MAP.get(DEFAULT_LANGUAGE) : resourceBundle;
    }


    private static String format(Locale locale, String msg, Object... params) {
        //如果有占位符而没有参数时
        if (msg.contains("{0}") && (params == null || params.length == 0)) {
            Matcher matcher = placeholderPattern.matcher(msg);
            int i = 1;
            while (matcher.find()) {
                System.out.println(i++);
            }
            if (i > 1) {
                params = new String[i - 1];
                for (int j = 0; j < params.length; j++) {
                    params[j] = "";
                }
            }
        }
        MessageFormat format = new MessageFormat(msg, locale);
        return format.format(params);
    }

    private static String convert(ResourceBundle resourceBundle, String key) {
        if (key == null) {
            return "";
        }
        if (!resourceBundle.keySet().contains(key)) {
            log.info(String.format("key【%s】不存在！", key));
            return "";
        }
        try {
            String str = resourceBundle.getString(key);
            return new String(str.getBytes("ISO-8859-1"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
