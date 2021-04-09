package com.blog.util;

import com.blog.base.util.ApplicationContextUtil;
import com.blog.pojo.Blog;
import com.blog.service.impl.BlogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.plugin.core.support.BeanListFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.testng.annotations.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class JSUtils {


    @PostMapping("/print")
    public static Object[] export(@RequestBody Map<String, Object> map) throws FileNotFoundException {
//        Object path = map.get("JS_PATH");
//        if (StringUtil.isEmpty(path)) {
//            return new Object[]{};
//        }

        //获取JS脚本信息
//        String JS = FileUpDownload.readURLToString(path.toString());
        //demo使用写死路径
        String JS = FileUpDownload.readFileToString("E:\\code\\blogs\\blog2\\src\\main\\resources\\static\\js\\export_demo.js");
//        String JS = path.toString();
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("nashorn");
        try {
            Invocable invocable = (Invocable) engine;
            //加载脚本
            engine.eval(JS);
            Object o = invocable.invokeFunction("ex", map);
            System.out.println(o);
            //如果不是指定数据类型则抛出异常
            if (!(o instanceof ArrayList)) {
//                throw new BusinessException("JS脚本返回值错误");
                System.out.println("错误");
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

    /*public static void main(String[] args) throws Exception {
        Map<String, Object> map = new HashMap<>();
//        map.put("JS_PATH", "127.0.0.1:8082/static/js_demo/export_demo.js");
        map.put("JS_PATH", "E:\\code\\blogs\\blog2\\src\\main\\resources\\static\\js\\export_demo.js");
        map.put("blogId", 1);
        export(map);
    }*/

//    @Test
    public static void js() throws FileNotFoundException {
        Map<String, Object> map = new HashMap<>();
        map.put("blogId", 1);
        Object[] export = export(map);
        System.out.println(export);
    }

}
