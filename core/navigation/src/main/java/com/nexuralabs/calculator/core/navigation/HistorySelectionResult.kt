package com.nexuralabs.calculator.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * Key used on [androidx.lifecycle.SavedStateHandle] to pass the expression the user tapped
 * in History back to the Calculator screen. This replaces directly grabbing another
 * feature's Hilt ViewModel instance (which was silently broken - Navigation-Compose scopes
 * hiltViewModel() per destination, so writing to it from History never reached the real
 * Calculator screen). Passing a plain String result through the NavController keeps
 * feature:history and feature:calculator fully decoupled.
 */
const val HISTORY_SELECTED_EXPRESSION_KEY = "history_selected_expression"

fun NavController.returnSelectedExpressionToCalculator(expression: String) {
    previousBackStackEntry
        ?.savedStateHandle
        ?.set(HISTORY_SELECTED_EXPRESSION_KEY, expression)
}

/**
 * Call from the Calculator screen. Invokes [onExpressionSelected] exactly once whenever a
 * new value arrives on the back stack entry's SavedStateHandle, then clears it so popping
 * back to this screen again doesn't replay the same value.
 */
@Composable
fun ObserveHistorySelection(
    navController: NavController,
    onExpressionSelected: (String) -> Unit,
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val selected = savedStateHandle?.get<String>(HISTORY_SELECTED_EXPRESSION_KEY)

    LaunchedEffect(selected) {
        if (selected != null) {
            onExpressionSelected(selected)
            savedStateHandle.remove<String>(HISTORY_SELECTED_EXPRESSION_KEY)
        }
    }
}
