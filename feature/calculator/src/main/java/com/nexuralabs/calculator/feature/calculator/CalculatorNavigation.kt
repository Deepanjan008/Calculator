package com.nexuralabs.calculator.feature.calculator

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.calculatorScreen(
    navController: NavController,
    unitConverterSheet: @Composable ((onDismiss: () -> Unit) -> Unit)
) {
    composable(NexuraRoutes.CALCULATOR) {
        CalculatorScreen(
            navController = navController,
            unitConverterSheet = unitConverterSheet
        )
    }
}
