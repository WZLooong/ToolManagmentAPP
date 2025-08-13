
package com.aircraft.toolmanagment.network

import com.aircraft.toolmanagment.data.entity.Tool
import com.aircraft.toolmanagment.data.entity.UserLoginRequest
import com.aircraft.toolmanagment.data.entity.UserLoginResponse
import com.aircraft.toolmanagment.data.entity.UserRegisterRequest
import com.aircraft.toolmanagment.data.entity.UserRegisterResponse
import com.aircraft.toolmanagment.data.entity.ApiResponse
import com.aircraft.toolmanagment.data.entity.BorrowReturnRecord
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

// Remote API service interface
interface ApiService {
    @POST("login")
    suspend fun loginUser(@Body request: UserLoginRequest): ApiResponse<UserLoginResponse>

    @POST("register")
    suspend fun registerUser(@Body request: UserRegisterRequest): ApiResponse<UserRegisterResponse>
    
    @GET("user/exists/{identifier}")
    suspend fun checkUserExists(@Path("identifier") identifier: String): ApiResponse<Boolean>
    
    @PUT("user")
    suspend fun updateUserInfo(
        @Query("username") username: String,
        @Body updateRequest: UpdateUserRequest
    ): ApiResponse<Unit>

    @DELETE("user")
    suspend fun deleteUser(@Query("username") username: String): ApiResponse<Unit>
    
    // Tool management endpoints
    @POST("tools")
    suspend fun addTool(@Body toolRequest: AddToolRequest): ApiResponse<Unit>
    
    @GET("tools")
    suspend fun queryTools(@Query("keyword") keyword: String?): ApiResponse<List<Tool>>
    
    @PUT("tools")
    suspend fun updateTool(@Body tool: Tool): ApiResponse<Unit>
    
    @DELETE("tools")
    suspend fun deleteTool(@Query("id") id: Int): ApiResponse<Unit>
    
    @POST("tools/batch")
    suspend fun batchImportTools(@Body tools: List<Tool>): ApiResponse<Unit>

    // Borrow and return endpoints
    @POST("borrow")
    suspend fun borrowTool(@Body borrowRecord: BorrowReturnRecord): ApiResponse<Unit>

    @PUT("return")
    suspend fun returnTool(@Query("recordId") recordId: Int): ApiResponse<Unit>

    @GET("borrow-records")
    suspend fun getBorrowRecords(
        @Query("toolId") toolId: Int? = null,
        @Query("userId") userId: Int? = null
    ): ApiResponse<List<BorrowReturnRecord>>

}