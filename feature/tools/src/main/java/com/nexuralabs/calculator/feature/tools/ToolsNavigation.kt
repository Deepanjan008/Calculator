package com.nexuralabs.calculator.feature.tools

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.toolsScreens(navController: NavController) {
    composable(NexuraRoutes.BMI) { BmiHealthScreen(navController) }
    composable(NexuraRoutes.AGE) { AgeCalculatorScreen(navController) }
    composable(NexuraRoutes.GPA) { GpaCalculatorScreen(navController) }
    composable(NexuraRoutes.FACTORIAL) { FactorialScreen(navController) }
    composable(NexuraRoutes.SOLVER) { EquationSolverScreen(navController) }
}
