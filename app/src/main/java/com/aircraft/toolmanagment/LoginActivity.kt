package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ToolManagmentAPP.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var userManagement: UserManagement
    private lateinit var identifierEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private lateinit var forgotPasswordTextView: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        userManagement = UserManagement(this)
        identifierEditText = findViewById(R.id.et_username_login)
        passwordEditText = findViewById(R.id.et_password_login)
        loginButton = findViewById(R.id.btn_login)

        forgotPasswordTextView = findViewById(R.id.tv_forgot_password)
        progressBar = findViewById(R.id.pb_login)

        // 登录按钮点击事件
        loginButton.setOnClickListener { 
            attemptLogin()
        }

        // 注册按钮点击事件
        findViewById<Button>(R.id.btn_register).setOnClickListener {
            attemptRegister()
        }

        // 忘记密码点击事件
        forgotPasswordTextView.setOnClickListener {
            // 这里可以添加忘记密码逻辑
            Toast.makeText(this, "忘记密码功能暂未实现", Toast.LENGTH_SHORT).show()
        }
    }

    private fun attemptRegister() {
        // 获取注册表单数据
        val email = findViewById<EditText>(R.id.et_email_register).text.toString().trim()
        val username = findViewById<EditText>(R.id.et_username_register).text.toString().trim()
        val password = findViewById<EditText>(R.id.et_password_register).text.toString().trim()
        val confirmPassword = findViewById<EditText>(R.id.et_confirm_password_register).text.toString().trim()

        // 简单验证
        if (email.isBlank() || username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "请填写所有字段", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "密码长度不能少于6位", Toast.LENGTH_SHORT).show()
            return
        }

        // 显示加载进度
        showProgress(true)

        // 执行注册
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 这里使用username作为team和role的临时值，实际应用中应该从UI获取这些信息
                val isRegistered = userManagement.register(email, email, password, username, username, username, "user")
                runOnUiThread { 
                    showProgress(false)
                    if (isRegistered) {
                        Toast.makeText(this@LoginActivity, "注册成功，请登录", Toast.LENGTH_SHORT).show()
                        // 注册成功后可以清空表单或保持现状
                    } else {
                        Toast.makeText(this@LoginActivity, "用户名或工号已存在", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { 
                    showProgress(false)
                    Toast.makeText(this@LoginActivity, "注册失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun attemptLogin() {
        // 重置错误
        identifierEditText.error = null
        passwordEditText.error = null

        // 获取输入值
        val identifier = identifierEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        var cancel = false
        var focusView: View? = null

        // 验证用户名
        if (identifier.isBlank()) {
            identifierEditText.error = "请输入用户名"
            focusView = identifierEditText
            cancel = true
        }

        // 验证密码
        if (password.isBlank()) {
            passwordEditText.error = "请输入密码"
            focusView = passwordEditText
            cancel = true
        } else if (password.length < 6) {
            passwordEditText.error = "密码长度不能少于6位"
            focusView = passwordEditText
            cancel = true
        }

        if (cancel) {
            // 有错误，聚焦到第一个有错误的输入框
            focusView?.requestFocus()
        } else {
            // 显示加载进度
            showProgress(true)

            // 执行登录逻辑
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // 先检查用户是否存在
                    val userExists = userManagement.checkUserExists(identifier)
                    if (!userExists) {
                        // 用户不存在，跳转到注册页面
                        runOnUiThread {
                            showProgress(false)
                            Toast.makeText(this@LoginActivity, "用户不存在，请先注册", Toast.LENGTH_SHORT).show()
                            // 自动填充用户名到注册表单
                            findViewById<EditText>(R.id.et_username_register).setText(identifier)
                            // 滚动到注册表单
                            findViewById<ScrollView>(R.id.scroll_view_login).smoothScrollTo(0, findViewById<LinearLayout>(R.id.ll_register_form).top)
                        }
                    } else {
                        // 用户存在，验证密码
                        val isLoggedIn = userManagement.login(identifier, password)
                        runOnUiThread {
                            showProgress(false)
                            if (isLoggedIn) {
                                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "密码错误，请重新输入", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        showProgress(false)
                        Toast.makeText(this@LoginActivity, "登录时发生错误: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        loginButton.isEnabled = !show
        findViewById<Button>(R.id.btn_register).isEnabled = !show
    }
}