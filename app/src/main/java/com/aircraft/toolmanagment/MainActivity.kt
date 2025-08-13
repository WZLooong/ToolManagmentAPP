package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    // MainActivity不再直接管理用户登录注册
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启动时直接跳转到登录界面
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 简化的MainActivity，仅作为应用入口

    // 空的Composable函数，因为MainActivity仅作为入口
    @Composable
    fun MainScreen() {
        // 这里可以留空，因为MainActivity会立即跳转到LoginActivity
        Text("应用加载中...")
    }
}