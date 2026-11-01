package com.nexuralabs.calculator.feature.calculator

import com.nexuralabs.calculator.core.domain.ResultFormatter
import com.nexuralabs.calculator.core.domain.repository.CalculationHistory
import com.nexuralabs.calculator.core.domain.repository.CalculatorPreferences
import com.nexuralabs.calculator.core.domain.usecase.ApplyKey
import com.nexuralabs.calculator.core.domain.usecase.EvaluateExpression
import com.nexuralabs.calculator.core.domain.usecase.FormatResult
import com.nexuralabs.calculator.core.domain.usecase.RecordCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * What the calculator decides, with no screen in the way.
 *
 * The point of pulling the decisions out of the ViewModel is exactly this file:
 * every rule below is asserted by calling the same entry point the keypad calls,
 * with no emulator, no database and no Compose test rule in sight.
 */
class CalculatorStateHolderTest {

    private val history = FakeHistory()
    private val preferences = FakePreferences()

    private fun newHolder() = CalculatorStateHolder(
        applyKey = ApplyKey(),
        evaluateExpression = EvaluateExpression(),
        formatResult = FormatResult(),
        recordCalculation = RecordCalculation(history),
        preferences = preferences
    )

    private fun CalculatorStateHolder.type(vararg keys: String) = runBlocking {
        keys.forEach { onEvent(CalculatorUiEvent.KeyPressed(it)) }
    }

    @Test
    fun `keys build an expression and the preview follows it`() = runBlocking {
        val holder = newHolder()

        holder.type("2", "+", "3", "*", "4")

        assertEquals("2+3*4", holder.state.value.expression)
        assertEquals("14", holder.state.value.preview)
    }

    @Test
    fun `an unfinished expression has no preview rather than a stale one`() = runBlocking {
        val holder = newHolder()

        holder.type("2", "+", "3", "+")

        assertEquals("2+3+", holder.state.value.expression)
        assertEquals("", holder.state.value.preview)
    }

    @Test
    fun `equals replaces the expression with the answer and writes it down`() = runBlocking {
        val holder = newHolder()

        holder.type("2", "+", "3", "=")

        assertEquals("5", holder.state.value.expression)
        assertEquals("", holder.state.value.preview)
        assertEquals(listOf("2+3" to "5"), history.recorded)
    }

    @Test
    fun `equals on an unfinished expression does nothing at all`() = runBlocking {
        val holder = newHolder()

        holder.type("2", "+", "=")

        assertEquals("2+", holder.state.value.expression)
        assertTrue("nothing may be recorded for a sum that has no answer", history.recorded.isEmpty())
    }

    @Test
    fun `dividing by zero says so and records nothing`() = runBlocking {
        val holder = newHolder()

        holder.type("1", "/", "0", "=")

        assertEquals("Error", holder.state.value.preview)
        assertEquals("1/0", holder.state.value.expression)
        assertTrue(history.recorded.isEmpty())
    }

    @Test
    fun `the precision setting decides how the answer is written`() = runBlocking {
        val holder = newHolder()
        preferences.precisionValue = 2

        holder.type("1", "0", "0", "/", "3", "=")

        assertEquals("33.33", holder.state.value.expression)
    }

    @Test
    fun `an expression loaded from the history arrives with its preview`() = runBlocking {
        val holder = newHolder()

        holder.onEvent(CalculatorUiEvent.ExpressionLoaded("7*6"))

        assertEquals("7*6", holder.state.value.expression)
        assertEquals("42", holder.state.value.preview)
    }

    @Test
    fun `the settings the screen reads come from the preferences contract`() = runBlocking {
        val holder = newHolder()
        assertEquals(ResultFormatter.DEFAULT_PRECISION, holder.state.value.precision)
        assertTrue(holder.state.value.hapticEnabled)
    }

    private class FakeHistory : CalculationHistory {
        val recorded = mutableListOf<Pair<String, String>>()
        override suspend fun record(expression: String, result: String) {
            recorded += expression to result
        }
    }

    private class FakePreferences : CalculatorPreferences {
        var precisionValue: Int = ResultFormatter.DEFAULT_PRECISION
        override val precision: Flow<Int> get() = flowOf(precisionValue)
        override val hapticEnabled: Flow<Boolean> get() = flowOf(true)
    }
}
