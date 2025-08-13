package com.aircraft.toolmanagment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var userManagement: UserManagement
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLoginNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        userManagement = UserManagement(this)
        
        // 初始化视图组件
        initViews()
        
        // 设置点击事件监听器
        setupClickListeners()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.et_email_register)
        etUsername = findViewById(R.id.et_username_register)
        etPassword = findViewById(R.id.et_password_register)
        etConfirmPassword = findViewById(R.id.et_confirm_password_register)
        btnRegister = findViewById(R.id.btn_register)
        btnLoginNav = findViewById(R.id.btn_login_nav)
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            
            if (password != confirmPassword) {
                Toast.makeText(this, "密码不匹配", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            registerUser(
                email.ifBlank { null },
                email.ifBlank { null },
                password,
                username,
                username,
                "Default Team",
                "USER"
            )
        }
        
        // 设置登录导航按钮点击事件
        btnLoginNav.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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