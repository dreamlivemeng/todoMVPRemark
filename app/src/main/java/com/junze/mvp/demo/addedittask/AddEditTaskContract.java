package com.junze.mvp.demo.addedittask;

import com.junze.mvp.demo.BasePresenter;
import com.junze.mvp.demo.BaseView;

/**
 * 新增修改的接口
 *
 * @author 2018/6/15 15:23 / mengwei
 */
public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {
        /**
         * 不能为空的提示
         */
        void showEmptyTaskError();

        /**
         * 展示tile
         */
        void showTaskList();

        /**
         * 设置title
         *
         * @param title
         */
        void setTitle(String title);

        /**
         * 设置描述
         *
         * @param description
         */
        void setDescription(String description);

        /**
         * fragment是否添加到Activity中去
         *
         * @return
         */
        boolean isActive();
    }

    interface Presenter extends BasePresenter {
        void saveTask(String title, String description);//保存

        void populateTask();

        boolean isDataMissing();
    }
}
