package com.deepanjan.neocalculator.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    customColor: Color = Color(0xFFBB86FC), // ডিফল্ট পার্পল
    content: @Composable () -> Unit
) {
    // ইউজারের দেওয়া কালার থেকে ডাইনামিক স্কিম তৈরি
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = customColor,
            secondary = customColor.copy(alpha = 0.7f),
            tertiary = customColor.copy(alpha = 0.5f),
            onPrimary = Color.Black
        )
    } else {
        lightColorScheme(
            primary = customColor,
            secondary = customColor.copy(alpha = 0.8f),
            tertiary = customColor.copy(alpha = 0.6f),
            onPrimary = Color.White
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
