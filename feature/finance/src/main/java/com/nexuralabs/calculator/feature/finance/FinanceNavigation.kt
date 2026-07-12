package com.nexuralabs.calculator.feature.finance

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.financeScreens(navController: NavController) {
    composable(NexuraRoutes.EMI) { EmiCalculatorScreen(navController) }
    composable(NexuraRoutes.INVESTMENT) { InvestmentScreen(navController) }
    composable(NexuraRoutes.DISCOUNT) { DiscountTaxScreen(navController) }
    composable(NexuraRoutes.FUEL) { FuelCostScreen(navController) }
}
