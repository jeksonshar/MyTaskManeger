package com.jeksonshar.mytaskmaneger.model;

import java.util.Calendar;
import java.util.Comparator;
import java.util.UUID;

public class Task {

    private UUID id;
    private Calendar dateAndTime;
    private Calendar dateCreated;
    private String title;
    private String detail;
    private boolean solved;
    private String priority;

    public Task() {
        this.id  = UUID.randomUUID();
        this.dateCreated = Calendar.getInstance();
        this.dateAndTime = Calendar.getInstance();
        this.dateAndTime.add(Calendar.HOUR_OF_DAY, 3);
        this.priority = String.valueOf(TaskPriorityValue.GREEN);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Calendar getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Calendar calendar) {
        this.dateAndTime = calendar;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean getSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public static final Comparator<Task> COMPARE_BY_DEADLINE = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getDateAndTime().compareTo(o2.getDateAndTime());
        }
    };

    public static final Comparator<Task> COMPARE_BY_CREATED_DATE = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.getDateCreated().compareTo(o2.getDateCreated());
        }
    };
}
