package com.aircraft.toolmanagment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.aircraft.toolmanagment.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE (name = :identifier OR employee_id = :identifier) AND password = :password")
    suspend fun getUserByIdentifierAndPassword(identifier: String, password: String): User?

    @Query("SELECT * FROM users WHERE name = :name OR employee_id = :userId LIMIT 1")
    suspend fun getUserByNameOrUserId(name: String, userId: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}