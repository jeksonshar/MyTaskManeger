package com.jeksonshar.mytaskmaneger.repository.room;

import android.content.Context;

import androidx.room.Room;

import com.jeksonshar.mytaskmaneger.model.Task;
import com.jeksonshar.mytaskmaneger.repository.BaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomRepository extends BaseRepository {

    private TasksDao mTasksDao;

    public RoomRepository(Context context) {
        mTasksDao = Room
                .databaseBuilder(context, TasksDataBase.class, "task-database2.sqLite")
                .allowMainThreadQueries()
                .build()
                .getTasksDao();
    }

    @Override
    public List<Task> getAllTasks() {
        List<TaskEntity> taskEntities = mTasksDao.getAllTasks();
        List<Task> resultList = new ArrayList<>();

        for (TaskEntity taskEntity: taskEntities) {
            resultList.add(Converter.convert(taskEntity));
        }
        return resultList;
    }

    @Override
    public List<Task> getUnsolvedTasks() {
        List<TaskEntity> taskEntities = mTasksDao.getAllTasks();
        List<Task> resultList = new ArrayList<>();

        for (TaskEntity taskEntity: taskEntities) {
            if (!Converter.convert(taskEntity).getSolved()) {
                resultList.add(Converter.convert(taskEntity));
            }
        }
        return resultList;
    }

    @Override
    public Task getTaskById(UUID uuid) {
        TaskEntity taskEntity = mTasksDao.getById(uuid.toString());
        return Converter.convert(taskEntity);
    }

    @Override
    public UUID addNewTask() {
        Task task = new Task();
        mTasksDao.addTask(Converter.convert(task));
        return task.getId();
    }

    @Override
    public void delete(Task task) {
        mTasksDao.deleteTask(Converter.convert(task));
        notifyListeners();
    }

    @Override
    public void update(Task task) {
        mTasksDao.updateTask(Converter.convert(task));
        notifyListeners();
    }
}
