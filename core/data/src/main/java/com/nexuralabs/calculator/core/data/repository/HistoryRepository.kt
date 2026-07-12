package com.nexuralabs.calculator.core.data.repository

import com.nexuralabs.calculator.core.data.db.HistoryDao
import com.nexuralabs.calculator.core.data.db.HistoryEntity
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

    suspend fun delete(entity: HistoryEntity) {
        dao.delete(entity)
    }
}
