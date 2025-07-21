package com.aircraft.toolmanagment

import android.content.Context
import androidx.room.Room
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.BorrowReturnDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T>
object Loading : Result<Nothing>()
data class Success<out T>(val data: T) : Result<T>()
data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()

class BorrowReturnManagement(private val context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "aircraft_tool_management"
    ).build()

    suspend fun borrowTool(borrowRecord: BorrowReturnRecord): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                db.borrowReturnDao().insertBorrowRecord(borrowRecord)
                Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Error("Failed to borrow tool", e)
            }
        }
    }

    suspend fun returnTool(borrowRecordId: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                db.borrowReturnDao().updateReturnStatus(borrowRecordId, System.currentTimeMillis())
                Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Error("Failed to return tool", e)
            }
        }
    }

    suspend fun getBorrowRecords(toolId: Int? = null, userId: Int? = null): Result<List<BorrowReturnRecord>> {
        return withContext(Dispatchers.IO) {
            try {
                val records = if (toolId != null) {
                    db.borrowReturnDao().getBorrowRecordsByToolId(toolId)
                } else if (userId != null) {
                    db.borrowReturnDao().getBorrowRecordsByUserId(userId)
                } else {
                    db.borrowReturnDao().getAllBorrowRecords()
                }
                Success(records)
            } catch (e: Exception) {
                e.printStackTrace()
                Error("Failed to get borrow records", e)
            }
        }
    }
}
