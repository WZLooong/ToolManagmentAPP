package com.aircraft.toolmanagment.data

import java.sql.DriverManager

// 移除 Room 相关导入和迁移代码

// 简单示例，可根据实际需求扩展

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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// 创建迁移对象，从版本 1 到版本 2
// 如无旧数据，可移除迁移逻辑，避免字段不一致导致崩溃

@Database(entities = [Tool::class, User::class, BorrowReturnRecord::class], version = 2)
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
                ).addMigrations(MIGRATION_1_2)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}