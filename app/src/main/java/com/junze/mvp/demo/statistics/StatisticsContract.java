package com.junze.mvp.demo.statistics;

import com.junze.mvp.demo.BasePresenter;
import com.junze.mvp.demo.BaseView;

/**
 * 统计相关的UI接口和逻辑接口
 *
 * @author 2018/6/20 10:29 / mengwei
 */
public interface StatisticsContract {

    interface View extends BaseView<Presenter> {
        /**
         * 设置加载提示
         *
         * @param active
         */
        void setProgressIndicator(boolean active);

        /**
         * 回调已完成和未完成的数量
         *
         * @param numberOfIncompleteTasks active数量
         * @param numberOfCompletedTasks  已完成数量
         */
        void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks);

        /**
         * 加载错误提示
         */
        void showLoadingStatisticsError();

        /**
         * 返回fragment是否加入到activity中
         *
         * @return
         */
        boolean isActive();
    }

    /**
     * //這个界面没有单独的逻辑接口，但是在BasPresenter里面有一个通用的start()接口供调用
     */
    interface Presenter extends BasePresenter {
    }
}
