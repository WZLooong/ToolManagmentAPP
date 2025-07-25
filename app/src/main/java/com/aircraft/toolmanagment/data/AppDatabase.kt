package com.aircraft.toolmanagment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.data.entity.User
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.ToolDao
import com.aircraft.toolmanagment.data.UserDao
import com.aircraft.toolmanagment.data.BorrowReturnDao

@Database(entities = [Tool::class, User::class, BorrowReturnRecord::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun userDao(): UserDao
    abstract fun borrowReturnDao(): BorrowReturnDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aircraft_tool_management"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}