
package com.aircraft.toolmanagment.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "borrow_return_records")
public class BorrowReturnRecord {
    // Room 需要无参构造函数
    public BorrowReturnRecord() {}
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int toolId;
    public int borrowerId;
    public long borrowTime;
    public long expectedReturnTime;
    public Long actualReturnTime;
    public String borrowReason;
    public String approvalStatus;
    public String rejectionReason;
}