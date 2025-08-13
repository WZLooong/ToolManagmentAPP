package com.aircraft.toolmanagment.network

import android.util.Log
import com.aircraft.toolmanagment.data.entity.UserLoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.net.UnknownHostException

class ConnectionTest {
    private val TAG = "ConnectionTest"

    suspend fun testApiConnection(): ConnectionResult {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("http://39.106.150.70:8080/api/health")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                
                val responseCode = connection.responseCode
                Log.d(TAG, "Response Code: $responseCode")
                
                connection.disconnect()
                
                when (responseCode) {
                    200 -> ConnectionResult.Success
                    in 400..499 -> ConnectionResult.ClientError(responseCode)
                    in 500..599 -> ConnectionResult.ServerError(responseCode)
                    else -> ConnectionResult.UnknownError("Unexpected response code: $responseCode")
                }
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "连接超时", e)
                ConnectionResult.NetworkError("连接超时，请检查网络连接或服务器状态")
            } catch (e: UnknownHostException) {
                Log.e(TAG, "无法解析主机地址", e)
                ConnectionResult.NetworkError("无法连接到服务器，请检查网络连接")
            } catch (e: Exception) {
                Log.e(TAG, "连接测试失败", e)
                ConnectionResult.UnknownError("连接测试失败: ${e.message}")
            }
        }
    }
    
    sealed class ConnectionResult {
        object Success : ConnectionResult()
        data class NetworkError(val message: String) : ConnectionResult()
        data class ClientError(val code: Int) : ConnectionResult()
        data class ServerError(val code: Int) : ConnectionResult()
        data class UnknownError(val message: String) : ConnectionResult()
    }
}