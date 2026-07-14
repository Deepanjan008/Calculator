package com.nexuralabs.calculator.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexuralabs.calculator.data.HistoryEntity
import com.nexuralabs.calculator.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    // Made MutableState so UI updates correctly
    private val _history = mutableStateOf<List<HistoryEntity>>(emptyList())
    val history: State<List<HistoryEntity>> = _history

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _history.value = repository.getAll()
        }
    }

    fun deleteItem(item: HistoryEntity) {
        viewModelScope.launch { 
            repository.delete(item)
            loadHistory() // Refresh list after deletion
        }
    }

    fun clearAll() {
        viewModelScope.launch { 
            repository.clearAll()
            loadHistory() // Refresh list after clearing
        }
    }
}
