package com.blog.base.thread;

/**
 * @author lt
 * @date 2021/3/5 12:08
 */
public interface ITaskRunning {
    String getTaskId();

    void runTask();

    boolean isTaskRunning();

    void setTaskRunning(boolean isRuning);
}
