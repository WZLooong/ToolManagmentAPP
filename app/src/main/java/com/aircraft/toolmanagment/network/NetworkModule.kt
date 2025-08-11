package com.aircraft.toolmanagment.network

import com.aircraft.toolmanagment.config.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    // 引用配置文件中的Base URL
    private const val BASE_URL = Config.BASE_URL

    // 配置OkHttpClient以增加超时时间
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // 连接超时时间
        .readTimeout(30, TimeUnit.SECONDS)     // 读取超时时间
        .writeTimeout(30, TimeUnit.SECONDS)    // 写入超时时间
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    // 提供统一的Retrofit实例
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    
    // 用于测试的本地开发环境API服务
    fun getDevApiService(devUrl: String): ApiService {
        val devRetrofit = Retrofit.Builder()
            .baseUrl(devUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return devRetrofit.create(ApiService::class.java)
    }
}