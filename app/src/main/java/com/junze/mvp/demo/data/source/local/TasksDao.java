package com.junze.mvp.demo.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.junze.mvp.demo.data.Task;

import java.util.List;

/**
 * @author 2018/6/14 15:53 / mengwei
 */
@Dao
public interface TasksDao {

    /**
     * 查询所有
     *
     * @return
     */
    @Query("SELECT * FROM Tasks")
    List<Task> getTasks();

    /**
     * 根据id查询单个Task
     *
     * @param taskId
     * @return
     */
    @Query("SELECT * FROM Tasks where entryid = :taskId")
    Task getTaskById(String taskId);

    /**
     * 在数据库中插入一个任务。 如果任务已经存在，请将其替换。
     *
     * @param task
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    /**
     * 更新
     *
     * @param task
     * @return 更新数量，這儿应该是1，因为不是集合
     */
    @Update
    int updateTask(Task task);

    /**
     * 更新task的completed
     *
     * @param taskId
     * @param completed
     */
    @Query("UPDATE Tasks SET completed =:completed WHERE entryid= :taskId")
    void updateCompleted(String taskId, boolean completed);

    /**
     * 删除
     *
     * @param taskId
     * @return
     */
    @Query("Delete from Tasks Where entryid = :taskId")
    int deleteTaskById(String taskId);

    /**
     * 删除所有的
     */
    @Query("Delete from tasks")
    void deleteTasks();

    /**
     * 删除已完成的
     *
     * @return
     */
    @Query("Delete from Tasks Where completed = 1")
    int deleteCompletedTasks();
}
