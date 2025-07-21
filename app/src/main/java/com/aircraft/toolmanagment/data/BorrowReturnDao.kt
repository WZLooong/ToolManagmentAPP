package com.aircraft.toolmanagment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord

@Dao
interface BorrowReturnDao {
    @Insert
    suspend fun insertBorrowRecord(record: BorrowReturnRecord)

    @Update
    suspend fun updateBorrowRecord(record: BorrowReturnRecord)

    @Query("SELECT * FROM borrow_return_records WHERE toolId = :toolId")
    suspend fun getBorrowRecordsByToolId(toolId: Int): List<BorrowReturnRecord>

    @Query("SELECT * FROM borrow_return_records WHERE borrowerId = :userId")
    suspend fun getBorrowRecordsByUserId(userId: Int): List<BorrowReturnRecord>

    @Query("SELECT * FROM borrow_return_records")
    suspend fun getAllBorrowRecords(): List<BorrowReturnRecord>

    @Query("UPDATE borrow_return_records SET actualReturnTime = :returnTime WHERE id = :recordId")
    suspend fun updateReturnStatus(recordId: Int, returnTime: Long)
}