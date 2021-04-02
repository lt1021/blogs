package com.blog.weapi;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author xtiger (xtiger@microsoul.com) 2010-7-12 21:26:39
 */
public class XString implements Serializable {
    //private static  long serialVersionUID = "";
    private HashMap<String, String> strs = new HashMap<String, String>();
    private String str;


    public XString(String str) {
        if (str == null) return;
        String[] ss = str.split("[;:]");
        if (ss.length >= 2) {
            for (int i = 0; i < ss.length; i += 2) {
                if (ss[i].equals("zh_CN")) {
                    strs.put("zh_TW", Language.translate("zh_TW", ss[i + 1]));
                }
                strs.put(ss[i], ss[i + 1]);
            }
        }
        this.str = str;
    }

    @Override
    public String toString() {
        String lang = MDC.getUser() == null || MDC.getUser().getLang() == null ? "en_US" : MDC.getUser().getLang();
        //if ("zh_TW".equals(lang)) lang = "zh_CN";
        return strs.get(lang) == null ? strs.size() > 0 ? strs.values().iterator().next() : str : strs.get(lang);
    }
}
