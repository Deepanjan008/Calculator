package com.nexuralabs.calculator.core.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: HistoryEntity)

    @Update
    suspend fun update(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    suspend fun getAll(): List<HistoryEntity>

    @Query("DELETE FROM history")
    suspend fun clearAll()

    @Delete
    suspend fun delete(history: HistoryEntity)
}
