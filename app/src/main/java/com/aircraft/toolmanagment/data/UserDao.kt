package com.aircraft.toolmanagment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aircraft.toolmanagment.data.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE (name = :identifier OR user_id = :identifier) AND password = :password")
    suspend fun getUserByIdentifierAndPassword(identifier: String, password: String): User?

    @Update
    suspend fun updateUser(user: User)
}