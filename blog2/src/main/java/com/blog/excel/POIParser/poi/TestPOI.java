package com.blog.excel.POIParser.poi;

import cn.hutool.core.util.IdUtil;
import com.blog.base.result.ResponseData;
import com.blog.base.util.LanguageUtils;
import com.blog.dowload.util.DowloadExcel;
import com.blog.excel.POIParser.poi.enity.TestUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lt
 * @date 2021/3/5 11:37
 */
@Controller(/*"/excel"*/)
public class TestPOI {
    protected String URL= "test001";

//    @GetMapping("exoprt")
    @PostMapping({"exoprte"})
//    @Export
    public ResponseData test(){
        Method method = null;
        try {
            Class cla = TestPOI.class;
            method = cla.getMethod("exportExcel", Map.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return ResponseData.fail(LanguageUtils.msg("错误"));
        }
        String path = URL;
        if (path == null || method == null) {
            return ResponseData.fail(LanguageUtils.msg("错误"));
        }
        String lang = "";
        return ResponseData.data(DowloadExcel.exportExcel(path.toString(), lang, TestPOI.class, method, true, Map.class));

    }


    public Object[] exportExcel(Map<String, Object> parse) {
        List<TestUser> list = new ArrayList<>();
        for (int i = 0 ; i < 50 ; i++) {
            TestUser user = new TestUser();
            user.setAge(i);
            user.setName(IdUtil.randomUUID());
            user.setMoney(new BigDecimal(i+1));

            list.add(user);
        }

        parse.put("list",list);
        return  new Object[]{parse};
    }


}
