package com.aircraft.toolmanagment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.data.entity.Tool

class ToolsAdapter(
    private var tools: List<Tool>,
    private val onToolClick: (Tool) -> Unit
) : RecyclerView.Adapter<ToolsAdapter.ToolViewHolder>() {

    fun updateTools(newTools: List<Tool>) {
        tools = newTools
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tool, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(tools[position])
    }

    override fun getItemCount(): Int = tools.size

    inner class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvToolName: TextView = itemView.findViewById(R.id.tv_tool_name)
        private val tvToolModel: TextView = itemView.findViewById(R.id.tv_tool_model)
        private val tvToolQuantity: TextView = itemView.findViewById(R.id.tv_tool_quantity)
        private val tvToolStatus: TextView = itemView.findViewById(R.id.tv_tool_status)

        fun bind(tool: Tool) {
            tvToolName.text = tool.name
            // 修复：移除不必要的Elvis操作符，因为model字段是非空类型
            tvToolModel.text = if (tool.model.isNotEmpty()) tool.model else "未指定型号"
            tvToolQuantity.text = "数量: ${tool.quantity}"
            // 修复：移除不必要的Elvis操作符，因为status字段是非空类型
            tvToolStatus.text = "状态: ${if (tool.status.isNotEmpty()) tool.status else "未知"}"

            // 根据状态设置颜色
            when (tool.status) {
                "可用" -> tvToolStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_green_dark)
                )
                "借出" -> tvToolStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_orange_dark)
                )
                else -> tvToolStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark)
                )
            }

            itemView.setOnClickListener { onToolClick(tool) }
        }
    }
}