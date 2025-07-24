package com.aircraft.toolmanagment.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int toolId;
    public String reminderType;
    public String message;
    public long sentTime;

    public Reminder() {}
}