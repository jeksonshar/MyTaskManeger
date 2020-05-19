package com.jeksonshar.mytaskmaneger.repository.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
    entities = {TaskEntity.class},
    version = 1,
    exportSchema = false
)
public abstract class TasksDataBase extends RoomDatabase {
    public abstract TasksDao getTasksDao();
}
