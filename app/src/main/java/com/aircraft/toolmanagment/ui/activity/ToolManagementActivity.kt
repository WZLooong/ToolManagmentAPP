package com.aircraft.toolmanagment.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.domain.ToolManagementViewModel
import com.aircraft.toolmanagment.ui.adapter.ToolsAdapter
import com.aircraft.toolmanagment.util.ViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToolManagementActivity : BaseActivity() {
    private val viewModel: ToolManagementViewModel by viewModels()
    private lateinit var etToolName: EditText
    private lateinit var btnAddTool: Button
    private lateinit var btnSearchTool: Button
    private lateinit var rvTools: RecyclerView
    private lateinit var toolsAdapter: ToolsAdapter

    companion object {
        private const val TAG = "ToolManagementActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_tool_management

    override fun initViews() {
        etToolName = findViewById(R.id.et_tool_name)
        btnAddTool = findViewById(R.id.btn_add_tool)
        btnSearchTool = findViewById(R.id.btn_search_tool)
        rvTools = findViewById(R.id.rv_tools)

        // 设置RecyclerView
        toolsAdapter = ToolsAdapter(emptyList()) { tool ->
            // 处理工具点击事件，这里可以显示详细信息或编辑界面
            Toast.makeText(this, "点击了工具: ${tool.name}", Toast.LENGTH_SHORT).show()
        }
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
            if (name.isNotEmpty()) {
                viewModel.addTool(
                    context = this,
                    name = name,
                    model = "",
                    specification = "",
                    quantity = 1,
                    manufacturer = "",
                    purchaseDate = null,
                    storageLocation = "",
                    barcode = "",
                    status = "可用"
                )
                etToolName.text.clear()
            } else {
                Toast.makeText(this, "请输入工具名称", Toast.LENGTH_SHORT).show()
            }
        }

        btnSearchTool.setOnClickListener {
            val name = etToolName.text.toString().trim()
            viewModel.queryTools(this, if (name.isNotEmpty()) name else null)
        }
    }

    private fun observeViewModel() {
        // 观察工具列表状态
        viewModel.toolsState.observe(this, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 可以显示加载指示器
                    Log.d(TAG, "正在加载工具列表...")
                }
                is ViewState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val tools = state.data as? List<Tool> ?: emptyList()
                    Log.d(TAG, "工具列表加载成功，共 ${tools.size} 个工具")
                    toolsAdapter.updateTools(tools)
                }
                is ViewState.Error -> {
                    Log.e(TAG, "工具列表加载失败: ${state.message}")
                    Toast.makeText(this, "加载失败: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // 观察添加工具状态
        viewModel.addToolState.observe(this, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    // 可以显示加载指示器
                    Log.d(TAG, "正在添加工具...")
                }
                is ViewState.Success<*> -> {
                    Log.d(TAG, "工具添加成功")
                    Toast.makeText(this, "工具添加成功", Toast.LENGTH_SHORT).show()
                    // 重新加载工具列表
                    viewModel.queryTools(this)
                }
                is ViewState.Error -> {
                    Log.e(TAG, "工具添加失败: ${state.message}")
                    Toast.makeText(this, "添加失败: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}