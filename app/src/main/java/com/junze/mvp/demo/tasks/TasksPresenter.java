package com.junze.mvp.demo.tasks;

import android.support.annotation.NonNull;

import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.data.source.TasksDataSource;
import com.junze.mvp.demo.data.source.TasksRepository;
import com.junze.mvp.demo.util.EspressoldingResource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 2018/6/14 17:26 / mengwei
 */
public class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository mTasksRepository;

    private final TasksContract.View mTasksView;

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private boolean mFirstLoad = true;

    public TasksPresenter(@NonNull TasksRepository tasksRepository, @NonNull TasksContract.View tasksView) {
        mTasksRepository = checkNotNull(tasksRepository, "tasksRepository 不能为空");
        mTasksView = checkNotNull(tasksView, "tasksView 不能为空");
        mTasksView.setPresenter(this);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        mTasksView.showSuccessfullySavedMessage();
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mTasksRepository.refreshTask();
        }
        // TODO: 2018/6/15  
//        EspressoldingResource.

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallBack() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                // TODO: 2018/6/15
//                if
                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (!mTasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false);
                }
                processTasks(tasksToShow);
            }

            @Override
            public void onDataNoAvailable() {
                if (!mTasksView.isActive()) {
                    return;
                }
                mTasksView.showLoadingTasksError();
            }
        });
    }

    /**
     * 处理数据
     *
     * @param tasks
     */
    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            processEmptyTasks();
        } else {
            mTasksView.showTasks(tasks);
            showFilterLabel();
        }
    }

    /**
     * 回调选择的filter标签
     */
    private void showFilterLabel() {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mTasksView.showAllFilterLabel();
                break;
            case ACTIVE_TASKS:
                mTasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                mTasksView.showCompletedFilterLable();
                break;
            default:
                mTasksView.showAllFilterLabel();
                break;
        }
    }

    /**
     * 当数据为空的时候，回调显示
     */
    private void processEmptyTasks() {
        switch (mCurrentFiltering) {
            case ALL_TASKS:
                mTasksView.showNoTasks();
                break;
            case ACTIVE_TASKS:
                mTasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                mTasksView.showNoCompletedTasks();
                break;
            default:
                mTasksView.showNoTasks();
                break;
        }
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestTask) {
        checkNotNull(requestTask, "requestedTask不能为空");
        mTasksView.showTaskDetailsUi(requestTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask, "completedTask 不能为空");
        mTasksRepository.completeTask(completedTask);
        mTasksView.showTaskMarkedComplete();
        loadTasks(false, false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

        checkNotNull(activeTask, "activeTask不能为空");
        mTasksRepository.activateTask(activeTask);
        mTasksView.showTaskMarkedActive();
        loadTasks(false, false);
    }

    @Override
    public void clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks();
        mTasksView.showCompletedTasksCleared();
        loadTasks(false, false);
    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return mCurrentFiltering;
    }

    @Override
    public void start() {
        loadTasks(false);
    }
}
