package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import com.aircraft.toolmanagment.util.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var etUserId: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSearchUser: Button
    private lateinit var btnUpdateUser: Button
    private lateinit var btnDeleteUser: Button
    private lateinit var rvUsers: RecyclerView

    override fun getLayoutRes(): Int = R.layout.activity_user_management

    override fun initViews() {
        etUserId = findViewById(R.id.et_user_id)
        etUsername = findViewById(R.id.et_username)
        etEmail = findViewById(R.id.et_email)
        btnSearchUser = findViewById(R.id.btn_search_user)
        btnUpdateUser = findViewById(R.id.btn_update_user)
        btnDeleteUser = findViewById(R.id.btn_delete_user)
        rvUsers = findViewById(R.id.rv_users)
    }

    override fun initData() {
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        btnSearchUser.setOnClickListener {
            val userIdText = etUserId.text.toString().trim()
            val username = etUsername.text.toString().trim()
            
            when {
                userIdText.isNotEmpty() -> {
                    showToast("按用户ID搜索功能待实现")
                }
                username.isNotEmpty() -> {
                    showToast("按用户名搜索功能待实现")
                }
                else -> {
                    showToast("请输入搜索条件")
                }
            }
        }
        
        btnUpdateUser.setOnClickListener {
            showToast("更新用户功能待实现")
        }
        
        btnDeleteUser.setOnClickListener {
            showToast("删除用户功能待实现")
        }
    }

    private fun observeViewModel() {
        // 观察登录状态变化（虽然在这个界面不用于登录，但保持一致性）
        userViewModel.loginState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 显示加载状态
                }
                is ViewState.Success<*> -> {
                    // 处理成功状态
                }
                is ViewState.Error -> {
                    showToast("操作失败: ${state.message}")
                }
            }
        }
        
        // 观察注册状态变化
        userViewModel.registerState.observe(this) { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 显示加载状态
                }
                is ViewState.Success<*> -> {
                    // 处理成功状态
                }
                is ViewState.Error -> {
                    showToast("操作失败: ${state.message}")
                }
            }
        }
    }
}