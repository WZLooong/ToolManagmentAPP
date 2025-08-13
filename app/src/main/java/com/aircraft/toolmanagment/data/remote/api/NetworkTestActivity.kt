<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <TextView
        android:id="@+id/tv_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="网络连接测试"
        android:textSize="24sp"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/tv_server_address"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="服务器地址: 39.106.150.70:8080"
        android:textColor="#888888"
        android:layout_marginBottom="24dp"/>

    <Button
        android:id="@+id/btn_test_connection"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="测试连接"/>

    <TextView
        android:id="@+id/tv_test_result"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:padding="12dp"
        android:visibility="gone"/>

    <TextView
        android:id="@+id/tv_instructions"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="如果连接失败，请检查：\n1. 服务器是否正在运行\n2. 防火墙设置是否正确\n3. 网络连接是否正常\n4. 服务器地址和端口是否正确"
        android:textColor="#888888"/>
</LinearLayout>
package com.aircraft.toolmanagment.network

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkTestActivity : ComponentActivity() {
    private lateinit var btnTestConnection: Button
    private lateinit var tvTestResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.network_test)
        
        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        btnTestConnection = findViewById(R.id.btn_test_connection)
        tvTestResult = findViewById(R.id.tv_test_result)
    }

    private fun setupClickListeners() {
        btnTestConnection.setOnClickListener {
            testNetworkConnection()
        }
    }

    private fun testNetworkConnection() {
        // 更新UI状态
        btnTestConnection.isEnabled = false
        btnTestConnection.text = "测试中..."
        tvTestResult.visibility = TextView.GONE

        CoroutineScope(Dispatchers.Main).launch {
            val connectionTest = ConnectionTest()
            val result = connectionTest.testApiConnection()
            
            // 恢复按钮状态
            btnTestConnection.isEnabled = true
            btnTestConnection.text = "测试连接"
            
            when (result) {
                is ConnectionTest.ConnectionResult.Success -> {
                    showTestResult("连接成功！", true)
                    Toast.makeText(this@NetworkTestActivity, "连接成功！", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.NetworkError -> {
                    showTestResult("网络错误: ${result.message}", false)
                    Toast.makeText(this@NetworkTestActivity, "网络错误: ${result.message}", Toast.LENGTH_LONG).show()
                }
                is ConnectionTest.ConnectionResult.ClientError -> {
                    showTestResult("客户端错误: ${result.code}", false)
                    Toast.makeText(this@NetworkTestActivity, "客户端错误: ${result.code}", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.ServerError -> {
                    showTestResult("服务器错误: ${result.code}", false)
                    Toast.makeText(this@NetworkTestActivity, "服务器错误: ${result.code}", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.UnknownError -> {
                    showTestResult("未知错误: ${result.message}", false)
                    Toast.makeText(this@NetworkTestActivity, "未知错误: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun showTestResult(result: String, isSuccess: Boolean) {
        tvTestResult.text = result
        tvTestResult.setTextColor(if (isSuccess) -0x100 else -0xff0100)
        tvTestResult.setBackgroundColor(if (isSuccess) 0xff008000.toInt() else 0xffff0000.toInt())
        tvTestResult.visibility = TextView.VISIBLE
    }
}