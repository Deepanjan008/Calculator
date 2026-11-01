package com.nexuralabs.calculator.core.data.repository

import com.nexuralabs.calculator.core.database.db.HistoryDao
import com.nexuralabs.calculator.core.domain.repository.CalculationHistory
import com.nexuralabs.calculator.core.database.db.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepository @Inject constructor(private val dao: HistoryDao) : CalculationHistory {

    /**
     * The half of this repository the calculator screen is allowed to see: it
     * writes a finished sum down. Reading, editing, deleting and clearing stay
     * here for the history screen, which is the screen that should have them.
     */
    override suspend fun record(expression: String, result: String) = insert(expression, result)

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
