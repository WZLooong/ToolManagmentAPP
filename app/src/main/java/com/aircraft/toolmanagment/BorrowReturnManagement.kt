package com.aircraft.toolmanagment

import android.content.Context
import android.util.Log
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.ApiResponse
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.BorrowReturnDao
import com.aircraft.toolmanagment.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T>
object Loading : Result<Nothing>()
data class Success<out T>(val data: T) : Result<T>()
data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()

class BorrowReturnManagement(private val context: Context) {
    private val TAG = "BorrowReturnManagement"
    private val db = AppDatabase.getDatabase(context)

    // 本地借阅工具
    suspend fun borrowToolLocally(borrowRecord: BorrowReturnRecord): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                db.borrowReturnDao().insertBorrowRecord(borrowRecord)
                Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Error("Failed to borrow tool locally", e)
            }
        }
    }

    // 通过API借阅工具
    suspend fun borrowToolViaApi(borrowRecord: BorrowReturnRecord): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                // 假设API接口为borrowTool
                NetworkModule.apiService.borrowTool(borrowRecord)
            }

            if (response.success) {
                // 如果API成功，同时保存到本地数据库
                borrowToolLocally(borrowRecord)
                Log.d(TAG, "工具借阅成功")
                true
            } else {
                Log.e(TAG, "工具借阅失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API借阅工具失败: ${e.message}", e)
            false
        }
    }

    // 本地归还工具
    suspend fun returnToolLocally(borrowRecordId: Int): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                db.borrowReturnDao().updateReturnStatus(borrowRecordId, System.currentTimeMillis())
                Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Error("Failed to return tool locally", e)
            }
        }
    }

    // 通过API归还工具
    suspend fun returnToolViaApi(borrowRecordId: Int): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                // 假设API接口为returnTool
                NetworkModule.apiService.returnTool(borrowRecordId)
            }

            if (response.success) {
                // 如果API成功，同时更新本地数据库
                returnToolLocally(borrowRecordId)
                Log.d(TAG, "工具归还成功，记录ID: $borrowRecordId")
                true
            } else {
                Log.e(TAG, "工具归还失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API归还工具失败: ${e.message}", e)
            false
        }
    }

    // 获取本地借阅记录
    suspend fun getLocalBorrowRecords(toolId: Int? = null, userId: Int? = null): Result<List<BorrowReturnRecord>> {
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
                Error("Failed to get local borrow records", e)
            }
        }
    }

    // 通过API获取借阅记录
    suspend fun getBorrowRecordsViaApi(toolId: Int? = null, userId: Int? = null): List<BorrowReturnRecord> {
        return try {
            val response = withContext(Dispatchers.IO) {
                // 假设API接口为getBorrowRecords
                NetworkModule.apiService.getBorrowRecords(toolId, userId)
            }

            if (response.success) {
                Log.d(TAG, "借阅记录获取成功，共 ${response.data?.size ?: 0} 条")
                response.data ?: emptyList()
            } else {
                Log.e(TAG, "借阅记录获取失败: ${response.message ?: "未知错误"}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API获取借阅记录失败: ${e.message}", e)
            emptyList()
        }
    }
}
