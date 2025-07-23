package com.aircraft.toolmanagment

import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import androidx.compose.foundation.content.MediaType.Companion.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TextField
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManagement = UserManagement(this)

        testBorrowOperations()

        setContent {
            LoginRegisterScreen()
        }
    }

    private fun testBorrowOperations() {
        val borrowManager = BorrowReturnManagement(this)
        
        // Create a test record
        val testRecord = BorrowReturnRecord(
            id = null,
            toolId = 1,
            borrowerId = 1001,
            borrowTime = System.currentTimeMillis(),
            expectedReturnTime = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 7 days later
            actualReturnTime = null,
            borrowReason = "Routine maintenance",
            approvalStatus = "Approved",
            rejectionReason = null
        )

        // Insert the record
        CoroutineScope(Dispatchers.Main).launch {
            when (val result = borrowManager.borrowTool(testRecord)) {
                is Success -> println("Successfully inserted record")
                is Error -> println("Error inserting record: ${result.message}")
                is Loading -> println("Loading...")
                else -> {}
            }
            
            // Retrieve and display records
            val recordsResult = borrowManager.getBorrowRecords(toolId = 1)
            when (recordsResult) {
                is Success -> println("Successfully retrieved ${recordsResult.data.size} records")
                is Error -> println("Error retrieving records: ${recordsResult.message}")
                is Loading -> println("Loading...")
                else -> {}
            }
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun LoginRegisterScreen() {
        var isLoginScreen by mutableStateOf(true)

        if (isLoginScreen) {
            LoginScreen(
                onLogin = { username, password ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = userManagement.login(username, password)
                        runOnUiThread {
                            if (user != null) {
                                Toast.makeText(this@MainActivity, "登录成功", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onNavigateToRegister = { isLoginScreen = false }
            )
        } else {
            RegisterScreen(
                onRegister = { username, password ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val success = userManagement.register(username, password)
                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this@MainActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                isLoginScreen = true
                            } else {
                                Toast.makeText(this@MainActivity, "注册失败，用户名可能已存在", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                onNavigateToLogin = { isLoginScreen = true }
            )
        }
    }

    @Composable
    fun LoginScreen(onLogin: (String, String) -> Unit, onNavigateToRegister: () -> Unit) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("用户名") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("密码") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = { onLogin(username, password) }, modifier = Modifier.fillMaxWidth()) {
                Text("登录")
            }
            Button(onClick = { onNavigateToRegister() }, modifier = Modifier.fillMaxWidth()) {
                Text("去注册")
            }
        }
    }


    @Composable
    fun RegisterScreen(onRegister: (String, String) -> Unit, onNavigateToLogin: () -> Unit) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("用户名") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("密码") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("确认密码") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.size(16.dp))
            Button(onClick = {
                if (password == confirmPassword) {
                    onRegister(username, password)
                } else {
                    Toast.makeText(LocalContext.current, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("注册")
            }
            Button(onClick = { onNavigateToLogin() }, modifier = Modifier.fillMaxWidth()) {
                Text("去登录")
            }
        }
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