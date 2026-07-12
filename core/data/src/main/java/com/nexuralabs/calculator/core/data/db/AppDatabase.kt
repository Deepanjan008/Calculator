package com.nexuralabs.calculator.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
