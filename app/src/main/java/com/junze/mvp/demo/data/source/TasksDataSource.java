package com.junze.mvp.demo.data.source;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.junze.mvp.demo.data.Task;

import java.util.List;

/**
 * TaskDataSource是基础接口，做成基础接口
 * 访问任务数据的主要入口点
 *
 * @author 2018/6/14 14:44 / mengwei
 */
public interface TasksDataSource {

    interface LoadTasksCallBack {
        void onTasksLoaded(List<Task> tasks);

        void onDataNoAvailable();
    }

    interface GetTaskCallBack {
        void onTaskLoaded(Task task);

        void onDataNotAvailable();
    }

    void getTasks(@NonNull LoadTasksCallBack callBack);

    void getTasks(@NonNull String taskId,@NonNull GetTaskCallBack callBack);

    void saveTask(@NonNull Task task);//保存

    void completeTask(@NonNull Task task);//完成

    void completeTask(@NonNull String taskId);

    void activateTask(@NonNull Task task);

    void activateTask(@NonNull String taskId);

    void  clearCompletedTasks();//清除已完成的任务

    void refreshTask();//刷新任务

    void deleteAllTasks();//删除所有任务

    void deleteTask(@NonNull String taksId);//删除任务

}
