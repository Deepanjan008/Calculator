package com.nexuralabs.calculator.core.domain

/**
 * What one key press does to the expression.
 *
 * Every awkward rule in a calculator keypad is in here, and every one of them is
 * a rule about the *text*, not about the screen: you cannot start an expression
 * with an operator, a second operator replaces the first rather than stacking,
 * a number takes one decimal point, and nothing may follow a square root sign.
 * Keeping them here is what makes them testable, and what stops the answer
 * depending on which button happens to call which lambda.
 */
object ApplyCalculatorKey {

    operator fun invoke(current: String, input: String): String = when (input) {
        CalculatorSymbols.BACKSPACE, "DEL" -> current.dropLast(1)

        CalculatorSymbols.CLEAR -> ""

        CalculatorSymbols.DECIMAL_POINT ->
            if (current.trailingNumber().contains(CalculatorSymbols.DECIMAL_POINT)) current else current + input

        CalculatorSymbols.ROOT ->
            if (current.isEmpty() || current.last().toString() != CalculatorSymbols.ROOT) current + input else current

        CalculatorSymbols.PERCENT ->
            if (current.isNotEmpty() && current.last().isDigit()) current + input else current

        else -> when {
            input in CalculatorSymbols.OPERATORS -> when {
                current.isEmpty() -> current
                current.last().toString() in CalculatorSymbols.OPERATORS -> current.dropLast(1) + input
                current.last().toString() == CalculatorSymbols.ROOT -> current
                else -> current + input
            }

            else -> current + input
        }
    }

    /** The digits since the last operator, which is where a decimal point belongs. */
    private fun String.trailingNumber(): String =
        split(Regex("[+\\-\u00d7\u00f7^%\u221a]")).lastOrNull().orEmpty()
}
