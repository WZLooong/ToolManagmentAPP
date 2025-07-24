
package com.aircraft.toolmanagment

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
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
                RegisterScreen { phone, email, password, name, employeeId, team, role ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val isRegistered = userManagement.register(phone, email, password, name, employeeId, team, role)
                            runOnUiThread {
                                if (isRegistered) {
                                    Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, "注册失败，用户名或员工 ID 已存在", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            runOnUiThread {
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
    var name by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var team by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("手机号") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("邮箱") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("密码") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("姓名") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = employeeId, onValueChange = { employeeId = it }, label = { Text("员工ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = team, onValueChange = { team = it }, label = { Text("团队") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("角色") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.size(16.dp))
        val context = LocalContext.current
        Button(onClick = {
            if (password.isBlank() || name.isBlank() || employeeId.isBlank()) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(context, "密码、姓名和员工 ID 不能为空", Toast.LENGTH_SHORT).show()
                }
                return@Button
            }
            onRegister(phone.takeIf { it.isNotBlank() }, email.takeIf { it.isNotBlank() }, password, name, employeeId, team, role)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("注册")
        }
    }
}