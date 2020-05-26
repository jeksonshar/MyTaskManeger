package com.jeksonshar.mytaskmaneger.repository.room;

import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * FROM TaskEntity")
    List<TaskEntity> getAllTasks();

    @Query("SELECT * FROM TaskEntity WHERE id == :id")
    TaskEntity getById(String id);

    @Insert
    void addTask(TaskEntity entity);

    @Delete
    void deleteTask(TaskEntity entity);

    @Update
    void updateTask(TaskEntity entity);
}
