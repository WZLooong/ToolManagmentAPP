package com.aircraft.toolmanagment.data.repository

import com.aircraft.toolmanagment.data.BorrowReturnDao
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.data.remote.api.NetworkExceptionHandler
import com.aircraft.toolmanagment.network.ApiService

/**
 * 借还记录数据仓库，处理借还记录相关的本地和远程数据操作
 */
class BorrowReturnRepository(
    private val localDataSource: BorrowReturnDao,
    private val remoteDataSource: ApiService
) {
    /**
     * 通过API借阅工具
     */
    suspend fun borrowToolViaApi(borrowRecord: BorrowReturnRecord): NetworkResult<Boolean> {
        return try {
            val response = remoteDataSource.borrowTool(borrowRecord)
            
            if (response.success) {
                // 如果API成功，同时保存到本地数据库
                localDataSource.insertBorrowRecord(borrowRecord)
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "借阅工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 通过API归还工具
     */
    suspend fun returnToolViaApi(borrowRecordId: Int): NetworkResult<Boolean> {
        return try {
            val response = remoteDataSource.returnTool(borrowRecordId)
            
            if (response.success) {
                // 如果API成功，同时更新本地数据库
                localDataSource.updateReturnStatus(borrowRecordId, System.currentTimeMillis())
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "归还工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 通过API获取借阅记录
     */
    suspend fun getBorrowRecordsViaApi(toolId: Int? = null, userId: Int? = null): NetworkResult<List<BorrowReturnRecord>> {
        return try {
            val response = remoteDataSource.getBorrowRecords(toolId, userId)
            
            if (response.success) {
                NetworkResult.Success(response.data ?: emptyList())
            } else {
                NetworkResult.Error(null, response.message ?: "获取借阅记录失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 本地获取借阅记录
     */
    suspend fun getLocalBorrowRecords(toolId: Int? = null, userId: Int? = null): List<BorrowReturnRecord> {
        return when {
            toolId != null -> localDataSource.getBorrowRecordsByToolId(toolId)
            userId != null -> localDataSource.getBorrowRecordsByUserId(userId)
            else -> localDataSource.getAllBorrowRecords()
        }
    }

    /**
     * 本地借阅工具
     */
    suspend fun borrowToolLocally(borrowRecord: BorrowReturnRecord) {
        localDataSource.insertBorrowRecord(borrowRecord)
    }

    /**
     * 本地归还工具
     */
    suspend fun returnToolLocally(borrowRecordId: Int) {
        localDataSource.updateReturnStatus(borrowRecordId, System.currentTimeMillis())
    }
}