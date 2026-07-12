package com.nexuralabs.calculator.feature.calculator

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.calculatorScreen(navController: NavController) {
    composable(NexuraRoutes.CALCULATOR) { CalculatorScreen(navController) }
}
