package com.nexuralabs.calculator.core.domain.usecase

import com.nexuralabs.calculator.core.domain.ApplyCalculatorKey
import com.nexuralabs.calculator.core.domain.ExpressionEvaluator
import com.nexuralabs.calculator.core.domain.ResultFormatter
import com.nexuralabs.calculator.core.domain.repository.CalculationHistory
import java.math.BigDecimal

/**
 * The calculator's four operations, as four names.
 *
 * They wrap single calls today, and that is the point: the screen is handed what
 * it can do rather than the engine, so a change to how a result is recorded -
 * adding the vault's item, say - lands in one place instead of at every call
 * site. It is also the seam the tests drive: a state holder built from fakes is
 * built from these, not from a repository.
 */

/** What one key press does to the expression. */
class ApplyKey {
    operator fun invoke(current: String, input: String): String = ApplyCalculatorKey(current, input)
}

/** The answer to an expression, or an exception if there isn't one. */
class EvaluateExpression {
    operator fun invoke(expression: String): BigDecimal = ExpressionEvaluator.evaluate(expression)
}

/** A result, rounded to [precision] decimal places and written the way a person reads it. */
class FormatResult {
    operator fun invoke(value: BigDecimal, precision: Int): String = ResultFormatter.format(value, precision)
}

/** Writes a finished sum into the history. */
class RecordCalculation(private val history: CalculationHistory) {
    suspend operator fun invoke(expression: String, result: String) = history.record(expression, result)
}
