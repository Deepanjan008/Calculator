package com.deepanjan.neocalculator

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

// Keys
private val THEME_KEY = stringPreferencesKey("theme")
private val PRIMARY_COLOR_KEY = longPreferencesKey("primary_color")
private val SECONDARY_COLOR_KEY = longPreferencesKey("secondary_color")
private val GLASSMORPHISM_KEY = booleanPreferencesKey("glassmorphism")
private val FONT_KEY = stringPreferencesKey("font")

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val dataStore: DataStore<Preferences> = remember { context.dataStore }

    val themeMode by dataStore.data.map { it[THEME_KEY] ?: "light" }.collectAsState(initial = "light")
    val primaryLong by dataStore.data.map { it[PRIMARY_COLOR_KEY] ?: 0xFF6200EE }.collectAsState(initial = 0xFF6200EE)
    val secondaryLong by dataStore.data.map { it[SECONDARY_COLOR_KEY] ?: 0xFF03DAC6 }.collectAsState(initial = 0xFF03DAC6)
    val glassmorphismEnabled by dataStore.data.map { it[GLASSMORPHISM_KEY] ?: false }.collectAsState(initial = false)
    val fontName by dataStore.data.map { it[FONT_KEY] ?: "sans" }.collectAsState(initial = "sans")

    val primary = Color(primaryLong)
    val secondary = Color(secondaryLong)

    val isDarkTheme = when (themeMode) {
        "dark" -> true
        "black" -> true
        else -> isSystemInDarkTheme()
    }

    val baseColorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        isDarkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    val colorScheme = baseColorScheme.copy(
        primary = primary,
        secondary = secondary,
        background = if (themeMode == "black") Color.Black else baseColorScheme.background,
        surface = if (themeMode == "black") Color.Black else baseColorScheme.surface,
        onBackground = if (themeMode == "black") Color.White else baseColorScheme.onBackground,
        onSurface = if (themeMode == "black") Color.White else baseColorScheme.onSurface
    )

    val typography = MaterialTheme.typography.let { typo ->
        when (fontName) {
            "serif" -> typo.copy(
                displayLarge = typo.displayLarge.copy(fontFamily = FontFamily.Serif),
                headlineLarge = typo.headlineLarge.copy(fontFamily = FontFamily.Serif)
            )
            "monospace" -> typo.copy(
                displayLarge = typo.displayLarge.copy(fontFamily = FontFamily.Monospace),
                headlineLarge = typo.headlineLarge.copy(fontFamily = FontFamily.Monospace)
            )
            else -> typo 
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
