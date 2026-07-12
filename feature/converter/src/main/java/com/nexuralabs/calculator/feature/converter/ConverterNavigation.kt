package com.nexuralabs.calculator.feature.converter

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nexuralabs.calculator.core.navigation.NexuraRoutes

fun NavGraphBuilder.converterScreens(navController: NavController) {
    composable(NexuraRoutes.UNIT_CONVERTER) { UnitConverterScreen(navController) }

    composable(
        route = NexuraRoutes.CONVERTER_DETAIL,
        arguments = listOf(navArgument(NexuraRoutes.CONVERTER_DETAIL_ARG) { type = NavType.StringType })
    ) { backStackEntry ->
        val category = backStackEntry.arguments?.getString(NexuraRoutes.CONVERTER_DETAIL_ARG) ?: ""
        GenericConverterDetailScreen(navController, category)
    }

    composable(NexuraRoutes.CURRENCY) { CurrencyScreen(navController) }
    composable(NexuraRoutes.LAND) { LandConverterScreen(navController) }
    composable(NexuraRoutes.UNIT_PRICE) { UnitPriceScreen(navController) }
}
