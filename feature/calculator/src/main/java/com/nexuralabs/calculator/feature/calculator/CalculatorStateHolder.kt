package com.nexuralabs.calculator.feature.calculator

import com.nexuralabs.calculator.core.domain.CalculatorExpression
import com.nexuralabs.calculator.core.domain.CalculatorSymbols
import com.nexuralabs.calculator.core.domain.repository.CalculatorPreferences
import com.nexuralabs.calculator.core.domain.usecase.ApplyKey
import com.nexuralabs.calculator.core.domain.usecase.EvaluateExpression
import com.nexuralabs.calculator.core.domain.usecase.FormatResult
import com.nexuralabs.calculator.core.domain.usecase.RecordCalculation
import com.nexuralabs.calculator.core.presentation.StateHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * What the calculator decides, with nothing about drawing it.
 *
 * Three rules are worth stating because they are what the split buys:
 *
 * 1. A key press changes the expression through [ApplyKey], never by string
 *    surgery at the call site, so "you cannot start with an operator" is a rule
 *    in one place instead of a habit in twenty.
 * 2. The preview is only ever computed from a complete expression. `2+` has no
 *    answer, and showing one - or leaving the previous one there - is how a
 *    calculator lies quietly.
 * 3. The answer is written to the history before the display is replaced, so a
 *    sum that was recorded and a sum that was shown are the same sum.
 */
class CalculatorStateHolder(
    private val applyKey: ApplyKey,
    private val evaluateExpression: EvaluateExpression,
    private val formatResult: FormatResult,
    private val recordCalculation: RecordCalculation,
    private val preferences: CalculatorPreferences
) : StateHolder<CalculatorUiState, CalculatorUiEvent, CalculatorUiEffect>(CalculatorUiState()) {

    /**
     * Follows the two settings this screen reads for as long as it is on the
     * stack. Suspends until the flows end, so the host collects it from a scope
     * of its own and cancellation is what stops it.
     */
    suspend fun observePreferences(): Unit = coroutineScope {
        launch { preferences.precision.collect { value -> update { it.copy(precision = value) } } }
        launch { preferences.hapticEnabled.collect { value -> update { it.copy(hapticEnabled = value) } } }
    }

    override suspend fun onEvent(event: CalculatorUiEvent) {
        when (event) {
            is CalculatorUiEvent.KeyPressed -> press(event.input)
            is CalculatorUiEvent.ExpressionLoaded -> {
                update { it.copy(expression = event.expression) }
                refreshPreview()
            }
        }
    }

    private suspend fun press(input: String) {
        if (input == CalculatorSymbols.EQUALS) {
            evaluate()
            return
        }
        update { it.copy(expression = applyKey(currentState.expression, input)) }
        refreshPreview()
    }

    private suspend fun evaluate() {
        val expression = currentState.expression
        if (!CalculatorExpression.isComplete(expression)) return

        val answer = compute(expression)
        if (answer == null) {
            update { it.copy(preview = ERROR) }
            return
        }

        recordCalculation(expression, answer)
        update { it.copy(expression = answer, preview = "") }
    }

    private suspend fun refreshPreview() {
        val expression = currentState.expression
        if (!CalculatorExpression.isComplete(expression)) {
            update { it.copy(preview = "") }
            return
        }
        update { it.copy(preview = compute(expression).orEmpty()) }
    }

    /** The formatted answer, or null when the expression has none. */
    private suspend fun compute(expression: String): String? = withContext(Dispatchers.Default) {
        try {
            formatResult(evaluateExpression(expression), currentState.precision)
        } catch (_: ArithmeticException) {
            null
        } catch (_: NumberFormatException) {
            null
        }
    }

    private companion object {
        const val ERROR = "Error"
    }
}
