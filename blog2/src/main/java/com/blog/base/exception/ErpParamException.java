package com.blog.base.exception;

/**
 * 如果提示信息带有占位符则用该异常处理。
 */
public class ErpParamException extends RuntimeException {

    private int status = 500;

    private Object data;

    public ErpParamException(String msg) {
        super(msg);
    }

    public ErpParamException(int status, String msg) {
        super(msg);
        this.status = status;
    }

    public ErpParamException(String msg, Object data) {
        super(msg);
        this.data = data;
    }


    public int getStatus() {
        return this.status;
    }

    public Object getData() {
        return data;
    }
}
