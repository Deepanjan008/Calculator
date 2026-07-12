package com.nexuralabs.calculator.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(NexuraRoutes.SETTINGS) { SettingsScreen(navController) }
}
