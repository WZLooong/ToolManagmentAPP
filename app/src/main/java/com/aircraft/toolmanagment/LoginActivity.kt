package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.UserManagement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManagement = UserManagement(this)
        
        setContent {
            MaterialTheme {
                LoginScreen { identifier, password ->
                    login(identifier, password)
                }
            }
        }
    }

    private fun login(identifier: String, password: String) {
        if (identifier.isBlank() || password.isBlank()) {
            if (!isFinishing && !isDestroyed) {
                Toast.makeText(this, "请输入用户名/员工ID和密码", Toast.LENGTH_SHORT).show()
            }
            return
        }

        // 使用lifecycleScope来自动管理协程生命周期
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val user = userManagement.loginUser(identifier, password)
                // 检查Activity是否仍然处于活跃状态
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    runOnUiThread {
                        // 再次检查Activity是否仍然处于活跃状态
                        if (!isFinishing && !isDestroyed) {
                            if (user != null) {
                                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                                // Navigate to main activity
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // 检查Activity是否仍然处于活跃状态
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    runOnUiThread {
                        // 再次检查Activity是否仍然处于活跃状态
                        if (!isFinishing && !isDestroyed) {
                            val errorMessage = e.message ?: "未知错误"
                            Log.e("LoginActivity", "登录异常", e)
                            Toast.makeText(this@LoginActivity, "登录异常: $errorMessage", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(onLogin: (identifier: String, password: String) -> Unit) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var identifierError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo and title
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 60.dp, bottom = 16.dp)
            )

            Text(
                text = "工具管理系统",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Form area
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Username/Employee ID field
                OutlinedTextField(
                    value = identifier,
                    onValueChange = { 
                        identifier = it
                        identifierError = null // Clear error when user types
                    },
                    label = { Text("用户名或员工ID") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = identifierError != null,
                    supportingText = identifierError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        passwordError = null // Clear error when user types
                    },
                    label = { Text("密码") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordError != null,
                    supportingText = passwordError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Login button
                Button(
                    onClick = {
                        // Reset errors
                        identifierError = null
                        passwordError = null

                        var isValid = true

                        // Validate identifier
                        if (identifier.isBlank()) {
                            identifierError = "请输入用户名或员工ID"
                            isValid = false
                        }

                        // Validate password
                        if (password.isBlank()) {
                            passwordError = "请输入密码"
                            isValid = false
                        }

                        if (isValid) {
                            onLogin(identifier, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "登录", fontSize = 16.sp)
                }
                
                // Register navigation button
                TextButton(
                    onClick = {
                        val intent = Intent(context, RegisterActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "没有账户？立即注册",
                        color = Color(0xFF2196F3),
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}