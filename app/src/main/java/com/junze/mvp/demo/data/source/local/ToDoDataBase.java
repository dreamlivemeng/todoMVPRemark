package com.junze.mvp.demo.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.junze.mvp.demo.data.Task;

/**
 * @author 2018/6/14 16:31 / mengwei
 */
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class ToDoDataBase extends RoomDatabase {

    private static ToDoDataBase INSTANCE;

    public abstract TasksDao taskDao();

    private static final String DATABASE_NAME = "Tasks.db";
    private static final Object sLock = new Object();

    public static ToDoDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDataBase.class, DATABASE_NAME).build();
            }
            return INSTANCE;
        }
    }
}
