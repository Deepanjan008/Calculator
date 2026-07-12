package com.nexuralabs.calculator.core.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val expression: String,
    val result: String,
    val note: String = "",
)
