package com.nexuralabs.calculator.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * The app's single theme. There used to be two competing AppTheme composables (one reading
 * colors from DataStore directly, one taking a customColor parameter) - only this one was ever
 * actually wired up from MainActivity, so the other was silently dead. This version keeps the
 * parameter-based approach (caller reads DataStore, passes the resolved color down) since that's
 * what real navigation used, and now also applies the custom Typography that was defined but
 * never actually passed to MaterialTheme before.
 */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    customColor: Color = Color(0xFFBB86FC),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = customColor,
            secondary = customColor.copy(alpha = 0.7f),
            tertiary = customColor.copy(alpha = 0.5f),
            onPrimary = Color.Black,
        )
    } else {
        lightColorScheme(
            primary = customColor,
            secondary = customColor.copy(alpha = 0.8f),
            tertiary = customColor.copy(alpha = 0.6f),
            onPrimary = Color.White,
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
        typography = Typography,
        content = content,
    )
}
