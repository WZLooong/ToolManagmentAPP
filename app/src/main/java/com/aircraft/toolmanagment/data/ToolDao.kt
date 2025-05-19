package com.aircraft.toolmanagment.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aircraft.toolmanagment.data.entity.Tool

@Dao
interface ToolDao {
    @Insert
    suspend fun insertTool(tool: Tool)

    @Query("SELECT * FROM Tool WHERE name LIKE '%' || :keyword || '%'")
    suspend fun searchTool(keyword: String): List<Tool>

    @Update
    suspend fun updateTool(tool: Tool)
}