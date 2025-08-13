package com.aircraft.toolmanagment.data.remote.api

import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 网络异常处理工具类
 */
object NetworkExceptionHandler {
    fun handle(throwable: Throwable): NetworkResult.Error {
        return when (throwable) {
            is HttpException -> {
                NetworkResult.Error(throwable.code(), throwable.message())
            }
            is SocketTimeoutException -> {
                NetworkResult.Error(null, "连接超时")
            }
            is UnknownHostException -> {
                NetworkResult.Error(null, "网络连接错误")
            }
            else -> {
                NetworkResult.Error(null, "未知错误: ${throwable.message}")
            }
        }
    }
}