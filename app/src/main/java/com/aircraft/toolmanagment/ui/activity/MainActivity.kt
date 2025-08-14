package com.aircraft.toolmanagment.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var btnBorrowReturn: Button
    private lateinit var btnToolManagement: Button
    private lateinit var btnUserManagement: Button
    private lateinit var btnUserDetails: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {
        btnBorrowReturn = findViewById(R.id.btn_borrow_return)
        btnToolManagement = findViewById(R.id.btn_tool_management)
        btnUserManagement = findViewById(R.id.btn_user_management)
        btnUserDetails = findViewById(R.id.btn_user_details)

        setupClickListeners()
    }

    override fun initData() {
        // MainActivity不需要初始化额外的数据
    }

    private fun setupClickListeners() {
        btnBorrowReturn.setOnClickListener {
            val intent = Intent(this, BorrowReturnActivity::class.java)
            startActivity(intent)
        }

        btnToolManagement.setOnClickListener {
            val intent = Intent(this, ToolManagementActivity::class.java)
            startActivity(intent)
        }

        btnUserManagement.setOnClickListener {
            // 跳转到用户管理登录界面
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        // 暂时移除用户详情按钮的点击事件，因为它是一个测试功能
        btnUserDetails.setOnClickListener {
            showToast("用户详情功能正在开发中")
        }
    }
}