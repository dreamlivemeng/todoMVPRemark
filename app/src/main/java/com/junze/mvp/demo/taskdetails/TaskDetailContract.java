package com.junze.mvp.demo.taskdetails;

import com.junze.mvp.demo.BasePresenter;
import com.junze.mvp.demo.BaseView;

/**
 * @author 2018/6/19 15:42 / mengwei
 */
public interface TaskDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showMissingTask();

        /**
         * 隐藏title
         */
        void hideTitle();

        /**
         * 显示title
         *
         * @param title
         */
        void showTitle(String title);

        /**
         * 隐藏描述
         */
        void hideDescription();

        /**
         * 显示描述
         *
         * @param description
         */
        void showDescription(String description);

        /**
         * 显示状态
         *
         * @param complete
         */
        void showCompletionStatus(boolean complete);

        /**
         * 跳转任务编辑页面
         *
         * @param taskId
         */
        void showEditTask(String taskId);

        /**
         * 删除成功后关闭页面回调
         */
        void showTaskDeleted();

        /**
         * 任务设置为完成提示
         */
        void showTaskMarkedComplete();

        /**
         * 任务设置为Active的提示
         */
        void showTaskMarkedActive();

        /**
         * fragment是否添加到Activity的回调
         *
         * @return
         */
        boolean isActive();

    }

    interface Presenter extends BasePresenter {
        /**
         * 编辑任务
         */
        void editTask();

        /**
         * 删除任务
         */
        void deleteTask();

        /**
         * 设置任务完成
         */
        void completeTask();

        /**
         * 设置任务为未完成active
         */
        void activateTask();
    }
}
