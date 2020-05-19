package com.jeksonshar.mytaskmaneger.repository.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaskEntity {

    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String detail;
    public long dateAndTime;
    public boolean solved;
}
