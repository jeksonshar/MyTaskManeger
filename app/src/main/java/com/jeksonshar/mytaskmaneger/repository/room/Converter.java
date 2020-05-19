package com.jeksonshar.mytaskmaneger.repository.room;

import com.jeksonshar.mytaskmaneger.model.Task;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Converter {

    static TaskEntity convert(Task task) {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.id = task.getId().toString();
        taskEntity.title = task.getTitle();
        taskEntity.detail = task.getDetail();
        taskEntity.dateAndTime = task.getDateAndTime().getTimeInMillis();
        taskEntity.solved = task.getSolved();

        return taskEntity;
    }

    static Task convert(TaskEntity taskEntity) {
        Task task = null;

        if (taskEntity != null) {
            task = new Task();

            task.setId(UUID.fromString(taskEntity.id));
            task.setTitle(taskEntity.title);
            task.setDetail(taskEntity.detail);

            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis(taskEntity.dateAndTime);
            task.setDateAndTime(cal);

            task.setSolved(taskEntity.solved);
        }
        return task;
    }
}
