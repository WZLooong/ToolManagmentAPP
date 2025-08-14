package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.BorrowReturnViewModel
import com.aircraft.toolmanagment.ui.adapter.BorrowRecordsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BorrowReturnActivity : BaseActivity() {
    private val viewModel: BorrowReturnViewModel by viewModels()
    private lateinit var etToolId: EditText
    private lateinit var etUserId: EditText
    private lateinit var btnBorrow: Button
    private lateinit var btnReturn: Button
    private lateinit var rvBorrowRecords: RecyclerView
    private lateinit var borrowRecordsAdapter: BorrowRecordsAdapter

    override fun getLayoutRes(): Int = R.layout.activity_borrow_return

    override fun initViews() {
        etToolId = findViewById(R.id.et_tool_id)
        etUserId = findViewById(R.id.et_user_id)
        btnBorrow = findViewById(R.id.btn_borrow)
        btnReturn = findViewById(R.id.btn_return)
        rvBorrowRecords = findViewById(R.id.rv_borrow_records)

        // 设置RecyclerView
        borrowRecordsAdapter = BorrowRecordsAdapter()
        rvBorrowRecords.layoutManager = LinearLayoutManager(this)
        rvBorrowRecords.adapter = borrowRecordsAdapter
    }

    override fun initData() {
        setupClickListeners()
        observeViewModel()
        
        // 初始加载所有借还记录
        viewModel.getBorrowRecords(this)
    }

    private fun setupClickListeners() {
        btnBorrow.setOnClickListener {
            val toolIdStr = etToolId.text.toString().trim()
            val userIdStr = etUserId.text.toString().trim()
            
            if (toolIdStr.isBlank()) {
                showToast("请输入工具ID")
                return@setOnClickListener
            }
            
            if (userIdStr.isBlank()) {
                showToast("请输入用户ID")
                return@setOnClickListener
            }
            
            val toolId = toolIdStr.toIntOrNull()
            val userId = userIdStr.toIntOrNull()
            
            if (toolId == null) {
                showToast("工具ID必须是数字")
                return@setOnClickListener
            }
            
            if (userId == null) {
                showToast("用户ID必须是数字")
                return@setOnClickListener
            }
            
            // 创建借出记录
            val borrowRecord = com.aircraft.toolmanagment.data.entity.BorrowReturnRecord(
                toolId = toolId,
                borrowerId = userId,
                borrowTime = System.currentTimeMillis(),
                expectedReturnTime = 0,
                actualReturnTime = null,
                borrowReason = "",
                approvalStatus = "",
                rejectionReason = ""
            )
            
            viewModel.borrowTool(this, borrowRecord)
        }
        
        btnReturn.setOnClickListener {
            val toolIdStr = etToolId.text.toString().trim()
            
            if (toolIdStr.isBlank()) {
                showToast("请输入工具ID")
                return@setOnClickListener
            }
            
            val toolId = toolIdStr.toIntOrNull()
            
            if (toolId == null) {
                showToast("工具ID必须是数字")
                return@setOnClickListener
            }
            
            // 这里简化处理，实际应该选择具体的借出记录
            // 在实际应用中，应该显示该工具的所有未归还记录供用户选择
            viewModel.returnTool(this, toolId)
        }
    }

    private fun observeViewModel() {
        viewModel.borrowRecordsState.observe(this) { state ->
            when (state) {
                is com.aircraft.toolmanagment.util.ViewState.Loading -> {
                    // 可以显示加载指示器
                }
                is com.aircraft.toolmanagment.util.ViewState.Success<*> -> {
                    borrowRecordsAdapter.submitList(state.data as List<com.aircraft.toolmanagment.data.entity.BorrowReturnRecord>)
                }
                is com.aircraft.toolmanagment.util.ViewState.Error -> {
                    showToast("加载借还记录失败: ${state.message}")
                }
            }
        }
        
        viewModel.borrowToolState.observe(this) { state ->
            when (state) {
                is com.aircraft.toolmanagment.util.ViewState.Loading -> {
                    btnBorrow.isEnabled = false
                    btnBorrow.text = "借出中..."
                }
                is com.aircraft.toolmanagment.util.ViewState.Success<*> -> {
                    btnBorrow.isEnabled = true
                    btnBorrow.text = "借出"
                    
                    if (state.data as Boolean) {
                        showToast("工具借出成功")
                        // 清空输入框
                        etToolId.text.clear()
                        etUserId.text.clear()
                        // 重新加载借还记录
                        viewModel.getBorrowRecords(this)
                    } else {
                        showToast("工具借出失败")
                    }
                }
                is com.aircraft.toolmanagment.util.ViewState.Error -> {
                    btnBorrow.isEnabled = true
                    btnBorrow.text = "借出"
                    showToast("借出工具失败: ${state.message}")
                }
            }
        }
        
        viewModel.returnToolState.observe(this) { state ->
            when (state) {
                is com.aircraft.toolmanagment.util.ViewState.Loading -> {
                    btnReturn.isEnabled = false
                    btnReturn.text = "归还中..."
                }
                is com.aircraft.toolmanagment.util.ViewState.Success<*> -> {
                    btnReturn.isEnabled = true
                    btnReturn.text = "归还"
                    
                    if (state.data as Boolean) {
                        showToast("工具归还成功")
                        // 清空输入框
                        etToolId.text.clear()
                        // 重新加载借还记录
                        viewModel.getBorrowRecords(this)
                    } else {
                        showToast("工具归还失败")
                    }
                }
                is com.aircraft.toolmanagment.util.ViewState.Error -> {
                    btnReturn.isEnabled = true
                    btnReturn.text = "归还"
                    showToast("归还工具失败: ${state.message}")
                }
            }
        }
    }
}