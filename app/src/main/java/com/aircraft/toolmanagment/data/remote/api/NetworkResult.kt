package com.aircraft.toolmanagment.data.remote.api

/**
 * 网络请求结果封装类
 */
sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int?, val message: String) : NetworkResult<Nothing>()
    object NetworkError : NetworkResult<Nothing>()
}