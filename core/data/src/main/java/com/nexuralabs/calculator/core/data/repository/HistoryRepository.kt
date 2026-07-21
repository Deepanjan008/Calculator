package com.nexuralabs.calculator.core.data.repository

import com.nexuralabs.calculator.core.data.db.HistoryDao
import com.nexuralabs.calculator.core.data.db.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val dao: HistoryDao) {
    suspend fun insert(expression: String, result: String, note: String = "") = withContext(Dispatchers.IO) {
        dao.insert(HistoryEntity(expression = expression, result = result, note = note))
    }

    suspend fun update(entity: HistoryEntity) = withContext(Dispatchers.IO) {
        dao.update(entity)
    }

    suspend fun getAll(): List<HistoryEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        dao.clearAll()
    }

    suspend fun delete(entity: HistoryEntity) = withContext(Dispatchers.IO) {
        dao.delete(entity)
    }
}
