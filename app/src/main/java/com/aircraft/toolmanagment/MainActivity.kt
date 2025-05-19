package com.aircraft.toolmanagment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater // 新增导入
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView // 确保导入 AndroidView
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


        setContent {
            LoginRegisterScreen()
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
        // 加载登录布局
        AndroidView(

            factory = { context ->
             if (context != null) {
                    LayoutInflater.from(context).inflate(R.layout.activity_login,null)
                } else {
                    throw IllegalArgumentException("Context cannot be null")
                }
            },
            update = { view ->
                view.findViewById<Button>(R.id.btn_login).setOnClickListener {
                    val username = view.findViewById<EditText>(R.id.et_username_login).text.toString()
                    val password = view.findViewById<EditText>(R.id.et_password_login).text.toString()
                    onLogin(username, password)
                }
                view.findViewById<Button>(R.id.btn_register_nav).setOnClickListener {
                    onNavigateToRegister()
                }
            }
        )
    }


    @Composable
    fun RegisterScreen(onRegister: (String, String) -> Unit, onNavigateToLogin: () -> Unit) {
        // 加载注册布局
        AndroidView(

            factory = { context ->
                LayoutInflater.from(context).inflate(R.layout.activity_register, null)
            },
            update = { view ->
                view.findViewById<Button>(R.id.btn_register).setOnClickListener {
                    val username = view.findViewById<EditText>(R.id.et_username_register).text.toString()
                    val password = view.findViewById<EditText>(R.id.et_password_register).text.toString()
                    val confirmPassword = view.findViewById<EditText>(R.id.et_confirm_password_register).text.toString()
                    if (password == confirmPassword) {
                        onRegister(username, password)
                    } else {
                        Toast.makeText(view.context, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                    }
                }
                view.findViewById<Button>(R.id.btn_login_nav).setOnClickListener {
                    onNavigateToLogin()
                }
            }
        )
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