package com.aircraft.toolmanagment.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.util.ViewState

class ToolManagementViewModel : BaseViewModel() {
    private val _toolsState = MutableLiveData<ViewState<List<Tool>>>()
    val toolsState: LiveData<ViewState<List<Tool>>> = _toolsState

    private val _addToolState = MutableLiveData<ViewState<Boolean>>()
    val addToolState: LiveData<ViewState<Boolean>> = _addToolState

    fun queryTools(context: Context, keyword: String? = null) {
        _toolsState.value = ViewState.Loading

        initRepository(context)

        launchMain(
            onError = { e ->
                _toolsState.value = ViewState.Error("获取工具列表异常: ${e.message}")
            }
        ) {
            // 先尝试从本地获取
            val localTools = repository.toolDao.searchTool(keyword ?: "")
            
            // 更新UI
            _toolsState.value = ViewState.Success(localTools)
            
            // 尝试从网络获取最新数据
            val networkResult = repository.toolRepo.queryToolsViaApi(keyword)
            when (networkResult) {
                is NetworkResult.Success<*> -> {
                    val tools = networkResult.data as List<Tool>
                    _toolsState.value = ViewState.Success(tools)
                }
                is NetworkResult.Error -> {
                    // 网络错误，但仍然显示本地数据
                    // _toolsState.value 保持本地数据不变
                }
                is NetworkResult.NetworkError -> {
                    // 网络错误，但仍然显示本地数据
                    // _toolsState.value 保持本地数据不变
                }
                else -> {
                    // 其他错误，但仍然显示本地数据
                    // _toolsState.value 保持本地数据不变
                }
            }
        }
    }

    fun addTool(
        context: Context,
        name: String,
        model: String?,
        specification: String?,
        quantity: Int,
        manufacturer: String?,
        purchaseDate: String?,
        storageLocation: String?,
        barcode: String?,
        status: String?
    ) {
        _addToolState.value = ViewState.Loading

        initRepository(context)

        launchMain(
            onError = { e ->
                _addToolState.value = ViewState.Error("添加工具异常: ${e.message}")
            }
        ) {
            val result = repository.toolRepo.addToolViaApi(
                name, model, specification, quantity, manufacturer,
                purchaseDate, storageLocation, barcode, status
            )
            when (result) {
                is NetworkResult.Success<*> -> {
                    _addToolState.value = ViewState.Success(result.data as Boolean)
                }
                is NetworkResult.Error -> {
                    _addToolState.value = ViewState.Error(result.message)
                }
                is NetworkResult.NetworkError -> {
                    _addToolState.value = ViewState.Error("网络连接错误，请检查网络设置")
                }
                else -> {
                    _addToolState.value = ViewState.Error("添加工具过程中发生未知错误")
                }
            }
        }
    }
}