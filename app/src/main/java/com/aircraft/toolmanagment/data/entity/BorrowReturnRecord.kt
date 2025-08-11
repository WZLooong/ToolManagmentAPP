package com.aircraft.toolmanagment.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borrow_return_records")
data class BorrowReturnRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val toolId: Int = 0,
    val borrowerId: Int = 0,
    val borrowTime: Long = 0,
    val expectedReturnTime: Long = 0,
    val actualReturnTime: Long? = null,
    val borrowReason: String = "",
    val approvalStatus: String = "",
    val rejectionReason: String = ""
)