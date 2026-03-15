package com.deepanjan.neocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.navigation.compose.rememberNavController
import com.deepanjan.neocalculator.ui.theme.AppTheme
import com.deepanjan.neocalculator.ui.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map

private val THEME_KEY = stringPreferencesKey("theme")
private val COLOR_KEY = stringPreferencesKey("theme_color")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val themeMode by context.dataStore.data.map { it[THEME_KEY] ?: "system" }.collectAsState(initial = "system")
            val colorHex by context.dataStore.data.map { it[COLOR_KEY] ?: "#BB86FC" }.collectAsState(initial = "#BB86FC")

            val isDarkTheme = when (themeMode.lowercase()) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            // হেক্স কোড থেকে কালার তৈরি (সেফ মেথড)
            val customThemeColor = remember(colorHex) {
                try { Color(android.graphics.Color.parseColor(colorHex)) } 
                catch (e: Exception) { Color(0xFFBB86FC) }
            }

            AppTheme(darkTheme = isDarkTheme, customColor = customThemeColor) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavGraph(navController = rememberNavController())
                }
            }
        }
    }
}
