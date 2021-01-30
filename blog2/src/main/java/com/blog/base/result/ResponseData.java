package com.blog.base.result;

import com.blog.base.em.ResponseStatus;
import com.blog.base.util.LanguageUtils;

import java.util.List;
/**
 * @author lt
 * @date 2021/1/21 11:57
 */
public class ResponseData<T>{
    /**
     * 状态码，默认成功。
     */
    private ResponseStatus status;

    private int code = 200;

    /**
     * 响应某些提示
     */
    private String message;

    /**
     * 数据
     */
    private T data;


    public static ResponseData data() {
        ResponseData responseData = new ResponseData();
        return responseData;
    }

    public static ResponseData data(ResponseStatus status) {
        ResponseData responseData = new ResponseData(status);
        return responseData;
    }

    public static <T> ResponseData<T> data(T t) {
        ResponseData responseData = new ResponseData(t);
        if (t instanceof Boolean) {
            responseData.setStatus((Boolean) t ? ResponseStatus.SUCCESS : ResponseStatus.FAIL);
        }
        return responseData;
    }

    public static <T> ResponseData<T> data(List<T> list) {
        ResponseData responseData = new ResponseData(list);
        return responseData;
    }

    public static ResponseData data(ResponseStatus status, String message) {
        ResponseData responseData = new ResponseData(status, LanguageUtils.msg(message));
        return responseData;
    }

    public static <T> ResponseData<T> data(ResponseStatus status, String message, T t) {
        ResponseData<T> responseData = new ResponseData(status, LanguageUtils.msg(message), t);
        return responseData;
    }

    public static <T> ResponseData<T> data(ResponseStatus status, T t) {
        ResponseData<T> responseData = new ResponseData(status, t);
        return responseData;
    }

    public static <T> ResponseData<T> data(ResponseStatus status, List<T> t) {
        ResponseData<T> responseData = new ResponseData(status, t);
        return responseData;
    }

    public static <T> ResponseData<T> data(String message, T t) {
        ResponseData<T> responseData = new ResponseData(LanguageUtils.msg(message), t);
        return responseData;
    }

    public static <T> ResponseData<T> data(String message, List<T> t) {
        ResponseData<T> responseData = new ResponseData(LanguageUtils.msg(message), t);
        return responseData;
    }

    public static <T> ResponseData<T> data(ResponseStatus status, String message, List<T> t) {
        ResponseData<T> responseData = new ResponseData(status, LanguageUtils.msg(message), t);
        return responseData;
    }

    public static <T> ResponseData<T> fail() {
        ResponseData responseData = new ResponseData();
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static <T> ResponseData<T> fail(String message) {
        ResponseData responseData = new ResponseData(LanguageUtils.msg(message));
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static <T> ResponseData<T> fail(T t) {
        ResponseData responseData = new ResponseData(t);
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static <T> ResponseData<T> fail(List<T> list) {
        ResponseData responseData = new ResponseData(list);
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static ResponseData fail(ResponseStatus status, String message) {
        ResponseData responseData = new ResponseData(status, LanguageUtils.msg(message));
        return responseData;
    }

    public static <T> ResponseData<T> fail(ResponseStatus status, String message, T t) {
        ResponseData<T> responseData = new ResponseData(status, LanguageUtils.msg(message), t);
        return responseData;
    }

    public static <T> ResponseData<T> fail(ResponseStatus status, T t) {
        ResponseData<T> responseData = new ResponseData(status, t);
        return responseData;
    }

    public static <T> ResponseData<T> fail(ResponseStatus status, List<T> t) {
        ResponseData<T> responseData = new ResponseData(status, t);
        return responseData;
    }

    public static <T> ResponseData<T> fail(String message, T t) {
        ResponseData<T> responseData = new ResponseData(LanguageUtils.msg(message), t);
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static <T> ResponseData<T> fail(String message, List<T> t) {
        ResponseData<T> responseData = new ResponseData(LanguageUtils.msg(message), t);
        responseData.setStatus(ResponseStatus.FAIL);
        return responseData;
    }

    public static <T> ResponseData<T> fail(ResponseStatus status, String message, List<T> t) {
        ResponseData<T> responseData = new ResponseData(status, LanguageUtils.msg(message), t);
        return responseData;
    }


    public ResponseData() {
    }

    public ResponseData(ResponseStatus status) {
        this.code = status.getValue();
        this.status = status;
    }

    public ResponseData(String message) {
        this.message = message;
    }

    public ResponseData(T data) {
        this.data = data;
    }

    public ResponseData(ResponseStatus status, String message) {
        this.code = status.getValue();
        this.status = status;
        this.message = message;
    }

    public ResponseData(ResponseStatus status, T data) {
        this.code = status.getValue();
        this.status = status;
        this.data = data;
    }

    public ResponseData(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResponseData(ResponseStatus status, String message, T data) {
        this.code = status.getValue();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.code = status.getValue();
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        if (status != null) {
            code = status.getValue();
        }
        return code;
    }
}
