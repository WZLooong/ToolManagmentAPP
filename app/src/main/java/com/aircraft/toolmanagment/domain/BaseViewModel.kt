package com.aircraft.toolmanagment.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircraft.toolmanagment.data.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel基类，提供通用功能
 */
abstract class BaseViewModel : ViewModel() {
    protected lateinit var repository: Repository
    
    /**
     * 初始化Repository
     */
    protected fun initRepository(context: Context) {
        // 这里应该通过依赖注入获取Repository实例
        // 但为了简化，我们直接创建实例
        // 在实际项目中，建议使用Hilt进行依赖注入
        repository = Repository.getInstance(context, com.aircraft.toolmanagment.network.NetworkModule.apiService)
    }
    
    /**
     * 在主线程中启动协程
     */
    protected fun launchMain(
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    
    /**
     * 在IO线程中启动协程
     */
    protected fun launchIO(
        onError: (Throwable) -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}