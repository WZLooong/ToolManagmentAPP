package com.aircraft.toolmanagment.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

class DatabaseConnectionTest {
    private val TAG = "DatabaseConnectionTest"

    suspend fun testDatabaseConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // 使用API端点测试数据库连接状态
                val url = URL("http://39.106.150.70:8080/api/health")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                
                val responseCode = connection.responseCode
                Log.d(TAG, "Database health check response code: $responseCode")
                
                connection.disconnect()
                
                // 如果API健康检查成功，说明后端服务和数据库连接正常
                responseCode == 200
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "连接超时", e)
                false
            } catch (e: UnknownHostException) {
                Log.e(TAG, "无法解析主机地址", e)
                false
            } catch (e: Exception) {
                Log.e(TAG, "数据库连接测试异常", e)
                false
            }
        }
    }
}