package com.aircraft.toolmanagment

import android.content.Context
import androidx.room.Room
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.BorrowRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BorrowReturnManagement(private val context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "aircraft_tool_management"
    ).build()

    suspend fun borrowTool(borrowRecord: BorrowRecord) {
        withContext(Dispatchers.IO) {
            db.borrowRecordDao().insertBorrowRecord(borrowRecord)
        }
    }

    suspend fun returnTool(borrowRecordId: Long) {
        withContext(Dispatchers.IO) {
            db.borrowRecordDao().updateReturnStatus(borrowRecordId, true)
        }
    }

    suspend fun getBorrowRecords(toolId: Long? = null, userId: Long? = null): List<BorrowRecord> {
        return withContext(Dispatchers.IO) {
            if (toolId != null) {
                db.borrowRecordDao().getBorrowRecordsByToolId(toolId)
            } else if (userId != null) {
                db.borrowRecordDao().getBorrowRecordsByUserId(userId)
            } else {
                db.borrowRecordDao().getAllBorrowRecords()
            }
        }
    }
}