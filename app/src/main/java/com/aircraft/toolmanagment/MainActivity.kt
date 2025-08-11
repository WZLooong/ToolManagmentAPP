package com.aircraft.toolmanagment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import com.aircraft.toolmanagment.BorrowReturnManagement
import com.aircraft.toolmanagment.data.Result
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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

@Composable
fun MyApp() {
    MaterialTheme {
        Surface {
            // 后续可添加具体 UI 组件 
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApp()
}