package com.deepanjan.neocalculator.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepanjan.neocalculator.data.HistoryEntity
import com.deepanjan.neocalculator.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoryRepository
) : ViewModel() {

    // এটাকে MutableState করা হলো যাতে UI আপডেট হতে পারে
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
            loadHistory() // ডিলিট করার পর লিস্ট রিফ্রেশ
        }
    }

    fun clearAll() {
        viewModelScope.launch { 
            repository.clearAll()
            loadHistory() // ক্লিয়ার করার পর লিস্ট রিফ্রেশ
        }
    }
}
