package com.junze.mvp.demo.tasks;

import android.support.annotation.NonNull;

import com.junze.mvp.demo.BasePresenter;
import com.junze.mvp.demo.BaseView;
import com.junze.mvp.demo.data.Task;

import java.util.List;

/**
 * 其模块内都把View和Presenter再次封装到TasksContract统一的接口容器里面。
 * >MVP是通过接口的方式来解耦，所以View和Presenter都是通过接口来解耦。
 *
 * @author 2018/6/14 18:07 / mengwei
 */
public interface TasksContract {

    interface View extends BaseView<Presenter> {
        /**
         * 加载提示
         *
         * @param active
         */
        void setLoadingIndicator(boolean active);

        /**
         * 展示任务列表
         *
         * @param tasks
         */
        void showTasks(List<Task> tasks);

        /**
         * 跳转到增加任务页面
         */
        void showAddTask();

        /**
         * 跳转到详情页面
         *
         * @param taskId
         */
        void showTaskDetailsUi(String taskId);

        /**
         * 标记任务完成提示
         */
        void showTaskMarkedComplete();

        /**
         * 标记任务为activie提示
         */
        void showTaskMarkedActive();

        /**
         * 清除已完成任务的提示
         */
        void showCompletedTasksCleared();

        /**
         * 加载任务出错提示
         */
        void showLoadingTasksError();


        /**
         * 展示active的label
         */
        void showActiveFilterLabel();

        /**
         * 展示完成任务的label
         */
        void showCompletedFilterLable();

        /**
         * 展示所有筛选的label
         */
        void showAllFilterLabel();

        /**
         * 展示没有 active的任务提示
         */
        void showNoActiveTasks();

        /**
         * 展示没有 完成任务的提示
         */
        void showNoCompletedTasks();

        /**
         * 没有任务的提示
         */
        void showNoTasks();

        /**
         * 展示保存成功的提示
         */
        void showSuccessfullySavedMessage();

        /**
         * fragment是否添加到Activity的回调
         *
         * @return
         */
        boolean isActive();

        /**
         * 显示筛选pop
         */
        void showFilteringPopUpMenu();

    }

    interface Presenter extends BasePresenter {
        /**
         * onActivityResult的事件回传
         *
         * @param requestCode
         * @param resultCode
         */
        void result(int requestCode, int resultCode);

        /**
         * 加载任务
         *
         * @param forceUpdate
         */
        void loadTasks(boolean forceUpdate);

        /**
         * 添加新任务
         */
        void addNewTask();

        /**
         * 打开任务详情
         * @param requestTask
         */
        void openTaskDetails(@NonNull Task requestTask);

        /**
         * 设置任务完成
         * @param completedTask
         */
        void completeTask(@NonNull Task completedTask);

        /**
         * 设置任务为活动的
         * @param activeTask
         */
        void activateTask(@NonNull Task activeTask);

        /**
         * 清除已完成的任务
         *
         */
        void clearCompletedTasks();

        /**
         * 设置筛选类型
         * @param requestType
         */
        void setFiltering(TasksFilterType requestType);

        /**
         * 获取筛选类型
         * @return
         */
        TasksFilterType getFiltering();
    }
}
