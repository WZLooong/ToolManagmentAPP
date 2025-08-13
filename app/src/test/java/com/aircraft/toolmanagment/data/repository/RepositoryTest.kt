package com.aircraft.toolmanagment.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.aircraft.toolmanagment.network.ApiService
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class RepositoryTest {
    private lateinit var context: Context
    private lateinit var apiService: ApiService
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        apiService = mock(ApiService::class.java)
        repository = Repository.getInstance(context, apiService)
    }

    @Test
    fun testRepositorySingleton() {
        val repository1 = Repository.getInstance(context, apiService)
        val repository2 = Repository.getInstance(context, apiService)
        
        assertNotNull(repository1)
        assertNotNull(repository2)
        assert(repository1 === repository2)
    }

    @Test
    fun testUserRepositoryInitialization() {
        assertNotNull(repository.userRepo)
    }

    @Test
    fun testToolRepositoryInitialization() {
        assertNotNull(repository.toolRepo)
    }

    @Test
    fun testBorrowReturnRepositoryInitialization() {
        assertNotNull(repository.borrowReturnRepo)
    }
}