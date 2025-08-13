package com.aircraft.toolmanagment.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import com.aircraft.toolmanagment.util.ViewState

class LoginActivity : BaseActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegisterNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        observeViewModel()
    }

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun initViews() {
        etEmail = findViewById(R.id.et_email_login)
        etPassword = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        btnRegisterNav = findViewById(R.id.btn_register_nav)
    }

    override fun initData() {
        setupClickListeners()
    }

    private fun initViewModel() {
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
    }

    private fun observeViewModel() {
        userViewModel.loginState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 显示加载状态
                    btnLogin.isEnabled = false
                    btnLogin.text = "登录中..."
                }
                is ViewState.Success -> {
                    // 恢复按钮状态
                    btnLogin.isEnabled = true
                    btnLogin.text = "登录"
                    
                    if (state.data != null) {
                        showToast("登录成功")
                        // Navigate to main activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        showToast("登录失败，请检查用户名和密码")
                    }
                }
                is ViewState.Error -> {
                    // 恢复按钮状态
                    btnLogin.isEnabled = true
                    btnLogin.text = "登录"
                    showToast("登录失败: ${state.message}")
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val identifier = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            userViewModel.login(this, identifier, password)
        }

        btnRegisterNav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}