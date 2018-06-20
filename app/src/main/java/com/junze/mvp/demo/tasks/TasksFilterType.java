package com.junze.mvp.demo.tasks;

/**
 * Used with the filter spinner in the tasks list.
 * 与任务列表中的筛选器spinner一起使用。
 *
 * @author 2018/6/15 9:21 / mengwei
 */
public enum TasksFilterType {
    /***
     * 所有
     */
    ALL_TASKS,
    /**
     * 未完成的
     */
    ACTIVE_TASKS,
    /**
     * 已完成
     */
    COMPLETED_TASKS
}
