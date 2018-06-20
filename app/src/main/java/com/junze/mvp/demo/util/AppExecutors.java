package com.junze.mvp.demo.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 全局执行程序池用于整个应用程序
 * java 多线程讲解
 *https://www.cnblogs.com/study-everyday/archive/2017/04/20/6737428.html
 * @author 2018/6/14 11:24 / mengwei
 */
public class AppExecutors {

    private static final int THREAD_COUNT = 3;//线程数量

    private final Executor diskIO;

    private final Executor networkIO;

    private final Executor mainThread;

    //@VisibleForTesting注释一个已经存在的程序元素或者所注释的元素被给于它所必须的更宽松的 可见性，仅仅被使用在测试代码中。
    @VisibleForTesting
    AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor networkIO() {
        return networkIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public AppExecutors() {
        this(new DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT), new MainThreadExecutor());
    }

    public static class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
