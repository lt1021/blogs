package com.blog.dowload.controller;

import com.blog.base.result.ResponseData;
import com.blog.dowload.util.DowloadExcel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lt
 * @date 2021/3/5 11:51
 */
@RestController
public class ExcelController {
    public ExcelController() {
    }

    @PostMapping({"/api/excel/download"})
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        StringBuilder sb = new StringBuilder(50);
        sb.append("attachment;  filename=").append(fileName != null && !fileName.trim().equals("") ? fileName : "未命名文件").append(".xls");
        response.setContentType("application/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition", new String(sb.toString().getBytes(), "ISO8859-1"));
        ServletOutputStream out = response.getOutputStream();
        out.write(DowloadExcel.readFile(request.getParameter("uuid")));
        out.flush();
        out.close();
        DowloadExcel.deleteByMap(request.getParameter("uuid"));
    }

    @PostMapping({"/api/excel/sure"})
    @ResponseBody
    public ResponseData fileSize(String uuid) {
        Map<String, Integer> map = (Map) Arrays.stream(uuid.split(",")).collect(Collectors.toMap((d) -> {
            return d;
        }, (v) -> {
            return DowloadExcel.sure(v);
        }));
        return ResponseData.data(map);
    }
}
