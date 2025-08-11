package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.UserManagement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManagement = UserManagement(this)
        setContent {
            MaterialTheme {
                RegisterScreen { phone, email, password, name, employeeId, team, role ->
                    registerUser(phone, email, password, name, employeeId, team, role)
                }
            }
        }
    }
    
    private fun registerUser(phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String) {
        // 检查Activity是否仍然处于活跃状态
        if (!isFinishing && !isDestroyed && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // 使用lifecycleScope来自动管理协程生命周期
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Check if user already exists
                    val userExists = userManagement.checkUserExists(name) || userManagement.checkUserExists(employeeId)
                    // 检查Activity是否仍然处于活跃状态
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        if (userExists) {
                            runOnUiThread {
                                // 再次检查Activity是否仍然处于活跃状态
                                if (!isFinishing && !isDestroyed) {
                                    Toast.makeText(this@RegisterActivity, "用户已存在，请直接登录", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // 在协程中执行注册操作
                            val isRegistered = userManagement.registerUser(phone, email, password, name, employeeId, team, role)
                            // 检查Activity是否仍然处于活跃状态
                            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                                runOnUiThread {
                                    // 再次检查Activity是否仍然处于活跃状态
                                    if (!isFinishing && !isDestroyed) {
                                        if (isRegistered) {
                                            Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                            // Navigate to login after successful registration
                                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this@RegisterActivity, "注册失败，请重试", Toast.LENGTH_SHORT).show()
                                        }
                                    }
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
                                Toast.makeText(this@RegisterActivity, "注册异常: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterScreen(onRegister: (phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String) -> Unit) {
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var team by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var employeeIdError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // 使用应用主题背景色
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
                    .padding(top = 20.dp, bottom = 16.dp)
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
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        nameError = null // Clear error when user types
                    },
                    label = { Text("姓名") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { 
                        phone = it
                        phoneError = null // Clear error when user types
                    },
                    label = { Text("手机号") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = phoneError != null,
                    supportingText = phoneError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        emailError = null // Clear error when user types
                    },
                    label = { Text("邮箱") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = emailError != null,
                    supportingText = emailError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Password
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

                // Confirm password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        confirmPasswordError = null // Clear error when user types
                    },
                    label = { Text("确认密码") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = confirmPasswordError != null,
                    supportingText = confirmPasswordError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Employee ID
                OutlinedTextField(
                    value = employeeId,
                    onValueChange = { 
                        employeeId = it
                        employeeIdError = null // Clear error when user types
                    },
                    label = { Text("员工ID") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = employeeIdError != null,
                    supportingText = employeeIdError?.let { { Text(it) } },
                    shape = RoundedCornerShape(8.dp)
                )

                // Team
                OutlinedTextField(
                    value = team,
                    onValueChange = { team = it },
                    label = { Text("团队") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Role
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("角色") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                // Register button
                Button(
                    onClick = {
                        // Reset errors
                        phoneError = null
                        emailError = null
                        passwordError = null
                        confirmPasswordError = null
                        nameError = null
                        employeeIdError = null

                        var isValid = true

                        // Validate name
                        if (name.isBlank()) {
                            nameError = "请输入姓名"
                            isValid = false
                        }

                        // Validate phone
                        if (phone.isNotBlank() && !android.util.Patterns.PHONE.matcher(phone).matches()) {
                            phoneError = "请输入有效的手机号"
                            isValid = false
                        }

                        // Validate email
                        if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            emailError = "请输入有效的邮箱"
                            isValid = false
                        }

                        // Validate password
                        if (password.isBlank()) {
                            passwordError = "请输入密码"
                            isValid = false
                        } else if (password.length < 6) {
                            passwordError = "密码长度不能少于6位"
                            isValid = false
                        }

                        // Validate confirm password
                        if (confirmPassword.isBlank()) {
                            confirmPasswordError = "请确认密码"
                            isValid = false
                        } else if (confirmPassword != password) {
                            confirmPasswordError = "两次输入的密码不一致"
                            isValid = false
                        }

                        // Validate employee ID
                        if (employeeId.isBlank()) {
                            employeeIdError = "请输入员工ID"
                            isValid = false
                        }

                        if (isValid) {
                            onRegister(
                                phone.takeIf { it.isNotBlank() },
                                email.takeIf { it.isNotBlank() },
                                password,
                                name,
                                employeeId,
                                team,
                                role
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "注册", fontSize = 16.sp)
                }
                
                // Login navigation button
                TextButton(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "已有账户？立即登录",
                        color = Color(0xFF2196F3),
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}