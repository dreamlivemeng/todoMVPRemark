package com.junze.mvp.demo.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.data.source.TasksDataSource;
import com.junze.mvp.demo.util.AppExecutors;
import com.junze.mvp.demo.util.LogUtil;

import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 本地数据存储
 *
 * @author 2018/6/14 16:10 / mengwei
 */
public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TasksDao mTasksDao;

    private AppExecutors mAppExecutors;


    private TasksLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull TasksDao tasksDao) {
        mAppExecutors = appExecutors;
        mTasksDao = tasksDao;
    }


    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(appExecutors, tasksDao);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void getTasks(@NonNull final LoadTasksCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = mTasksDao.getTasks();
                if (tasks.isEmpty()) {
                    LogUtil.e("onDataNoAvailable 执行1");
                    callBack.onDataNoAvailable();
                } else {
                    LogUtil.e("onTasksLoaded 执行");
                    callBack.onTasksLoaded(tasks);
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getTasks(@NonNull final String taskId, @NonNull final GetTaskCallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = mTasksDao.getTaskById(taskId);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task != null) {
                            callBack.onTaskLoaded(task);
                        } else {
                            LogUtil.e("onDataNoAvailable 执行2");
                            callBack.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.insertTask(task);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull final Task task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), false);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteCompletedTasks();
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void refreshTask() {

    }

    @Override
    public void deleteAllTasks() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTasks();
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteTask(@NonNull final String taksId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTaskById(taksId);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    static void clearInstance() {
        INSTANCE = null;
    }
}
