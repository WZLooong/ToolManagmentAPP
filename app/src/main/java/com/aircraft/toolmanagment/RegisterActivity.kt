
package com.aircraft.toolmanagment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ToolManagmentAPP.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userManagement = UserManagement(this)
        setContent {
            MaterialTheme {
                RegisterScreen {
                    phone, email, password, name, employeeId, team, role ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            showProgress(true)
                            val isRegistered = userManagement.register(phone, email, password, name, employeeId, team, role)
                            runOnUiThread {
                                showProgress(false)
                                if (isRegistered) {
                                    Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, "注册失败，用户名或员工 ID 已存在", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
                                showProgress(false)
                                Toast.makeText(this@RegisterActivity, "注册异常: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        runOnUiThread {
            setContent {
                if (show) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
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

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF5F5F5))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo和标题
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
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

        // 表单区域
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 姓名
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("姓名") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 手机号
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("手机号") },
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError != null,
                supportingText = phoneError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 邮箱
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("邮箱") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = emailError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 密码
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 确认密码
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("确认密码") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 员工ID
            OutlinedTextField(
                value = employeeId,
                onValueChange = { employeeId = it },
                label = { Text("员工ID") },
                modifier = Modifier.fillMaxWidth(),
                isError = employeeIdError != null,
                supportingText = employeeIdError?.let { { Text(it) } },
                shape = RoundedCornerShape(8.dp)
            )

            // 团队
            OutlinedTextField(
                value = team,
                onValueChange = { team = it },
                label = { Text("团队") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // 角色
            OutlinedTextField(
                value = role,
                onValueChange = { role = it },
                label = { Text("角色") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            // 注册按钮
            Button(
                onClick = {
                    // 重置错误
                    phoneError = null
                    emailError = null
                    passwordError = null
                    confirmPasswordError = null
                    nameError = null
                    employeeIdError = null

                    var isValid = true

                    // 验证姓名
                    if (name.isBlank()) {
                        nameError = "请输入姓名"
                        isValid = false
                    }

                    // 验证手机号
                    if (phone.isNotBlank() && !android.util.Patterns.PHONE.matcher(phone).matches()) {
                        phoneError = "请输入有效的手机号"
                        isValid = false
                    }

                    // 验证邮箱
                    if (email.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "请输入有效的邮箱"
                        isValid = false
                    }

                    // 验证密码
                    if (password.isBlank()) {
                        passwordError = "请输入密码"
                        isValid = false
                    } else if (password.length < 6) {
                        passwordError = "密码长度不能少于6位"
                        isValid = false
                    }

                    // 验证确认密码
                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "请确认密码"
                        isValid = false
                    } else if (confirmPassword != password) {
                        confirmPasswordError = "两次输入的密码不一致"
                        isValid = false
                    }

                    // 验证员工ID
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
        }
    }
}