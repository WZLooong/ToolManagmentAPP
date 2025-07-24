package com.aircraft.toolmanagment.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tool_transfers")
public class ToolTransfer {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int toolId;
    public int oldBorrowerId;
    public int newBorrowerId;
    public long transferTime;
    public String transferReason;
    public String location;

    public ToolTransfer() {}
}