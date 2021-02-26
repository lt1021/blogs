package com.blog.base.exception;

/**
 * 提示信息没有占位符则用该异常处理
 */
public class ErpBaseException extends RuntimeException {
    private int status = 500;

    private Object data;

    public ErpBaseException(String msg) {
        super(msg);
    }

    public ErpBaseException(int status, String msg) {
        super(msg);
        this.status = status;
    }


    public ErpBaseException(String msg, Object data) {
        super(msg);
        this.status = status;
        this.data = data;
    }

    public int getStatus() {
        return this.status;
    }

    public Object getData() {
        return data;
    }
}
