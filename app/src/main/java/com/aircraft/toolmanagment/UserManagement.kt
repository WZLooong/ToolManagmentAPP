package com.aircraft.toolmanagment

import android.content.Context
import android.util.Log
import com.aircraft.toolmanagment.data.AppDatabase
import com.aircraft.toolmanagment.data.entity.ApiResponse
import com.aircraft.toolmanagment.data.entity.User
import com.aircraft.toolmanagment.data.entity.UserLoginRequest
import com.aircraft.toolmanagment.data.entity.UserLoginResponse
import com.aircraft.toolmanagment.data.entity.UserRegisterRequest
import com.aircraft.toolmanagment.data.entity.UserRegisterResponse
import com.aircraft.toolmanagment.network.ApiService
import com.aircraft.toolmanagment.network.NetworkModule
import com.aircraft.toolmanagment.network.UpdateUserRequest
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserManagement(private val context: Context) {
    private val TAG = "UserManagement"
    private val userDao = AppDatabase.getDatabase(context).userDao()

    suspend fun registerUser(phone: String?, email: String?, password: String, name: String, employeeId: String, team: String, role: String): Boolean {
        return try {
            // Prepare registration request
            val request = UserRegisterRequest(
                phone = phone,
                email = email,
                password = password,
                name = name,
                employeeId = employeeId,
                team = team,
                role = role
            )

            // Call API to register user
            val response: ApiResponse<UserRegisterResponse> = withContext(Dispatchers.IO) {
                NetworkModule.apiService.registerUser(request)
            }

            // Handle API response
            if (response.success && response.data != null) {
                Log.d(TAG, "用户注册成功: $name")
                // For registration, we don't get a full User object back, just success/failure
                true
            } else {
                Log.e(TAG, "用户注册失败: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "注册用户时发生异常", e)
            false
        }
    }

    suspend fun loginUser(identifier: String, password: String): User? {
        return try {
            // Directly call API to authenticate, skip local database password verification
            val request = UserLoginRequest(
                identifier = identifier,
                password = password
            )

            // Call API to login user
            val response: ApiResponse<UserLoginResponse> = withContext(Dispatchers.IO) {
                NetworkModule.apiService.loginUser(request)
            }

            // Handle API response
            if (response.success && response.data != null && response.data.user != null) {
                Log.d(TAG, "用户登录成功: ${response.data.user.username}")
                // Save user to local database
                withContext(Dispatchers.IO) {
                    userDao.insertUser(response.data.user)
                }
                response.data.user
            } else {
                Log.e(TAG, "用户登录失败: ${response.message}")
                null
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e(TAG, "用户登录时发生HTTP异常，状态码: ${e.code()}, 错误信息: ${e.message()}, 响应体: $errorBody", e)
            throw Exception("登录服务器错误 (HTTP ${e.code()}): $errorBody")
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "用户登录时发生网络超时异常", e)
            throw Exception("连接服务器超时，请检查网络连接或稍后重试")
        } catch (e: UnknownHostException) {
            Log.e(TAG, "用户登录时发生主机解析异常", e)
            throw Exception("无法连接到服务器，请检查网络连接")
        } catch (e: Exception) {
            Log.e(TAG, "用户登录时发生异常", e)
            throw Exception("登录过程中发生错误: ${e.message}")
        }
    }

    suspend fun checkUserExists(identifier: String): Boolean {
        return try {
            // First check local database
            val localUser = withContext(Dispatchers.IO) {
                userDao.getUserByNameOrUserId(identifier, identifier)
            }

            if (localUser != null) {
                return true
            }

            // If not found locally, check with API
            val response: ApiResponse<Boolean> = withContext(Dispatchers.IO) {
                NetworkModule.apiService.checkUserExists(identifier)
            }

            response.success && response.data == true
        } catch (e: Exception) {
            Log.e(TAG, "检查用户是否存在时发生异常", e)
            false
        }
    }

    suspend fun updateUser(username: String, newPassword: String?, newEmail: String?, newPhone: String?): Boolean {
        return try {
            // Prepare update request
            val request = UpdateUserRequest(
                password = newPassword,
                email = newEmail,
                phone = newPhone
            )

            // Call API to update user
            val response: ApiResponse<Unit> = withContext(Dispatchers.IO) {
                NetworkModule.apiService.updateUserInfo(username, request)
            }

            // Update local database if API call is successful
            if (response.success) {
                Log.d(TAG, "用户信息更新成功: $username")
                true
            } else {
                Log.e(TAG, "用户信息更新失败: ${response.message}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "更新用户信息时发生异常", e)
            false
        }
    }

    suspend fun deleteUser(username: String): Boolean {
        return try {
            Log.d(TAG, "用户删除功能尚未完全实现: $username")
            // In a real implementation, we would call the API to delete the user
            // and then delete from local database
            false
        } catch (e: Exception) {
            Log.e(TAG, "删除用户时发生异常", e)
            false
        }
    }
}