import androidx.room.Delete
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

    @Query("SELECT * FROM User WHERE name = :name OR user_id = :userId LIMIT 1")
    suspend fun getUserByNameOrUserId(name: String, userId: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)
}