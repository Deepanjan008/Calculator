package com.nexuralabs.calculator.repository

import com.nexuralabs.calculator.data.HistoryDao
import com.nexuralabs.calculator.data.HistoryEntity
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val dao: HistoryDao) {
    suspend fun insert(expression: String, result: String, note: String = "") {
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

    // This new function was added for single deletion!
    suspend fun delete(entity: HistoryEntity) {
        dao.delete(entity)
    }
}
