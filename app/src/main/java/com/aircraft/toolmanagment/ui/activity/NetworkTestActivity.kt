package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.network.ConnectionTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkTestActivity : BaseActivity() {
    private lateinit var btnTestConnection: Button
    private lateinit var tvTestResult: TextView

    override fun getLayoutRes(): Int = R.layout.network_test

    override fun initViews() {
        btnTestConnection = findViewById(R.id.btn_test_connection)
        tvTestResult = findViewById(R.id.tv_test_result)
    }

    override fun initData() {
        setupClickListeners()
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
                    showToast("连接成功！")
                }
                is ConnectionTest.ConnectionResult.NetworkError -> {
                    showTestResult("网络错误: ${result.message}", false)
                    showToast("网络错误: ${result.message}", android.widget.Toast.LENGTH_LONG)
                }
                is ConnectionTest.ConnectionResult.ClientError -> {
                    showTestResult("客户端错误: ${result.code}", false)
                    showToast("客户端错误: ${result.code}")
                }
                is ConnectionTest.ConnectionResult.ServerError -> {
                    showTestResult("服务器错误: ${result.code}", false)
                    showToast("服务器错误: ${result.code}")
                }
                is ConnectionTest.ConnectionResult.UnknownError -> {
                    showTestResult("未知错误: ${result.message}", false)
                    showToast("未知错误: ${result.message}", android.widget.Toast.LENGTH_LONG)
                }
            }
        }
    }
    
    private fun showTestResult(result: String, isSuccess: Boolean) {
        tvTestResult.text = result
        tvTestResult.setTextColor(
            ContextCompat.getColor(this, if (isSuccess) android.R.color.white else android.R.color.white)
        )
        tvTestResult.setBackgroundColor(
            ContextCompat.getColor(this, if (isSuccess) R.color.success_color else R.color.error_color)
        )
        tvTestResult.visibility = TextView.VISIBLE
    }
}