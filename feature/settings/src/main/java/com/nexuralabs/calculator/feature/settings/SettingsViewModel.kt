package com.nexuralabs.calculator.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexuralabs.calculator.core.data.repository.HistoryRepository
import com.nexuralabs.calculator.core.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings used to grab HistoryViewModel via hiltViewModel() just to call clearAll() - a needless
 * dependency on the entire History feature. This ViewModel talks to HistoryRepository directly
 * instead, and wraps PreferencesRepository so the screen has one clean typed API instead of
 * touching raw DataStore keys (which is also where the old duplicate "val Context.dataStore"
 * declaration used to live).
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    val precision = preferencesRepository.precision
    val hapticEnabled = preferencesRepository.hapticEnabled
    val themeColorHex = preferencesRepository.themeColorHex

    fun setThemeColorHex(hex: String) {
        viewModelScope.launch { preferencesRepository.setThemeColorHex(hex) }
    }

    fun setPrecision(value: Int) {
        viewModelScope.launch { preferencesRepository.setPrecision(value) }
    }

    fun setHapticEnabled(enabled: Boolean) {
        viewModelScope.launch { preferencesRepository.setHapticEnabled(enabled) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { historyRepository.clearAll() }
    }
}
