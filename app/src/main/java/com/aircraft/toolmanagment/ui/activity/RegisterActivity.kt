package com.aircraft.toolmanagment.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import com.aircraft.toolmanagment.util.ViewState

class RegisterActivity : BaseActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLoginNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        observeViewModel()
    }

    override fun getLayoutRes(): Int = R.layout.activity_register

    override fun initViews() {
        etEmail = findViewById(R.id.et_email_register)
        etUsername = findViewById(R.id.et_username_register)
        etPassword = findViewById(R.id.et_password_register)
        etConfirmPassword = findViewById(R.id.et_confirm_password_register)
        btnRegister = findViewById(R.id.btn_register)
        btnLoginNav = findViewById(R.id.btn_login_nav)
    }

    override fun initData() {
        setupClickListeners()
    }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    private fun observeViewModel() {
        userViewModel.registerState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 显示加载状态
                    btnRegister.isEnabled = false
                    btnRegister.text = "注册中..."
                }
                is ViewState.Success -> {
                    // 恢复按钮状态
                    btnRegister.isEnabled = true
                    btnRegister.text = "注册"
                    
                    if (state.data == true) {
                        showToast("注册成功")
                        // Navigate to login after successful registration
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("注册失败，请重试")
                    }
                }
                is ViewState.Error -> {
                    // 恢复按钮状态
                    btnRegister.isEnabled = true
                    btnRegister.text = "注册"
                    showToast("注册失败: ${state.message}")
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            
            if (password != confirmPassword) {
                showToast("密码不匹配")
                return@setOnClickListener
            }
            
            userViewModel.register(
                this,
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
}