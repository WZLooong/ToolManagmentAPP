package com.aircraft.toolmanagment.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager

class DatabaseConnectionTest {
    private val TAG = "DatabaseConnectionTest"

    suspend fun testDatabaseConnection(): Boolean {
        return withContext(Dispatchers.IO) {
            var connection: Connection? = null
            try {
                // JDBC连接URL
                val DB_HOST = "39.106.150.70"
                val DB_PORT = 3306
                val DB_NAME = "aircraft_tool_management"
                val DB_USER = "aircraft_tool_management"
                val DB_PASSWORD = "88888888"
                
                val jdbcUrl = "jdbc:mysql://$DB_HOST:$DB_PORT/$DB_NAME?user=$DB_USER&password=$DB_PASSWORD&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC"
                
                Log.d(TAG, "尝试连接数据库: $jdbcUrl")
                
                // 加载MySQL JDBC驱动
                Class.forName("com.mysql.cj.jdbc.Driver")
                
                // 建立连接
                connection = DriverManager.getConnection(jdbcUrl)
                
                if (connection != null && !connection.isClosed) {
                    Log.d(TAG, "数据库连接成功")
                    return@withContext true
                } else {
                    Log.e(TAG, "数据库连接失败")
                    return@withContext false
                }
            } catch (e: Exception) {
                Log.e(TAG, "数据库连接异常", e)
                return@withContext false
            } finally {
                try {
                    connection?.close()
                } catch (e: Exception) {
                    Log.e(TAG, "关闭数据库连接时出错", e)
                }
            }
        }
    }
}