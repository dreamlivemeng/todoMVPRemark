package com.junze.mvp.demo.taskdetails;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Strings;
import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.data.source.TasksDataSource;
import com.junze.mvp.demo.data.source.TasksRepository;
import com.junze.mvp.demo.tasks.TasksPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * P层，接口实现类，处理一些逻辑
 *
 * @author 2018/6/19 15:43 / mengwei
 */
public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TasksRepository mTaskRepository;

    private final TaskDetailContract.View mTaskDetailView;

    @Nullable
    private String mTaskId;

    public TaskDetailPresenter(@Nullable String taskId, @NonNull TasksRepository tasksRepository,
                               @NonNull TaskDetailContract.View taskDetailView) {
        mTaskId = taskId;

        mTaskRepository = checkNotNull(tasksRepository, "tasksRepository 不能为空");
        mTaskDetailView = checkNotNull(taskDetailView, "taskDetailView 不能为空");

        mTaskDetailView.setPresenter(this);
    }

    @Override
    public void editTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskDetailView.showEditTask(mTaskId);
    }


    @Override
    public void deleteTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }

        mTaskRepository.deleteTask(mTaskId);
        mTaskDetailView.showTaskDeleted();
    }

    @Override
    public void completeTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskRepository.completeTask(mTaskId);
        mTaskDetailView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (Strings.isNullOrEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskRepository.activateTask(mTaskId);
        mTaskDetailView.showTaskMarkedActive();
    }

    @Override
    public void start() {
        openTask();
    }

    private void openTask() {
        if (TextUtils.isEmpty(mTaskId)) {
            mTaskDetailView.showMissingTask();
            return;
        }
        mTaskDetailView.setLoadingIndicator(true);
        mTaskRepository.getTasks(mTaskId, new TasksDataSource.GetTaskCallBack() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!mTaskDetailView.isActive()) {
                    return;
                }

                mTaskDetailView.setLoadingIndicator(false);
                if (null == task) {
                    mTaskDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {

                if (!mTaskDetailView.isActive()) {
                    return;
                }
                mTaskDetailView.showMissingTask();
            }
        });
    }

    private void showTask(@NonNull Task task) {
        String title = task.getTitle();
        String description = task.getDescription();
        if (TextUtils.isEmpty(title)) {
            mTaskDetailView.hideTitle();
        } else {
            mTaskDetailView.showTitle(title);
        }

        if (TextUtils.isEmpty(description)) {
            mTaskDetailView.hideDescription();
        } else {
            mTaskDetailView.showDescription(description);
        }
        mTaskDetailView.showCompletionStatus(task.isCompleted());
    }
}
