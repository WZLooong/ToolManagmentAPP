package com.aircraft.toolmanagment.data.di

import android.content.Context
import com.aircraft.toolmanagment.data.repository.Repository
import com.aircraft.toolmanagment.network.ApiService
import com.aircraft.toolmanagment.network.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return NetworkModule.apiService
    }
    
    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): Repository {
        return Repository.getInstance(context, apiService)
    }
}