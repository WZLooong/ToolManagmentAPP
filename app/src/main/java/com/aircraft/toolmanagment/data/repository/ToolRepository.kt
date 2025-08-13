package com.aircraft.toolmanagment.data.repository

import com.aircraft.toolmanagment.data.ToolDao
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.data.remote.api.NetworkExceptionHandler
import com.aircraft.toolmanagment.network.AddToolRequest
import com.aircraft.toolmanagment.network.ApiService

/**
 * 工具数据仓库，处理工具相关的本地和远程数据操作
 */
class ToolRepository(
    private val localDataSource: ToolDao,
    private val remoteDataSource: ApiService
) {
    /**
     * 通过API添加工具
     */
    suspend fun addToolViaApi(
        name: String,
        model: String?,
        specification: String?,
        quantity: Int,
        manufacturer: String?,
        purchaseDate: String?,
        storageLocation: String?,
        barcode: String?,
        status: String?
    ): NetworkResult<Boolean> {
        return try {
            val request = AddToolRequest(
                name, model, specification, quantity, manufacturer,
                purchaseDate, storageLocation, barcode, status
            )
            val response = remoteDataSource.addTool(request)
            
            if (response.success) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "添加工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 通过API查询工具
     */
    suspend fun queryToolsViaApi(keyword: String?): NetworkResult<List<Tool>> {
        return try {
            val response = remoteDataSource.queryTools(keyword)
            
            if (response.success) {
                NetworkResult.Success(response.data ?: emptyList())
            } else {
                NetworkResult.Error(null, response.message ?: "查询工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 通过API更新工具
     */
    suspend fun updateToolViaApi(tool: Tool): NetworkResult<Boolean> {
        return try {
            val response = remoteDataSource.updateTool(tool)
            
            if (response.success) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "更新工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 通过API删除工具
     */
    suspend fun deleteToolViaApi(id: Int): NetworkResult<Boolean> {
        return try {
            val response = remoteDataSource.deleteTool(id)
            
            if (response.success) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "删除工具失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 本地搜索工具
     */
    suspend fun searchToolLocally(keyword: String): List<Tool> {
        return localDataSource.searchTool(keyword)
    }

    /**
     * 本地添加工具
     */
    suspend fun addToolLocally(tool: Tool) {
        localDataSource.insertTool(tool)
    }

    /**
     * 本地更新工具
     */
    suspend fun updateToolLocally(tool: Tool) {
        localDataSource.updateTool(tool)
    }
}