package com.aircraft.toolmanagment.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.data.entity.User
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import com.aircraft.toolmanagment.data.ToolDao
import com.aircraft.toolmanagment.data.UserDao
import com.aircraft.toolmanagment.data.BorrowReturnDao

@Database(entities = [Tool::class, User::class, BorrowReturnRecord::class], version = 1)
@Suppress("unused")
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolDao(): ToolDao
    abstract fun userDao(): UserDao
    abstract fun borrowReturnDao(): BorrowReturnDao

    companion object {
        // 数据库迁移策略
        // 从版本1到版本2的迁移示例
        // val MIGRATION_1_2 = object : Migration(1, 2) {
        //     override fun migrate(database: SupportSQLiteDatabase) {
        //         // 在这里添加数据库结构更改的SQL语句
        //     }
        // }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aircraft_tool_management"
                )
                    // 添加迁移策略
                    // .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}