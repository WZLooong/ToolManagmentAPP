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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// 创建迁移对象，从版本 1 到版本 2
val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 创建 User 表
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `User` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `password` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `user_name` TEXT NOT NULL, `department` TEXT NOT NULL, `positon` TEXT NOT NULL)")
    }
}

@Database(entities = [Tool::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun userDao(): UserDao

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