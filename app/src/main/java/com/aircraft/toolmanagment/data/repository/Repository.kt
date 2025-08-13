package com.aircraft.toolmanagment.data.repository

import android.content.Context
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.BorrowReturnDao
import com.aircraft.toolmanagment.data.ToolDao
import com.aircraft.toolmanagment.data.UserDao
import com.aircraft.toolmanagment.network.ApiService

/**
 * Repository管理类，提供统一的数据访问入口
 */
class Repository private constructor(
    private val appDatabase: AppDatabase,
    private val apiService: ApiService
) {
    // 数据访问对象
    val userDao: UserDao = appDatabase.userDao()
    val toolDao: ToolDao = appDatabase.toolDao()
    val borrowReturnDao: BorrowReturnDao = appDatabase.borrowReturnDao()
    
    // 数据仓库
    val userRepo: UserRepository = UserRepository(userDao, apiService)
    val toolRepo: ToolRepository = ToolRepository(toolDao, apiService)
    val borrowReturnRepo: BorrowReturnRepository = BorrowReturnRepository(borrowReturnDao, apiService)
    
    // 网络服务
    val api: ApiService = apiService
    
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        
        fun getInstance(context: Context, apiService: ApiService): Repository {
            return INSTANCE ?: synchronized(this) {
                val instance = Repository(
                    AppDatabase.getDatabase(context),
                    apiService
                )
                INSTANCE = instance
                instance
            }
        }
    }
}