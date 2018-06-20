package com.junze.mvp.demo.statistics;


import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.junze.mvp.demo.data.Task;
import com.junze.mvp.demo.data.source.TasksDataSource;
import com.junze.mvp.demo.data.source.TasksRepository;

import java.util.List;


/**
 * 统计的Presenter
 *
 * @author 2018/6/20 10:29 / mengwei
 */
public class StatisticsPresenter implements StatisticsContract.Presenter {

    private final TasksRepository mTasksRepository;
    private final StatisticsContract.View mStatisticsView;

    public StatisticsPresenter(@NonNull TasksRepository tasksRepository,
                               @NonNull StatisticsContract.View statisticsView) {
        mTasksRepository = Preconditions.checkNotNull(tasksRepository, "mTasksRepository cannot be" +
                " null");
        mStatisticsView = Preconditions.checkNotNull(statisticsView, "mStatisticsView cannot be" +
                " null");

    }


    @Override
    public void start() {
        loadStatistics();
    }


    /**
     * 获取所有的任务，循环统计完成和未完成的数量
     */
    private void loadStatistics() {

        mStatisticsView.setProgressIndicator(true);
        //  EspressoIdlingResource.increment(); // App is busy until further notice

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallBack() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int activeTasks = 0;
                int completedTasks = 0;
                /*if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                    EspressoIdlingResource.decrement(); // Set app as idle.
                }*/

                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks += 1;
                    } else {
                        activeTasks += 1;
                    }
                }
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.setProgressIndicator(false);
                mStatisticsView.showStatistics(activeTasks, completedTasks);
            }

            @Override
            public void onDataNoAvailable() {
                if (!mStatisticsView.isActive()) {
                    return;
                }
                mStatisticsView.showLoadingStatisticsError();
            }
        });

    }
}
