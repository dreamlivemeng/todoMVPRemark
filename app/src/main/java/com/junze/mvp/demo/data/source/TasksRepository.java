package com.junze.mvp.demo.data.source;

import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.util.LogUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * 将数据源中的任务加载到缓存中的具体实现
 *
 * @author 2018/6/14 14:43 / mengwei
 */
public class TasksRepository implements TasksDataSource {


    private static TasksRepository INSTANCE = null;
    private final TasksDataSource mTasksRemoteDataSource;
    private final TasksDataSource mTasksLocalDataSource;

    Map<String, Task> mCachedTasks;

    //将缓存标记为无效，以在下次请求数据时强制更新。 该变量具有程序包本地可见性，因此可以从测试中访问它。
    boolean mCacheIsDirty = false;

    private TasksRepository(@NonNull TasksDataSource tasksRemoteDataSource,
                            @NonNull TasksDataSource tasksLocalDataSource) {
        mTasksRemoteDataSource = checkNotNull(tasksRemoteDataSource);
        mTasksLocalDataSource = checkNotNull(tasksLocalDataSource);
    }

    /**
     * 获取实例
     *
     * @param tasksRemoteDataSource
     * @param tasksLocalDataSource
     * @return
     */
    public static TasksRepository getInstance(TasksDataSource tasksRemoteDataSource,
                                              TasksDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TasksRepository(tasksRemoteDataSource, tasksLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallBack callBack) {
        checkNotNull(callBack);
        if (mCachedTasks != null && !mCacheIsDirty) {
            callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            //todo
            getTasksFromRemoteDataSource(callBack);
        } else {
            mTasksLocalDataSource.getTasks(new LoadTasksCallBack() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
                }

                @Override
                public void onDataNoAvailable() {
                    getTasksFromRemoteDataSource(callBack);
                }
            });
        }
    }

    @Override
    public void getTasks(@NonNull final String taskId, @NonNull final GetTaskCallBack callBack) {

        checkNotNull(taskId);
        checkNotNull(callBack);

        final Task cachedTask = getTaskWithId(taskId);
        if (cachedTask != null) {
            callBack.onTaskLoaded(cachedTask);
            return;
        }

        //
        mTasksLocalDataSource.getTasks(taskId, new GetTaskCallBack() {
            @Override
            public void onTaskLoaded(Task task) {
                if (mCachedTasks == null) {
                    mCachedTasks = new LinkedHashMap<>();
                }
                mCachedTasks.put(task.getId(), task);
                callBack.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksRemoteDataSource.getTasks(taskId, new GetTaskCallBack() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        if (mCachedTasks == null) {
                            mCachedTasks = new LinkedHashMap<>();
                        }
                        mCachedTasks.put(task.getId(), task);
                        callBack.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callBack.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.saveTask(task);//保存的远程
        mTasksLocalDataSource.saveTask(task);//本地保存
        //放入到缓存
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
        mTasksLocalDataSource.completeTask(task);
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksRemoteDataSource.activateTask(task);
        mTasksLocalDataSource.activateTask(task);

        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.put(task.getId(), activeTask);
    }


    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRemoteDataSource.clearCompletedTasks();
        mTasksRemoteDataSource.clearCompletedTasks();
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Task>> iterator = mCachedTasks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Task> entry = iterator.next();
            if (entry.getValue().isCompleted()) {
                iterator.remove();
            }
        }
    }

    @Override
    public void refreshTask() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {

        mTasksRemoteDataSource.deleteAllTasks();
        mTasksLocalDataSource.deleteAllTasks();

        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();

    }

    @Override
    public void deleteTask(@NonNull String taksId) {
        mTasksRemoteDataSource.deleteTask(checkNotNull(taksId));
        mTasksLocalDataSource.deleteTask(checkNotNull(taksId));
        mCachedTasks.remove(taksId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallBack callBack) {
        mTasksRemoteDataSource.getTasks(new LoadTasksCallBack() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                LogUtil.e("onTasksLoaded  3");

                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callBack.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            }

            @Override
            public void onDataNoAvailable() {
                LogUtil.e("onDataNoAvailable  3");
                callBack.onDataNoAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null) {
            mCachedTasks = new LinkedHashMap<>();
        }
        mCachedTasks.clear();
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        mTasksLocalDataSource.deleteAllTasks();
        for (Task task : tasks) {
            mTasksLocalDataSource.saveTask(task);
        }
    }

    /**
     * 根据id获取Task
     *
     * @param id
     * @return
     */
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedTasks == null || mCachedTasks.isEmpty()) {
            return null;
        } else {
            return mCachedTasks.get(id);
        }
    }
}
