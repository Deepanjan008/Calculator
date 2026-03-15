package com.deepanjan.neocalculator.repository

import com.deepanjan.neocalculator.data.HistoryDao
import com.deepanjan.neocalculator.data.HistoryEntity
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val dao: HistoryDao) {
    suspend fun insert(expression: String, result: String, note: String) {
        dao.insert(HistoryEntity(expression = expression, result = result, note = note))
    }

    suspend fun update(entity: HistoryEntity) {
        dao.update(entity)
    }

    suspend fun getAll(): List<HistoryEntity> {
        return dao.getAll()
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    // এই নতুন ফাংশনটি যোগ করা হলো সিঙ্গেল ডিলিটের জন্য!
    suspend fun delete(entity: HistoryEntity) {
        dao.delete(entity)
    }
}
