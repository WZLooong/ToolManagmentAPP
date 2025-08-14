package com.aircraft.toolmanagment.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import com.aircraft.toolmanagment.util.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegisterNav: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun initViews() {
        etEmail = findViewById(R.id.et_email_login)
        etPassword = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        btnRegisterNav = findViewById(R.id.btn_register_nav)
        
        // 添加输入监听器
        addInputListeners()
        
        // 初始化按钮状态
        btnLogin.isEnabled = false
    }

    override fun initData() {
        setupClickListeners()
    }

    private fun addInputListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInput()
            }
        }
        
        etEmail.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
    }

    private fun validateInput() {
        val identifier = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        btnLogin.isEnabled = identifier.isNotBlank() && password.isNotBlank()
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
                        // 用户不存在，跳转到注册页面
                        showToast("用户不存在，请先注册")
                        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        startActivity(intent)
                    }
                }
                is ViewState.Error -> {
                    // 恢复按钮状态
                    btnLogin.isEnabled = true
                    btnLogin.text = "登录"
                    // 根据错误信息判断是否跳转到注册页面
                    if (state.message?.contains("用户不存在") == true || 
                        state.message?.contains("不存在") == true) {
                        showToast("用户不存在，请先注册")
                        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                        startActivity(intent)
                    } else {
                        // 显示更详细的错误信息
                        showToast("登录失败: ${state.message}")
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val identifier = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            
            // 输入验证
            if (identifier.isBlank()) {
                showToast("请输入用户名/员工ID")
                return@setOnClickListener
            }
            
            if (password.isBlank()) {
                showToast("请输入密码")
                return@setOnClickListener
            }
            
            userViewModel.login(this, identifier, password)
        }

        btnRegisterNav.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}