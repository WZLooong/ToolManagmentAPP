package com.aircraft.toolmanagment.data

/**
 * 通用结果封装类
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Exception? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * 扩展函数，用于简化Result的使用
 */
inline fun <T> Result<T>.fold(
    onSuccess: (T) -> Unit,
    onError: (String, Exception?) -> Unit,
    onLoading: () -> Unit
): Unit {
    when (this) {
        is Result.Success -> onSuccess(this.data)
        is Result.Error -> onError(this.message, this.exception)
        is Result.Loading -> onLoading()
    }
}