package cn.lt.blog3.base.em;

import org.omg.CORBA.TIMEOUT;

/**
 * @author lt
 * @date 2021/4/7 18:26
 */
public enum ResponseStatus {
    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败
     */
    FAIL(500),

    /**
     * 没有权限
     */

    NO_ACCESS(403),

    /**
     * 超时
     */
    TIMEOUT(502);

    private int status;

    ResponseStatus(int status) {
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
    }}
