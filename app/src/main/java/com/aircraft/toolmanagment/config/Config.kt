package com.aircraft.toolmanagment.config

object Config {
    // API基础URL配置

    // 生产环境地址
    const val BASE_URL = "http://39.106.150.70:8080/api/"

    // 开发环境地址（本地测试时可以使用）
    // Android模拟器访问本地主机使用 http://10.0.2.2:8080
    // 真机调试时可以使用本地局域网IP或 http://localhost:8080
    const val BASE_URL_DEV = "http://10.0.2.2:8080/api/"

    // 可以在这里添加其他环境的配置
}