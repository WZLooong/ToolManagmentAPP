package com.aircraft.toolmanagment.network

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetworkTestScreen { testNetworkConnection() }
        }
    }

    private fun testNetworkConnection() {
        CoroutineScope(Dispatchers.Main).launch {
            val connectionTest = ConnectionTest()
            val result = connectionTest.testApiConnection()
            
            when (result) {
                is ConnectionTest.ConnectionResult.Success -> {
                    Toast.makeText(this@NetworkTestActivity, "连接成功！", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.NetworkError -> {
                    Toast.makeText(this@NetworkTestActivity, "网络错误: ${result.message}", Toast.LENGTH_LONG).show()
                }
                is ConnectionTest.ConnectionResult.ClientError -> {
                    Toast.makeText(this@NetworkTestActivity, "客户端错误: ${result.code}", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.ServerError -> {
                    Toast.makeText(this@NetworkTestActivity, "服务器错误: ${result.code}", Toast.LENGTH_SHORT).show()
                }
                is ConnectionTest.ConnectionResult.UnknownError -> {
                    Toast.makeText(this@NetworkTestActivity, "未知错误: ${result.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Composable
fun NetworkTestScreen(onTestConnection: () -> Unit) {
    var testResult by remember { mutableStateOf<String?>(null) }
    var isTesting by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "网络连接测试",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "服务器地址: 39.106.150.70:8080",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            
            Button(
                onClick = {
                    isTesting = true
                    testResult = null
                    onTestConnection()
                },
                enabled = !isTesting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isTesting) {
                    Text("测试中...")
                } else {
                    Text("测试连接")
                }
            }
            
            testResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (result.contains("成功")) Color.Green else Color.Red
                    )
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "如果连接失败，请检查：\n" +
                        "1. 服务器是否正在运行\n" +
                        "2. 防火墙设置是否正确\n" +
                        "3. 网络连接是否正常\n" +
                        "4. 服务器地址和端口是否正确",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}