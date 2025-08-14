package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import com.aircraft.toolmanagment.util.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun getLayoutRes(): Int = R.layout.activity_user_login

    override fun initViews() {
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
    }

    override fun initData() {
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString()
            
            // 输入验证
            if (username.isBlank()) {
                showToast("请输入用户名")
                return@setOnClickListener
            }
            
            if (password.isBlank()) {
                showToast("请输入密码")
                return@setOnClickListener
            }
            
            // 使用UserViewModel进行登录验证
            userViewModel.login(this, username, password)
        }
    }

    private fun observeViewModel() {
        // 观察登录状态变化
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
                        // 登录成功后跳转到用户管理界面
                        val intent = android.content.Intent(this, UserManagementActivity::class.java)
                        startActivity(intent)
                        showToast("登录成功")
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
}