package com.junze.mvp.demo;

import android.content.Context;
import android.support.annotation.NonNull;

import com.junze.mvp.demo.data.source.TasksRepository;
import com.junze.mvp.demo.data.source.local.TasksLocalDataSource;
import com.junze.mvp.demo.data.source.local.ToDoDataBase;
import com.junze.mvp.demo.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 2018/6/15 10:36 / mengwei
 */
public class Injection {

    public static TasksRepository provideTasksRepository(@NonNull Context context) {

        checkNotNull(context);
        ToDoDataBase dataBase = ToDoDataBase.getInstance(context);
        return TasksRepository.getInstance(FakeTasksRemoteDataSource.getInstance(),
                TasksLocalDataSource.getInstance(new AppExecutors(), dataBase.taskDao()));
    }
}

