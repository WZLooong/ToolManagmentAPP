package com.aircraft.toolmanagment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.data.entity.Tool

class ToolsAdapter : ListAdapter<Tool, ToolsAdapter.ToolViewHolder>(ToolDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tool, parent, false)
        return ToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ToolViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvToolName: TextView = itemView.findViewById(R.id.tv_tool_name)
        private val tvToolModel: TextView = itemView.findViewById(R.id.tv_tool_model)
        private val tvToolQuantity: TextView = itemView.findViewById(R.id.tv_tool_quantity)
        private val tvToolStatus: TextView = itemView.findViewById(R.id.tv_tool_status)

        fun bind(tool: Tool) {
            tvToolName.text = tool.name
            tvToolModel.text = tool.model ?: "未指定型号"
            tvToolQuantity.text = "数量: ${tool.quantity}"
            tvToolStatus.text = "状态: ${tool.status ?: "未知"}"
            
            // 根据状态设置颜色
            when (tool.status) {
                "可用" -> tvToolStatus.setTextColor(
                    itemView.context.getColor(android.R.color.holo_green_dark)
                )
                "借出" -> tvToolStatus.setTextColor(
                    itemView.context.getColor(android.R.color.holo_orange_dark)
                )
                else -> tvToolStatus.setTextColor(
                    itemView.context.getColor(android.R.color.darker_gray)
                )
            }
        }
    }

    class ToolDiffCallback : DiffUtil.ItemCallback<Tool>() {
        override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem == newItem
        }
    }
}