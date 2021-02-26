package com.blog.util;

import lombok.extern.slf4j.Slf4j;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JSUtils {


    public static Object[] export(Map<String, Object> map) {
        Object path = map.get("JS_PATH");
        if (StringUtil.isEmpty(path)) {
            return new Object[]{};
        }

        //获取JS脚本信息
//        String JS = FileUpDownload.readURLToString(path.toString());
        //demo使用写死路径
//        String JS = FileUpDownload.readFileToString("E:\\java\\idea\\erp2.0\\erp_static\\static\\export_js\\export_demo.js");
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        try {
            Invocable invocable = (Invocable) engine;
            //加载脚本
//            engine.eval(JS);
            Object o = invocable.invokeFunction("ex", map);
            System.out.println(o);
            //如果不是指定数据类型则抛出异常
            if (!(o instanceof ArrayList)) {
//                throw new BusinessException("JS脚本返回值错误");
            }
            return ((ArrayList) o).toArray();
        } catch (ScriptException e) {
            e.printStackTrace();
            log.error("JS脚本错误");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            log.error("JS脚本找不到export方法");
        }
        return new Object[]{};
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("JS_PATH", "http://erp.w7os.cn/static/js_demo/export_demo.js");
        map.put("orderId", 1);
        export(map);
    }

}
