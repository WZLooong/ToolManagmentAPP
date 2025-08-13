package com.aircraft.toolmanagment.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.aircraft.toolmanagment.R

class MainActivity : BaseActivity() {
    private lateinit var btnToolManagement: Button
    private lateinit var btnBorrowReturn: Button
    private lateinit var btnNetworkTest: Button

    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun initViews() {
        btnToolManagement = findViewById(R.id.btn_tool_management)
        btnBorrowReturn = findViewById(R.id.btn_borrow_return)
        btnNetworkTest = findViewById(R.id.btn_network_test)
    }

    override fun initData() {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        btnToolManagement.setOnClickListener {
            val intent = Intent(this, ToolManagementActivity::class.java)
            startActivity(intent)
        }

        btnBorrowReturn.setOnClickListener {
            val intent = Intent(this, BorrowReturnActivity::class.java)
            startActivity(intent)
        }

        btnNetworkTest.setOnClickListener {
            val intent = Intent(this, NetworkTestActivity::class.java)
            startActivity(intent)
        }
    }
}