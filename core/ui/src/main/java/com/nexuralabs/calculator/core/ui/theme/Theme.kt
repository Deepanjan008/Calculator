package com.nexuralabs.calculator.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    customColor: Color = Color(0xFFBB86FC),
    content: @Composable () -> Unit,
) {
    val colorScheme = remember(customColor, darkTheme) { buildColorScheme(customColor, darkTheme) }

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

private fun buildColorScheme(seed: Color, darkTheme: Boolean): ColorScheme {
    val secondary = seed.rotateHue(30f)
    val tertiary = seed.rotateHue(-30f)

    return if (darkTheme) {
        darkColorScheme(
            primary = seed.lighten(0.15f),
            onPrimary = onColorFor(seed.lighten(0.15f)),
            primaryContainer = seed.darken(0.35f),
            onPrimaryContainer = seed.lighten(0.6f),
            secondary = secondary.lighten(0.15f),
            onSecondary = onColorFor(secondary.lighten(0.15f)),
            secondaryContainer = secondary.darken(0.35f),
            onSecondaryContainer = secondary.lighten(0.6f),
            tertiary = tertiary.lighten(0.15f),
            onTertiary = onColorFor(tertiary.lighten(0.15f)),
            tertiaryContainer = tertiary.darken(0.35f),
            onTertiaryContainer = tertiary.lighten(0.6f),
            background = lerp(Color(0xFF141218), seed, 0.04f),
            surface = lerp(Color(0xFF141218), seed, 0.04f),
            surfaceVariant = lerp(Color(0xFF2B2930), seed, 0.06f),
            surfaceContainerLowest = lerp(Color(0xFF0F0D13), seed, 0.03f),
            surfaceContainerLow = lerp(Color(0xFF1D1B20), seed, 0.045f),
            surfaceContainer = lerp(Color(0xFF211F26), seed, 0.06f),
            surfaceContainerHigh = lerp(Color(0xFF2B2930), seed, 0.07f),
            surfaceContainerHighest = lerp(Color(0xFF36343B), seed, 0.08f),
        )
    } else {
        lightColorScheme(
            primary = seed.darken(0.05f),
            onPrimary = onColorFor(seed.darken(0.05f)),
            primaryContainer = seed.lighten(0.75f),
            onPrimaryContainer = seed.darken(0.45f),
            secondary = secondary.darken(0.05f),
            onSecondary = onColorFor(secondary.darken(0.05f)),
            secondaryContainer = secondary.lighten(0.75f),
            onSecondaryContainer = secondary.darken(0.45f),
            tertiary = tertiary.darken(0.05f),
            onTertiary = onColorFor(tertiary.darken(0.05f)),
            tertiaryContainer = tertiary.lighten(0.75f),
            onTertiaryContainer = tertiary.darken(0.45f),
            background = lerp(Color(0xFFFFFBFE), seed, 0.025f),
            surface = lerp(Color(0xFFFFFBFE), seed, 0.025f),
            surfaceVariant = lerp(Color(0xFFE7E0EC), seed, 0.05f),
            surfaceContainerLowest = lerp(Color(0xFFFFFFFF), seed, 0.015f),
            surfaceContainerLow = lerp(Color(0xFFF7F2FA), seed, 0.03f),
            surfaceContainer = lerp(Color(0xFFF1ECF4), seed, 0.045f),
            surfaceContainerHigh = lerp(Color(0xFFEBE6EE), seed, 0.06f),
            surfaceContainerHighest = lerp(Color(0xFFE6E0E9), seed, 0.075f),
        )
    }
}

private fun onColorFor(background: Color): Color =
    if (background.luminance() > 0.5f) Color.Black else Color.White

private fun Color.lighten(fraction: Float): Color = lerp(this, Color.White, fraction)
private fun Color.darken(fraction: Float): Color = lerp(this, Color.Black, fraction)

private fun Color.rotateHue(degrees: Float): Color {
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (red * 255).toInt().coerceIn(0, 255),
        (green * 255).toInt().coerceIn(0, 255),
        (blue * 255).toInt().coerceIn(0, 255),
        hsv,
    )
    hsv[0] = (hsv[0] + degrees).let { h -> ((h % 360f) + 360f) % 360f }
    return Color(android.graphics.Color.HSVToColor(alpha.let { (it * 255).toInt().coerceIn(0, 255) }, hsv))
}
