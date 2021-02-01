package com.blog.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

/**
 * @author lt
 * @date 2021/2/1 10:48
 */
public class HuTool extends Convert {
    public static void main(String[] args) {
        System.out.println("123456");
    }

    public static Long[] toLongArrays(Object value, Long[] defaultValue) {
        return convertQuietly(Long.class, value, defaultValue);
    }

    /**
     * 转换为字符串
     */
    @Test
    public void toStr() {
        int a = 1;
        String aStr = Convert.toStr(a);
        System.out.println(aStr);

        long[] b = {1, 2, 3, 4, 5};
        //bStr为："[1, 2, 3, 4, 5]"
        String bStr = Convert.toStr(b);
        System.out.println(bStr);

        int c = Convert.toInt("a", 0);//转换失败返回默认值0
        System.out.println(c);
    }

    /**
     * 转换为指定类型数组
     */
    @Test
    public void toArray() {
        String[] b = {"1", "2", "3", "4"};
        //结果为Integer数组
        Integer[] intArray = toIntArray(b);

        long[] c = {1, 2, 3, 4, 5};
        //结果为Integer数组
        Integer[] intArray2 = toIntArray(c);

        String[] d = {"a", "2", "2", "4", "6"};
        Long[] bool = null;
        Long[] bb = toLongArrays(d, bool);//转换失败返回默认值null
        System.out.println(bb);
    }

    /**
     * 日期转换
     */
    @Test
    public void toDate() {
        String a = "aa";
        Date value = toDate(a);
        System.out.println(value);
    }

    @Test
    public void toLists() {
        Object[] a = {"a", "你", "好", "", 1};
        List<?> list = Convert.convert(List.class, a);
        System.out.println(list);
        //从4.1.11开始可以这么用
        List<?> lists = Convert.toList(a);
        System.out.println(lists);
    }

    /**
     * 其他类型转换
     * 1标准类型
     * 通过Convert.convert(Class<T>, Object)方法可以将任意类型转换为指定类型，Hutool中预定义了许多类型转换，
     * 例如转换为URI、URL、Calendar等等，这些类型的转换都依托于ConverterRegistry类。通过这个类和Converter接口，
     * 我们可以自定义一些类型转换。详细的使用请参阅“自定义类型转换”一节。
     * <p>
     * 2泛型类型
     * 通过convert(TypeReference<T> reference, Object value)方法，自行new一个TypeReference对象可以对嵌套泛型进行类型转换。
     * 例如，我们想转换一个对象为List<String>类型，此时传入的标准Class就无法满足要求，此时我们可以这样：
     */
    @Test
    public void toListOther() {
        Object[] a = {"a", "你", "好", "", 1};
        List<String> list = Convert.convert(new TypeReference<List<String>>() {
        }, a);
    }

    /**
     * 半角转全角：
     */
    @Test
    public void toSBCs() {
        String a = "123456789";

        //结果为："１２３４５６７８９"
        String sbc = Convert.toSBC(a);
    }

    /**
     * 全角转半角：
     */
    @Test
    public void toDBCs() {
        String a = "１２３４５６７８９";

        //结果为"123456789"
        String dbc = Convert.toDBC(a);
    }

    /**
     * 16进制（Hex）
     */
    @Test
    public void toHex() {
//        转为16进制（Hex）字符串
        String a = "我是一个小小的可爱的字符串";
        //结果："e68891e698afe4b880e4b8aae5b08fe5b08fe79a84e58fafe788b1e79a84e5ad97e7aca6e4b8b2"
        String hex = Convert.toHex(a, CharsetUtil.CHARSET_UTF_8);

//      将16进制（Hex）字符串转为普通字符串:
//      结果为："我是一个小小的可爱的字符串"
        String raw = Convert.hexStrToStr(hex, CharsetUtil.CHARSET_UTF_8);

//      注意：在4.1.11之后hexStrToStr将改名为hexToStr
        String raws = Convert.hexToStr(hex, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * Unicode和字符串转换
     */
    @Test
    public void toUnicode() {
        String a = "我是一个小小的可爱的字符串";

//      结果为："\\u6211\\u662f\\u4e00\\u4e2a\\u5c0f\\u5c0f\\u7684\\u53ef\\u7231\\u7684\\u5b57\\u7b26\\u4e32"
        String unicode = Convert.strToUnicode(a);

//      结果为："我是一个小小的可爱的字符串"
        String raw = Convert.unicodeToStr(unicode);
    }

    /**
     *编码转换
     */
    @Test
    public void toConvertCharset() {

        String a = "我不是乱码";
        //转换后result为乱码
        String result = Convert.convertCharset(a, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1);
        String raw = Convert.convertCharset(result, CharsetUtil.ISO_8859_1, "UTF-8");
        Assert.assertEquals(raw, a);
    }

    /**
     *金额大小写转换
     * 注意 转换为大写只能精确到分（小数点儿后两位），之后的数字会被忽略。
     */
    @Test
    public void toDigitToChinese() {
        double a = 67556.32;

//      结果为："陆万柒仟伍佰伍拾陆元叁角贰分"
        String digitUppercase = Convert.digitToChinese(a);
        FileUtil.getOutputStream("d:/test2.txt");

    }
    @Test
    public void toStringUtil() {
        String template = "{}爱{}，就像老鼠爱大米";
        String str = StrUtil.format(template, "我", "你"); //str -> 我爱你，就像老鼠爱大米
    }
}
