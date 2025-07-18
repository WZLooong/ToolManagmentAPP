package com.aircraft.toolmanagment
import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.User
import java.sql.DriverManager

class UserManagement(private val context: Context) {
    private val db = databaseBuilder(
        context,
        AppDatabase::class.java,
        "aircraft_tool_management"
    ).build()

    suspend fun register(phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String): Boolean {
        // 注册前查重
        val exist = db.userDao().getUserByNameOrUserId(name, employeeId)
        if (exist != null) return false
        val newUser = User(
            phone = phone,
            email = email,
            password = password,
            name = name,
            employee_id = employeeId,
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

    // JDBC相关内容已移除，仅保留Room本地数据库操作
}