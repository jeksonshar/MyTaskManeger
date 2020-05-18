package com.jeksonshar.mytaskmaneger.model;

import android.content.res.Resources;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.UUID;

public class Task {

    private UUID id;
    private Calendar dateAndTime;
    private String title;
    private String detail;
    private boolean solved;

    public Task() {
        this.id  = UUID.randomUUID();
        this.dateAndTime = Calendar.getInstance();
        this.dateAndTime.add(Calendar.HOUR_OF_DAY, 3);
    }

    public UUID getId() {
        return id;
    }

    public Calendar getDateAndTime() {
        return dateAndTime;
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
}