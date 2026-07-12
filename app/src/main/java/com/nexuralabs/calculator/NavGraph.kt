package com.nexuralabs.calculator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.nexuralabs.calculator.core.navigation.NexuraRoutes
import com.nexuralabs.calculator.feature.calculator.calculatorScreen
import com.nexuralabs.calculator.feature.converter.converterScreens
import com.nexuralabs.calculator.feature.finance.financeScreens
import com.nexuralabs.calculator.feature.history.historyScreen
import com.nexuralabs.calculator.feature.settings.settingsScreen
import com.nexuralabs.calculator.feature.tools.toolsScreens

/**
 * The only file in :app that knows every feature module exists. Each feature only knows the
 * plain route strings from :core:navigation - none of them reference each other directly.
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NexuraRoutes.CALCULATOR) {
        calculatorScreen(navController)
        converterScreens(navController)
        financeScreens(navController)
        toolsScreens(navController)
        historyScreen(navController)
        settingsScreen(navController)
    }
}
