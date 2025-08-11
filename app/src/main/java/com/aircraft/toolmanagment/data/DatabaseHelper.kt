package com.aircraft.toolmanagment.data

import android.util.Log

object DatabaseHelper {
    private const val TAG = "DatabaseHelper"

    /**
     * 关闭数据库资源
     */
    fun closeResources(vararg resources: AutoCloseable?) {
        resources.forEach { resource ->
            try {
                resource?.close()
            } catch (e: Exception) {
                Log.e(TAG, "关闭资源失败", e)
            }
        }
    }
}