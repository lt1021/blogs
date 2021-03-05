package com.blog.weapi;

/**
 * Created by openxtiger.
 * User: xtiger
 */
public class Language {
    public static Language language;

    public static String translate(String lang, String cc) {
        return language.traditionalized(lang, cc);
    }

    public String traditionalized(String lang, String cc) {
        return cc;
    }
}
