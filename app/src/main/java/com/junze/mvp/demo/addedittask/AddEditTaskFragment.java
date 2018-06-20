package com.junze.mvp.demo.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.junze.mvp.demo.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 新增编辑，这个界面的功能只有一个输入之后显示完成和显示内容以及部分提示。
 *
 * @author 2018/6/15 15:24 / mengwei
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {


    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskContract.Presenter mPresenter;

    private TextView mTitle;//标题
    private TextView mDescription;//描述

    public AddEditTaskFragment() {
    }

    /**
     * fragment实例，每次都是新的
     *
     * @return
     */
    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.addtask_frag, container, false);
        mTitle = (TextView) root.findViewById(R.id.add_task_title);
        mDescription = (TextView) root.findViewById(R.id.add_task_description);
        setHasOptionsMenu(true);
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //完成按钮
        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存
                mPresenter.saveTask(mTitle.getText().toString(), mDescription.getText().toString());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(mTitle, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void showTaskList() {
        //保存完成会回调這个接口
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        //设置标题
        mTitle.setText(title);

    }

    @Override
    public void setDescription(String description) {
        //设置描述 ，其实這个可以和设置标题放一起的
        mDescription.setText(description);

    }

    @Override
    public boolean isActive() {
        //这个isAdded是fragment的方法
        //Return true if the fragment is currently added to its activity.
        return isAdded();
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
