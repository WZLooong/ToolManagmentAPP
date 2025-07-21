package com.aircraft.toolmanagment.data.entity

import androidx.room.*

@Entity(tableName = "borrow_return_records")
data class BorrowReturnRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val toolId: Int,
    val borrowerId: Int,
    val borrowTime: Long,
    val expectedReturnTime: Long,
    var actualReturnTime: Long? = null,
    val borrowReason: String,
    val approvalStatus: String,
    val rejectionReason: String? = null
)