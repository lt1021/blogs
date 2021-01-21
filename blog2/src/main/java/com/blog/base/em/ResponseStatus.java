package com.blog.base.em;

/**
 * @author lt
 * @date 2021/1/21 12:03
 */
public enum  ResponseStatus {

    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败
     */
    FAIL(500),

    /**
     * 无权限
     */
    NO_ACCESS(403),

    /**
     * 超时
     */
    TIMEOUT(502);

    private int status;

    private ResponseStatus(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    @Override
    public String toString() {
        return "ResponseStatus{" +
                "status=" + status +
                '}';
    }
}
