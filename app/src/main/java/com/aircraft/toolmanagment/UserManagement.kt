package com.aircraft.toolmanagment
import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.User
import java.sql.DriverManager

class UserManagement(private val context: Context) {
    private val db  = databaseBuilder(
        context,
        AppDatabase::class.java,
        "aircraft_tool_management"
    ).build()

    suspend fun registerUser(user: User) {
        db.userDao().insertUser(user)
    }

    suspend fun loginUser(identifier: String, password: String): User? {
        return db.userDao().getUserByIdentifierAndPassword(identifier, password)
    }

    suspend fun updateUserInfo(user: User) {
        db.userDao().updateUser(user)
    }

    suspend fun register(phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String): Boolean {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun login(identifier: String, password: String): Boolean {
        val user = db.userDao().getUserByIdentifier(identifier)
        return user != null && user.password == password
    }

    fun updateUserInfo(userId: Int, newInfo: Map<String, Any>) {
        val url = "jdbc:mysql://39.106.150.70/aircraft_tool_management"
        val username = "aircraft_tool_management"
        val dbPassword = "88888888"
        DriverManager.getConnection(url, username, dbPassword).use {
            val columns = newInfo.keys.joinToString(", ") { "$it = ?" }
            val sql = "UPDATE users SET $columns WHERE id = ?"
            it.prepareStatement(sql).use {
                var index = 1
                newInfo.values.forEach { value ->
                    it.setObject(index++, value)
                }
                it.setInt(index, userId)
                it.executeUpdate()
            }
        }
    }

    fun manageUserRole(userId: Int, newRole: String, newInfo: Map<String, Any>) {
        val url = "jdbc:mysql://39.106.150.70/aircraft_tool_management"
        val username = "aircraft_tool_management"
        val dbPassword = "88888888"
        DriverManager.getConnection(url, username, dbPassword).use {
            val columns = newInfo.keys.joinToString(", ") { "$it = ?" }
            val sql = "UPDATE users SET $columns WHERE id = ?"
            it.prepareStatement(sql).use {
                var index = 1
                newInfo.values.forEach { value ->
                    it.setObject(index++, value)
                }
                it.setInt(index, userId)
                it.executeUpdate()
            }
        }
    }
}