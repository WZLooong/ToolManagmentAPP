package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.domain.viewmodel.ToolManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolManagementActivity : BaseActivity() {
    private val viewModel: ToolManagementViewModel by viewModels()
    private lateinit var etToolName: EditText
    private lateinit var etToolModel: EditText
    private lateinit var etToolQuantity: EditText
    private lateinit var btnAddTool: Button
    private lateinit var btnSearchTool: Button
    private lateinit var rvTools: RecyclerView
    private lateinit var toolsAdapter: ToolsAdapter

    override fun getLayoutRes(): Int = R.layout.activity_tool_management

    override fun initViews() {
        etToolName = findViewById(R.id.et_tool_name)
        etToolModel = findViewById(R.id.et_tool_model)
        etToolQuantity = findViewById(R.id.et_tool_quantity)
        btnAddTool = findViewById(R.id.btn_add_tool)
        btnSearchTool = findViewById(R.id.btn_search_tool)
        rvTools = findViewById(R.id.rv_tools)

        // 设置RecyclerView
        toolsAdapter = ToolsAdapter()
        rvTools.layoutManager = LinearLayoutManager(this)
        rvTools.adapter = toolsAdapter
    }

    override fun initData() {
        setupClickListeners()
        observeViewModel()
        
        // 初始加载所有工具
        viewModel.queryTools(this)
    }

    private fun setupClickListeners() {
        btnAddTool.setOnClickListener {
            val name = etToolName.text.toString().trim()
            val model = etToolModel.text.toString().trim()
            val quantityStr = etToolQuantity.text.toString().trim()
            
            if (name.isBlank()) {
                showToast("请输入工具名称")
                return@setOnClickListener
            }
            
            if (quantityStr.isBlank()) {
                showToast("请输入工具数量")
                return@setOnClickListener
            }
            
            val quantity = quantityStr.toIntOrNull() ?: 0
            
            viewModel.addTool(
                context = this,
                name = name,
                model = model.ifBlank { null },
                specification = null,
                quantity = quantity,
                manufacturer = null,
                purchaseDate = null,
                storageLocation = null,
                barcode = null,
                status = "可用"
            )
        }
        
        btnSearchTool.setOnClickListener {
            val keyword = etToolName.text.toString().trim()
            viewModel.queryTools(this, if (keyword.isBlank()) null else keyword)
        }
    }

    private fun observeViewModel() {
        viewModel.toolsState.observe(this) { state ->
            when (state) {
                is com.aircraft.toolmanagment.util.ViewState.Loading -> {
                    // 可以显示加载指示器
                }
                is com.aircraft.toolmanagment.util.ViewState.Success -> {
                    toolsAdapter.submitList(state.data)
                }
                is com.aircraft.toolmanagment.util.ViewState.Error -> {
                    showToast("加载工具失败: ${state.message}")
                }
            }
        }
        
        viewModel.addToolState.observe(this) { state ->
            when (state) {
                is com.aircraft.toolmanagment.util.ViewState.Loading -> {
                    btnAddTool.isEnabled = false
                    btnAddTool.text = "添加中..."
                }
                is com.aircraft.toolmanagment.util.ViewState.Success -> {
                    btnAddTool.isEnabled = true
                    btnAddTool.text = "添加工具"
                    
                    if (state.data) {
                        showToast("工具添加成功")
                        // 清空输入框
                        etToolName.text.clear()
                        etToolModel.text.clear()
                        etToolQuantity.text.clear()
                        // 重新加载工具列表
                        viewModel.queryTools(this)
                    } else {
                        showToast("工具添加失败")
                    }
                }
                is com.aircraft.toolmanagment.util.ViewState.Error -> {
                    btnAddTool.isEnabled = true
                    btnAddTool.text = "添加工具"
                    showToast("添加工具失败: ${state.message}")
                }
            }
        }
    }
}