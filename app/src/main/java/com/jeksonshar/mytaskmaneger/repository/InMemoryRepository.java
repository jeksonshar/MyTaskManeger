package com.jeksonshar.mytaskmaneger.repository;

import com.jeksonshar.mytaskmaneger.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryRepository extends BaseRepository {

    private final List<Task> mTaskList = new ArrayList<>();

    @Override
    public List<Task> getAllTasks() {
        return mTaskList;
    }

    @Override
    public List<Task> getUnsolvedTasks() {
        return mTaskList;
    }

    @Override
    public Task getTaskById(UUID uuid) {
        for (Task task: mTaskList) {
            if (task.getId().equals(uuid)) {
                return task;
            }
        }
        return null;
    }

    @Override
    public UUID addNewTask() {
        Task task = new Task();
        mTaskList.add(task);
        return task.getId();
    }

    @Override
    public void delete(Task task) {
        mTaskList.remove(task);
        notifyListeners();
    }

    @Override
    public void update(Task task) {
        notifyListeners();
    }
}
