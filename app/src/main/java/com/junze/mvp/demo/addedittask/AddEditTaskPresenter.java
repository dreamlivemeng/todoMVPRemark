package com.junze.mvp.demo.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.data.source.TasksDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 新增修改的P层
 *
 * @author 2018/6/15 15:25 / mengwei
 */
public class AddEditTaskPresenter implements AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallBack {

    @NonNull
    private final TasksDataSource mTaskRepository;
    @NonNull
    private final AddEditTaskContract.View mAddTskView;
    @Nullable
    private String mTaskId;//根据taskId来判断是新增还是修改

    private boolean mIsDataMissing;//数据是否丢失，默认其实应该是true，，需要用这个字段来判断是否需要再次获取数据

    public AddEditTaskPresenter(@Nullable String taskId,
                                @NonNull TasksDataSource taskRepository,
                                @NonNull AddEditTaskContract.View addTaskView,
                                boolean shouldLoadDataFromRepo) {
        mTaskId = taskId;
        mTaskRepository = checkNotNull(taskRepository);
        mAddTskView = checkNotNull(addTaskView);
        mIsDataMissing = shouldLoadDataFromRepo;
        mAddTskView.setPresenter(this);
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            //新增
            createTask(title, description);
        } else {
            //修改
            updateTask(title, description);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        //获取数据根据mtaskId
        mTaskRepository.getTasks(mTaskId, this);
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (mAddTskView.isActive()) {
            //回调显示title和描述
            mAddTskView.setTitle(task.getTitle());
            mAddTskView.setDescription(task.getDescription());
        }
        //如果查询到数据数据，那么设置为false
        mIsDataMissing = false;
    }

    @Override
    public void onDataNotAvailable() {
        if (mAddTskView.isActive()) {
            mAddTskView.showEmptyTaskError();
        }
    }

    @Override
    public boolean isDataMissing() {
        return mIsDataMissing;
    }

    @Override
    public void start() {
        //如果是新任务，并且mIsDataMiss为true
        if (!isNewTask() && mIsDataMissing) {
            populateTask();
        }
    }

    /**
     * 是否是新任务
     *
     * @return true是新任务，false不是新任务
     */
    private boolean isNewTask() {
        return mTaskId == null;
    }

    /**
     * 新建
     *
     * @param title
     * @param description
     */
    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if (newTask.isEmpty()) {
            mAddTskView.showEmptyTaskError();
        } else {
            mTaskRepository.saveTask(newTask);
            mAddTskView.showTaskList();
        }
    }

    /**
     * 更新
     *
     * @param title
     * @param description
     */
    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new.");
        }
        mTaskRepository.saveTask(new Task(title, description, mTaskId));
        mAddTskView.showTaskList();
    }
}
