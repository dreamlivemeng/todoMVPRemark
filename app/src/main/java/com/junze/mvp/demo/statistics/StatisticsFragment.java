package com.junze.mvp.demo.statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.junze.mvp.demo.R;

/**
 * 统计fragment
 *
 * @author 2018/6/20 10:29 / mengwei
 */
public class StatisticsFragment extends Fragment implements StatisticsContract.View {


    private TextView mStatisticsTV;

    private StatisticsContract.Presenter mPresenter;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.statistics_frag, container, false);
        mStatisticsTV = root.findViewById(R.id.statistics);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();//在onResume里面去调用start方法，默认加载
    }


    @Override
    public void setProgressIndicator(boolean active) {//初始化加载
        if (active) {
            mStatisticsTV.setText("loading");
        } else {
            mStatisticsTV.setText("");
        }
    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompletedTasks) {
        if (numberOfCompletedTasks == 0 && numberOfIncompleteTasks == 0) {//两种任务都没有的时候显示
            mStatisticsTV.setText("You have no  tasks");
        } else {
            String displayString = "Active tasks:" + numberOfCompletedTasks + "\n" + "Completed tasks:" + numberOfCompletedTasks;
            mStatisticsTV.setText(displayString);
        }
    }

    @Override
    public void showLoadingStatisticsError() {
        mStatisticsTV.setText("Error loading statistics.");//错误提示
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(StatisticsContract.Presenter presenter) {
        mPresenter = Preconditions.checkNotNull(presenter);
    }
}
