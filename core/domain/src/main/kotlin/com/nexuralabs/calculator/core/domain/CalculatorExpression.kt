package com.nexuralabs.calculator.core.domain

/**
 * What counts as a finished expression.
 *
 * A trailing operator is not a question, it is a sentence someone is still
 * writing: `2+` has no answer, and asking for one used to mean an exception
 * caught two layers away and shown as "Error". The rule lives here so the
 * preview and the equals key cannot disagree about when there is something to
 * evaluate.
 */
object CalculatorExpression {

    /** True when [expression] ends in something that can be evaluated. */
    fun isComplete(expression: String): Boolean {
        if (expression.isEmpty()) return false
        val last = expression.last()
        return last.toString() !in CalculatorSymbols.OPERATORS && last.toString() != CalculatorSymbols.ROOT
    }
}
