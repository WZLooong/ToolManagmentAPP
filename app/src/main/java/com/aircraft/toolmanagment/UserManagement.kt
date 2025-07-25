package com.aircraft.toolmanagment

import android.content.Context
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.User

class UserManagement(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)

    suspend fun register(phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String): Boolean {
        // 注册前查重
        val exist = db.userDao().getUserByNameOrUserId(name, employeeId)
        if (exist != null) return false
        val newUser = User(
            username = name,
            password = password,
            name = name,
            employeeId = employeeId,
            phone = phone,
            email = email,
            team = team,
            role = role
        )
        db.userDao().insertUser(newUser)
        return true
    }

    suspend fun login(identifier: String, password: String): Boolean {
        val user = db.userDao().getUserByIdentifierAndPassword(identifier, password)
        return user != null
    }

    suspend fun updateUserInfo(user: User) {
        db.userDao().updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        db.userDao().deleteUser(user)
    }

    // 检查用户是否存在
    suspend fun checkUserExists(identifier: String): Boolean {
        val user = db.userDao().getUserByNameOrUserId(identifier, identifier)
        return user != null
    }

    // JDBC相关内容已移除，仅保留Room本地数据库操作
}