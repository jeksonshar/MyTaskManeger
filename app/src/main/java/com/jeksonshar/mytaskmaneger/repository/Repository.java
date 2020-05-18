package com.jeksonshar.mytaskmaneger.repository;

import com.jeksonshar.mytaskmaneger.model.Task;

import java.util.List;
import java.util.UUID;

public interface Repository {

    List<Task> getAllTasks();

    Task getTaskById(UUID uuid);

    UUID addNewTask();

    void delete(Task task);

    void update(Task task);

    void addListener(Listener listener);

    void removeListener(Listener listener);

    interface Listener {
        void onDataChanged();
    }
}
