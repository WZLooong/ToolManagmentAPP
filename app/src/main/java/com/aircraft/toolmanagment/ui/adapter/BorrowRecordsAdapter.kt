package com.aircraft.toolmanagment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import java.text.SimpleDateFormat
import java.util.*

class BorrowRecordsAdapter : ListAdapter<BorrowReturnRecord, BorrowRecordsAdapter.BorrowRecordViewHolder>(BorrowRecordDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorrowRecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_borrow_record, parent, false)
        return BorrowRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: BorrowRecordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BorrowRecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRecordId: TextView = itemView.findViewById(R.id.tv_record_id)
        private val tvRecordStatus: TextView = itemView.findViewById(R.id.tv_record_status)
        private val tvToolInfo: TextView = itemView.findViewById(R.id.tv_tool_info)
        private val tvUserInfo: TextView = itemView.findViewById(R.id.tv_user_info)
        private val tvBorrowTime: TextView = itemView.findViewById(R.id.tv_borrow_time)
        private val tvReturnTime: TextView = itemView.findViewById(R.id.tv_return_time)

        fun bind(record: BorrowReturnRecord) {
            tvRecordId.text = "记录ID: ${record.id}"
            
            // 设置状态和颜色
            if (record.returnTime != null) {
                tvRecordStatus.text = "已归还"
                tvRecordStatus.setTextColor(
                    itemView.context.getColor(android.R.color.holo_green_dark)
                )
            } else {
                tvRecordStatus.text = "未归还"
                tvRecordStatus.setTextColor(
                    itemView.context.getColor(android.R.color.holo_orange_dark)
                )
            }
            
            tvToolInfo.text = "工具ID: ${record.toolId}"
            tvUserInfo.text = "用户ID: ${record.userId}"
            
            // 格式化时间
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            tvBorrowTime.text = "借出时间: ${record.borrowTime?.let { dateFormat.format(Date(it)) } ?: "未知"}"
            tvReturnTime.text = "归还时间: ${record.returnTime?.let { dateFormat.format(Date(it)) } ?: "未归还"}"
        }
    }

    class BorrowRecordDiffCallback : DiffUtil.ItemCallback<BorrowReturnRecord>() {
        override fun areItemsTheSame(oldItem: BorrowReturnRecord, newItem: BorrowReturnRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BorrowReturnRecord, newItem: BorrowReturnRecord): Boolean {
            return oldItem == newItem
        }
    }
}