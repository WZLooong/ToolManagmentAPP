package com.aircraft.toolmanagment.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aircraft.toolmanagment.data.entity.User
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.util.ViewState

/**
 * 用户ViewModel，处理用户相关的业务逻辑
 */
class UserViewModel : BaseViewModel() {
    private val _loginState = MutableLiveData<ViewState<User?>>()
    val loginState: LiveData<ViewState<User?>> = _loginState

    private val _registerState = MutableLiveData<ViewState<Boolean>>()
    val registerState: LiveData<ViewState<Boolean>> = _registerState

    /**
     * 用户登录
     */
    fun login(context: Context, identifier: String, password: String) {
        // 检查输入有效性
        if (identifier.isBlank() || password.isBlank()) {
            _loginState.value = ViewState.Error("请输入用户名/员工ID和密码")
            return
        }

        _loginState.value = ViewState.Loading
        
        initRepository(context)
        
        launchMain(
            onError = { e ->
                android.util.Log.e("LoginViewModel", "登录异常", e)
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "无法连接到服务器，请检查网络连接和服务器地址"
                    is java.net.SocketTimeoutException -> "连接服务器超时，请检查网络连接"
                    is retrofit2.HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        when (e.code()) {
                            401 -> "认证失败，请检查用户名和密码"
                            in 400..499 -> "客户端错误: ${e.code()} ${e.message()}"
                            in 500..599 -> "服务器错误: ${e.code()} ${e.message()}"
                            else -> "HTTP错误: ${e.code()} ${e.message()} Response: $errorBody"
                        }
                    }
                    else -> "登录异常: ${e.message}"
                }
                _loginState.value = ViewState.Error(errorMessage)
            }
        ) {
            val result = repository.userRepo.login(identifier, password)
            android.util.Log.d("LoginViewModel", "登录结果: $result")
            when (result) {
                is NetworkResult.Success<*> -> {
                    android.util.Log.i("LoginViewModel", "登录成功: ${result.data}")
                    _loginState.value = ViewState.Success(result.data as User?)
                }
                is NetworkResult.Error -> {
                    val message = result.message
                    android.util.Log.w("LoginViewModel", "登录失败: $message")
                    if (message?.contains("不存在") == true || message?.contains("not found") == true) {
                        _loginState.value = ViewState.Success(null)
                    } else {
                        // Provide more detailed error message for 401 errors
                        val errorMessage = if (message.isNullOrBlank()) {
                            "认证失败，请检查用户名和密码"
                        } else if (message.contains("密码错误")) {
                            // 特别处理密码错误的情况
                            "密码错误，请检查密码是否正确"
                        } else {
                            message
                        }
                        _loginState.value = ViewState.Error(errorMessage)
                    }
                }
                is NetworkResult.NetworkError -> {
                    android.util.Log.e("LoginViewModel", "网络连接错误")
                    _loginState.value = ViewState.Error("网络连接错误，请检查网络设置")
                }
                else -> {
                    android.util.Log.e("LoginViewModel", "未知错误")
                    _loginState.value = ViewState.Error("登录过程中发生未知错误")
                }
            }
        }
    }

    /**
     * 用户注册
     */
    fun register(
        context: Context,
        phone: String?,
        email: String?,
        password: String,
        name: String,
        employeeId: String,
        team: String,
        role: String
    ) {
        // 检查密码一致性等验证逻辑应该在UI层处理
        
        _registerState.value = ViewState.Loading
        
        initRepository(context)
        
        launchMain(
            onError = { e ->
                _registerState.value = ViewState.Error("注册异常: ${e.message}")
            }
        ) {
            // 首先检查用户是否存在
            val checkResult = repository.userRepo.checkUserExists(name)
            when (checkResult) {
                is NetworkResult.Success<*> -> {
                    if (checkResult.data == true) {
                        // 用户已存在
                        _registerState.value = ViewState.Error("用户已存在，请直接登录")
                    } else {
                        // 用户不存在，可以注册
                        val registerResult = repository.userRepo.register(
                            phone, email, password, name, employeeId, team, role
                        )
                        when (registerResult) {
                            is NetworkResult.Success<*> -> {
                                _registerState.value = ViewState.Success(registerResult.data as Boolean)
                            }
                            is NetworkResult.Error -> {
                                _registerState.value = ViewState.Error(registerResult.message)
                            }
                            is NetworkResult.NetworkError -> {
                                _registerState.value = ViewState.Error("网络连接错误，请检查网络设置")
                            }
                            else -> {
                                _registerState.value = ViewState.Error("注册过程中发生未知错误")
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _registerState.value = ViewState.Error("检查用户失败: ${checkResult.message}")
                }
                is NetworkResult.NetworkError -> {
                    _registerState.value = ViewState.Error("网络连接错误，请检查网络设置")
                }
                else -> {
                    _registerState.value = ViewState.Error("检查用户过程中发生未知错误")
                }
            }
        }
    }
}