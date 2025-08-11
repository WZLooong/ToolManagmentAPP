package com.aircraft.toolmanagment

import android.content.Context
import android.util.Log
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.ApiResponse
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.network.ApiService
import com.aircraft.toolmanagment.network.AddToolRequest
import com.aircraft.toolmanagment.network.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToolManagement(private val context: Context) {
    private val TAG = "ToolManagement"
    private val db = AppDatabase.getDatabase(context)

    suspend fun addTool(tool: Tool) {
        withContext(Dispatchers.IO) {
            db.toolDao().insertTool(tool)
        }
    }

    suspend fun searchTool(keyword: String): List<Tool> {
        return withContext(Dispatchers.IO) {
            db.toolDao().searchTool(keyword)
        }
    }

    suspend fun updateTool(tool: Tool) {
        withContext(Dispatchers.IO) {
            db.toolDao().updateTool(tool)
        }
    }

    // 通过 API 添加工具
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
    ): Boolean {
        return try {
            val request = AddToolRequest(
                name = name,
                model = model,
                specification = specification,
                quantity = quantity,
                manufacturer = manufacturer,
                purchaseDate = purchaseDate,
                storageLocation = storageLocation,
                barcode = barcode,
                status = status
            )
            
            val response = withContext(Dispatchers.IO) {
                NetworkModule.apiService.addTool(request)
            }
            
            if (response.success) {
                Log.d(TAG, "工具添加成功: $name")
                true
            } else {
                Log.e(TAG, "工具添加失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API添加工具失败: ${e.message}", e)
            false
        }
    }

    // 通过 API 查询工具
    suspend fun queryToolsViaApi(keyword: String?): List<Tool> {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkModule.apiService.queryTools(keyword)
            }
            
            if (response.success) {
                Log.d(TAG, "工具查询成功，找到 ${response.data?.size ?: 0} 个工具")
                response.data ?: emptyList()
            } else {
                Log.e(TAG, "工具查询失败: ${response.message ?: "未知错误"}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API查询工具失败: ${e.message}", e)
            emptyList()
        }
    }

    // 通过 API 更新工具
    suspend fun updateToolViaApi(tool: Tool): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkModule.apiService.updateTool(tool)
            }
            
            if (response.success) {
                Log.d(TAG, "工具更新成功: ${tool.name}")
                true
            } else {
                Log.e(TAG, "工具更新失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API更新工具失败: ${e.message}", e)
            false
        }
    }

    // 通过 API 删除工具
    suspend fun deleteToolViaApi(id: Int): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkModule.apiService.deleteTool(id)
            }
            
            if (response.success) {
                Log.d(TAG, "工具删除成功，ID: $id")
                true
            } else {
                Log.e(TAG, "工具删除失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API删除工具失败: ${e.message}", e)
            false
        }
    }

    // 通过 API 批量导入工具
    suspend fun batchImportToolsViaApi(tools: List<Tool>): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                NetworkModule.apiService.batchImportTools(tools)
            }
            
            if (response.success) {
                Log.d(TAG, "批量导入工具成功，共 ${tools.size} 个")
                true
            } else {
                Log.e(TAG, "批量导入工具失败: ${response.message ?: "未知错误"}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "通过API批量导入工具失败: ${e.message}", e)
            false
        }
    }
}