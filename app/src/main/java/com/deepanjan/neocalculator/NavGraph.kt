package com.deepanjan.neocalculator

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.deepanjan.neocalculator.ui.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "calculator") {
        composable("calculator") { CalculatorScreen(navController) }
        composable("bmi") { BmiHealthScreen(navController) }
        composable("investment") { InvestmentScreen(navController) }
        composable("fuel") { FuelCostScreen(navController) }
        composable("unit_price") { UnitPriceScreen(navController) }
        composable("gpa") { GpaCalculatorScreen(navController) }
        composable("currency") { CurrencyScreen(navController) }
        composable("age") { AgeCalculatorScreen(navController) }
        composable("emi") { EmiCalculatorScreen(navController) }
        composable("solver") { EquationSolverScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("factorial") { FactorialScreen(navController) }
        composable("discount") { DiscountTaxScreen(navController) }
        composable("land") { LandConverterScreen(navController) }

        // নতুন Unit Converter রুটগুলো এখানে যোগ করা হলো
        composable("unit_converter") { UnitConverterScreen(navController) }
        composable("converter_detail/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Length"
            GenericConverterDetailScreen(navController, category)
        }
    }
}
