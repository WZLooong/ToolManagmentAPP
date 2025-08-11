package com.aircraft.toolmanagment.data.entity

// 登录响应只包含用户数据，状态和消息由ApiResponse处理
data class UserLoginResponse(
    val user: User? = null
)