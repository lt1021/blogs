package com.blog.weapi;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xtiger (xtiger@microsoul.com) 2014-09-25 23:54
 */
public class Tools {
    public static String parseString(String v) {
        return v == null ? "" : ((String) v).trim();
    }

    public static String format(String input, Map value) {
        Pattern pt = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher mc = pt.matcher(input);
        int index = 0;
        String matchList = "";
        while (mc.find()) {
            String match = input.subSequence(index, mc.start()).toString();
            matchList += (match);
            matchList += (parseString((String) value.get(mc.group(1))));
            index = mc.end();
        }
        if (index <= input.length() - 1) {
            matchList += (input.subSequence(index, input.length()).toString());
        }
        return matchList;
    }
}
