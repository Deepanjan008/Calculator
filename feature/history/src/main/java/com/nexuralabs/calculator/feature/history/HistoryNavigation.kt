package com.nexuralabs.calculator.feature.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.historyScreen(navController: NavController) {
    composable(NexuraRoutes.HISTORY) { HistoryScreen(navController) }
}
