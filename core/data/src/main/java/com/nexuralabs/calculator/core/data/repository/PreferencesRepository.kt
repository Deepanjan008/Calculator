package com.nexuralabs.calculator.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val PRECISION = intPreferencesKey("precision")
        val HAPTIC = booleanPreferencesKey("haptic")
        val THEME_MODE = stringPreferencesKey("theme")
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    val precision: Flow<Int> = dataStore.data.map { it[Keys.PRECISION] ?: 6 }
    val hapticEnabled: Flow<Boolean> = dataStore.data.map { it[Keys.HAPTIC] ?: true }
    val themeMode: Flow<String> = dataStore.data.map { it[Keys.THEME_MODE] ?: "system" }
    val themeColorHex: Flow<String> = dataStore.data.map { it[Keys.THEME_COLOR] ?: "#BB86FC" }

    suspend fun setPrecision(value: Int) {
        dataStore.edit { it[Keys.PRECISION] = value }
    }

    suspend fun setHapticEnabled(value: Boolean) {
        dataStore.edit { it[Keys.HAPTIC] = value }
    }

    suspend fun setThemeMode(value: String) {
        dataStore.edit { it[Keys.THEME_MODE] = value }
    }

    suspend fun setThemeColorHex(value: String) {
        dataStore.edit { it[Keys.THEME_COLOR] = value }
    }
}
