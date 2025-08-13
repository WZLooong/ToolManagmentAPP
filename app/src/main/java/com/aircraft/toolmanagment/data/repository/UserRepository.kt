package com.aircraft.toolmanagment.data.repository

import com.aircraft.toolmanagment.data.UserDao
import com.aircraft.toolmanagment.data.entity.User
import com.aircraft.toolmanagment.data.remote.api.NetworkResult
import com.aircraft.toolmanagment.data.remote.api.NetworkExceptionHandler
import com.aircraft.toolmanagment.network.ApiService
import com.aircraft.toolmanagment.data.entity.UserLoginRequest
import com.aircraft.toolmanagment.data.entity.UserRegisterRequest

/**
 * 用户数据仓库，处理用户相关的本地和远程数据操作
 */
class UserRepository(
    private val localDataSource: UserDao,
    private val remoteDataSource: ApiService
) {
    /**
     * 用户登录
     */
    suspend fun login(identifier: String, password: String): NetworkResult<User?> {
        return try {
            val request = UserLoginRequest(identifier, password)
            val response = remoteDataSource.loginUser(request)
            
            if (response.success && response.data?.user != null) {
                // 保存用户到本地数据库
                localDataSource.insertUser(response.data.user)
                NetworkResult.Success(response.data.user)
            } else {
                NetworkResult.Error(null, response.message ?: "登录失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 用户注册
     */
    suspend fun register(
        phone: String?,
        email: String?,
        password: String,
        name: String,
        employeeId: String,
        team: String,
        role: String
    ): NetworkResult<Boolean> {
        return try {
            val request = UserRegisterRequest(phone, email, password, name, employeeId, team, role)
            val response = remoteDataSource.registerUser(request)
            
            if (response.success) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(null, response.message ?: "注册失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }

    /**
     * 检查用户是否存在
     */
    suspend fun checkUserExists(identifier: String): NetworkResult<Boolean> {
        return try {
            // 先检查本地数据库
            val localUser = localDataSource.getUserByNameOrUserId(identifier, identifier)
            if (localUser != null) {
                return NetworkResult.Success(true)
            }

            // 如果本地不存在，检查远程
            val response = remoteDataSource.checkUserExists(identifier)
            
            if (response.success) {
                NetworkResult.Success(response.data ?: false)
            } else {
                NetworkResult.Error(null, response.message ?: "检查用户失败")
            }
        } catch (e: Exception) {
            NetworkExceptionHandler.handle(e)
        }
    }
}