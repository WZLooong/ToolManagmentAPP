package com.aircraft.toolmanagment.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.util.ViewState

class BorrowReturnViewModel : BaseViewModel() {
    private val _borrowRecordsState = MutableLiveData<ViewState<List<BorrowReturnRecord>>>()
    val borrowRecordsState: LiveData<ViewState<List<BorrowReturnRecord>>> = _borrowRecordsState

    private val _borrowToolState = MutableLiveData<ViewState<Boolean>>()
    val borrowToolState: LiveData<ViewState<Boolean>> = _borrowToolState

    private val _returnToolState = MutableLiveData<ViewState<Boolean>>()
    val returnToolState: LiveData<ViewState<Boolean>> = _returnToolState

    fun getBorrowRecords(context: Context, toolId: Int? = null, userId: Int? = null) {
        _borrowRecordsState.value = ViewState.Loading

        initRepository(context)

        launchMain(
            onError = { e ->
                _borrowRecordsState.value = ViewState.Error("获取借还记录异常: ${e.message}")
            }
        ) {
            // 先尝试从本地获取
            val localRecords = repository.borrowReturnRepo.getLocalBorrowRecords(toolId, userId)
            
            // 更新UI
            _borrowRecordsState.value = ViewState.Success(localRecords)
            
            // 尝试从网络获取最新数据
            val networkResult = repository.borrowReturnRepo.getBorrowRecordsViaApi(toolId, userId)
            when (networkResult) {
                is NetworkResult.Success<*> -> {
                    val records = networkResult.data as List<BorrowReturnRecord>
                    _borrowRecordsState.value = ViewState.Success(records)
                }
                is NetworkResult.Error -> {
                    // 网络错误，但仍然显示本地数据
                    // _borrowRecordsState.value 保持本地数据不变
                }
                is NetworkResult.NetworkError -> {
                    // 网络错误，但仍然显示本地数据
                    // _borrowRecordsState.value 保持本地数据不变
                }
                else -> {
                    // 其他错误，但仍然显示本地数据
                    // _borrowRecordsState.value 保持本地数据不变
                }
            }
        }
    }

    fun borrowTool(context: Context, borrowRecord: BorrowReturnRecord) {
        _borrowToolState.value = ViewState.Loading

        initRepository(context)

        launchMain(
            onError = { e ->
                _borrowToolState.value = ViewState.Error("借出工具异常: ${e.message}")
            }
        ) {
            val result = repository.borrowReturnRepo.borrowToolViaApi(borrowRecord)
            when (result) {
                is NetworkResult.Success<*> -> {
                    _borrowToolState.value = ViewState.Success(result.data as Boolean)
                }
                is NetworkResult.Error -> {
                    _borrowToolState.value = ViewState.Error(result.message)
                }
                is NetworkResult.NetworkError -> {
                    _borrowToolState.value = ViewState.Error("网络连接错误，请检查网络设置")
                }
                else -> {
                    _borrowToolState.value = ViewState.Error("借出工具过程中发生未知错误")
                }
            }
        }
    }

    fun returnTool(context: Context, toolId: Int) {
        _returnToolState.value = ViewState.Loading

        initRepository(context)

        launchMain(
            onError = { e ->
                _returnToolState.value = ViewState.Error("归还工具异常: ${e.message}")
            }
        ) {
            // 这里需要先找到未归还的记录
            val recordsResult = repository.borrowReturnRepo.getBorrowRecordsViaApi(toolId)
            when (recordsResult) {
                is NetworkResult.Success<*> -> {
                    val records = recordsResult.data as List<BorrowReturnRecord>
                    val unreturnedRecord = records.find { it.actualReturnTime == null }
                    
                    if (unreturnedRecord != null) {
                        // 执行归还操作
                        val result = repository.borrowReturnRepo.returnToolViaApi(unreturnedRecord.id)
                        when (result) {
                            is NetworkResult.Success<*> -> {
                                _returnToolState.value = ViewState.Success(result.data as Boolean)
                            }
                            is NetworkResult.Error -> {
                                _returnToolState.value = ViewState.Error(result.message)
                            }
                            is NetworkResult.NetworkError -> {
                                _returnToolState.value = ViewState.Error("网络连接错误，请检查网络设置")
                            }
                            else -> {
                                _returnToolState.value = ViewState.Error("归还工具过程中发生未知错误")
                            }
                        }
                    } else {
                        _returnToolState.value = ViewState.Error("未找到未归还的记录")
                    }
                }
                is NetworkResult.Error -> {
                    _returnToolState.value = ViewState.Error(recordsResult.message)
                }
                is NetworkResult.NetworkError -> {
                    _returnToolState.value = ViewState.Error("网络连接错误，请检查网络设置")
                }
                else -> {
                    _returnToolState.value = ViewState.Error("查询记录过程中发生未知错误")
                }
            }
        }
    }
}