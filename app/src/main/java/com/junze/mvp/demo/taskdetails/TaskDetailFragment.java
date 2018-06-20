package com.junze.mvp.demo.taskdetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.junze.mvp.demo.R;
import com.junze.mvp.demo.addedittask.AddEditTaskActivity;
import com.junze.mvp.demo.addedittask.AddEditTaskFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author 2018/6/19 15:42 / mengwei
 */
public class TaskDetailFragment extends Fragment implements TaskDetailContract.View {

    @NonNull
    private static final String ARGUMENT_TASK_ID = "TASK_ID";

    @NonNull
    static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter mPresenter;

    private TextView mDetailTitle;

    private TextView mDetailDescription;

    private CheckBox mDetailCompleteStatus;


    /**
     * fragment实例
     *
     * @param taskId
     * @return
     */
    public static TaskDetailFragment newInstance(@Nullable String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.taskdetail_frag, container, false);

        mDetailTitle = (TextView) root.findViewById(R.id.task_detail_title);
        mDetailDescription = (TextView) root.findViewById(R.id.task_detail_description);
        mDetailCompleteStatus = (CheckBox) root.findViewById(R.id.task_detail_complete);

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_edit_task);
        //设置floating action button按钮事件
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个编辑很有意思，先由V层通知P层做一个逻辑判断（mTaskId是否为空），如果为空由P层告诉V层提示不能跳转，如果不为空也有P层告诉V层跳转到编辑任务的Activity。
                mPresenter.editTask();
            }
        });
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_delete:
                //右上角的menu item 监听事件通知P层需要删除任务。P层再去通知V层
                mPresenter.deleteTask();
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            mDetailTitle.setText("");
            mDetailDescription.setText("加载中");
        }
    }

    @Override
    public void showMissingTask() {
        //没有数据的提示
        mDetailTitle.setText("");
        mDetailDescription.setText("No data");
    }

    @Override
    public void hideTitle() {
        //隐藏title
        mDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        //显示title
        mDetailTitle.setVisibility(View.VISIBLE);
        mDetailTitle.setText(title);
    }

    @Override
    public void hideDescription() {
        //隐藏描述
        mDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        //显示描述
        mDetailDescription.setVisibility(View.VISIBLE);
        mDetailDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(boolean complete) {
        Preconditions.checkNotNull(mDetailCompleteStatus);
        //改变状态
        mDetailCompleteStatus.setChecked(complete);
        //设置监听
        mDetailCompleteStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPresenter.completeTask();
                } else {
                    mPresenter.activateTask();
                }
            }
        });
    }

    @Override
    public void showEditTask(String taskId) {
        //P层他通知V层跳转到编辑页面
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        //当删除成功P层告诉V需要关闭页面
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        //P层告诉V层需要提示
        Snackbar.make(getView(), "Task marked complete", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskMarkedActive() {
        //P层告诉V层需要提示
        Snackbar.make(getView(), "Task marked active", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().finish();
            }
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        //P层传给V层Presenter的实例，供V层调用来通知P层
        mPresenter = checkNotNull(presenter);
    }
}
