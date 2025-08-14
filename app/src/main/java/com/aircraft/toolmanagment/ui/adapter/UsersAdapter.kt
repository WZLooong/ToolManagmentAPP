package com.aircraft.toolmanagment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aircraft.toolmanagment.R
import com.aircraft.toolmanagment.data.entity.User

class UsersAdapter : ListAdapter<User, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserId: TextView = itemView.findViewById(R.id.tv_user_id)
        private val tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
        private val tvUserEmail: TextView = itemView.findViewById(R.id.tv_user_email)
        private val tvUserEmployeeId: TextView = itemView.findViewById(R.id.tv_user_employee_id)

        fun bind(user: User) {
            tvUserId.text = "ID: ${user.id}"
            tvUserName.text = "姓名: ${user.name}"
            tvUserEmail.text = "邮箱: ${user.email ?: "未设置"}"
            tvUserEmployeeId.text = "员工ID: ${user.employeeId}"
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}