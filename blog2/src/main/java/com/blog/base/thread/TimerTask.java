package com.blog.base.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author lt
 * @date 2021/3/5 12:08
 */
public class TimerTask {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> taskHandle;
    private static TimerTask inst;
    private HashMap<String, ITaskRunning> tasks = new HashMap();

    public TimerTask() {
    }

    public static TimerTask getInst() {
        if (inst == null) {
            inst = new TimerTask();
        }

        return inst;
    }

    public synchronized void addTask(ITaskRunning task) {
        System.out.println("add task----" + task.getTaskId());
        this.tasks.put(task.getTaskId(), task);
    }

    public void taskBegin() {
        Runnable task = new Runnable() {
            public void run() {
                ArrayList<ITaskRunning> ltasks = new ArrayList();
                synchronized(this) {
                    ltasks.addAll(TimerTask.this.tasks.values());
                    TimerTask.this.tasks.clear();
                }

                try {
                    Iterator var2 = ltasks.iterator();

                    while(var2.hasNext()) {
                        ITaskRunning t = (ITaskRunning)var2.next();
                        t.setTaskRunning(true);

                        try {
                            t.runTask();
                        } catch (Exception var15) {
                            var15.printStackTrace();
                        } finally {
                            t.setTaskRunning(false);
                        }
                    }
                } finally {
                    ltasks.clear();
                }

            }
        };
        this.taskHandle = this.scheduler.scheduleAtFixedRate(task, 1L, 20L, TimeUnit.SECONDS);
    }

    public void taskEnd() {
        this.scheduler.shutdown();
    }
}
