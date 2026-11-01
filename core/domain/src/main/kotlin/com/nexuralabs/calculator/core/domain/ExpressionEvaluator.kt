package com.nexuralabs.calculator.core.domain

import java.math.BigDecimal
import java.math.MathContext
import java.util.ArrayDeque
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * The arithmetic, with no Android and no screen anywhere near it.
 *
 * Shunting-yard into reverse Polish, then a stack machine: the shortest route
 * from an infix string to an answer that also gets precedence and associativity
 * right without a parser generator. `2+3*4` is 14, `2^10` is 1024, `-5+3` is -2
 * because a minus with nothing to its left is unary rather than subtraction, and
 * `2+50%` is 2.5 because percent divides what precedes it.
 *
 * Division and multiplication stay in [BigDecimal], so `0.1+0.2` is 0.3 rather
 * than the binary float that isn't. The two places that go through [Double] -
 * powers and roots - do so because the platform has no exact decimal version of
 * either, and the formatter rounds what they return.
 *
 * This used to be a private method of a ViewModel, which is why none of it could
 * be tested without a device. Every rule in the class doc above is asserted in
 * `ExpressionEvaluatorTest`.
 */
object ExpressionEvaluator {

    private val HUNDRED = BigDecimal(100)

    private val PRECEDENCE = mapOf(
        "+" to 1,
        "-" to 1,
        "*" to 2,
        "/" to 2,
        "^" to 3,
        CalculatorSymbols.UNARY_MINUS to 4,
        CalculatorSymbols.ROOT to 4
    )

    private val RIGHT_ASSOCIATIVE = setOf(CalculatorSymbols.UNARY_MINUS, CalculatorSymbols.ROOT)

    /**
     * Evaluates [expression], or throws.
     *
     * @throws ArithmeticException when the expression divides by zero.
     * @throws NumberFormatException when it contains a number that cannot be read.
     */
    fun evaluate(expression: String): BigDecimal {
        val values = ArrayDeque<BigDecimal>()
        val operators = ArrayDeque<String>()
        var lastWasOperator = true

        for (token in tokenize(expression.normalised())) {
            val mapped = if (token == "-" && lastWasOperator) CalculatorSymbols.UNARY_MINUS else token
            when {
                mapped.toBigDecimalOrNull() != null -> {
                    values.addLast(mapped.toBigDecimal())
                    lastWasOperator = false
                }

                mapped == CalculatorSymbols.PERCENT -> {
                    if (values.isNotEmpty()) {
                        values.addLast(values.removeLast().divide(HUNDRED, MathContext.DECIMAL128))
                    }
                    lastWasOperator = false
                }

                PRECEDENCE.containsKey(mapped) -> {
                    val rightAssociative = mapped in RIGHT_ASSOCIATIVE
                    val precedence = PRECEDENCE.getValue(mapped)
                    while (operators.isNotEmpty()) {
                        val top = operators.last()
                        val topPrecedence = PRECEDENCE.getOrDefault(top, 0)
                        if (topPrecedence > precedence || (!rightAssociative && topPrecedence == precedence)) {
                            apply(values, operators.removeLast())
                        } else {
                            break
                        }
                    }
                    operators.addLast(mapped)
                    lastWasOperator = true
                }
            }
        }

        while (operators.isNotEmpty()) apply(values, operators.removeLast())
        return values.firstOrNull() ?: BigDecimal.ZERO
    }

    private fun String.normalised(): String = replace("\u00d7", "*").replace("\u00f7", "/").replace("\u2212", "-")

    private fun tokenize(expression: String): List<String> {
        val tokens = mutableListOf<String>()
        val number = StringBuilder()
        for (character in expression) {
            if (character.isDigit() || character == '.') {
                number.append(character)
            } else {
                if (number.isNotEmpty()) {
                    tokens.add(number.toString())
                    number.clear()
                }
                tokens.add(character.toString())
            }
        }
        if (number.isNotEmpty()) tokens.add(number.toString())
        return tokens
    }

    private fun apply(values: ArrayDeque<BigDecimal>, operator: String) {
        if (operator == CalculatorSymbols.ROOT) {
            if (values.isEmpty()) return
            values.addLast(BigDecimal(sqrt(values.removeLast().toDouble())))
            return
        }
        if (operator == CalculatorSymbols.UNARY_MINUS) {
            if (values.isEmpty()) return
            values.addLast(values.removeLast().negate())
            return
        }
        if (values.size < 2) return

        val right = values.removeLast()
        val left = values.removeLast()
        values.addLast(
            when (operator) {
                "+" -> left.add(right)
                "-" -> left.subtract(right)
                "*" -> left.multiply(right)
                "/" -> if (right.compareTo(BigDecimal.ZERO) == 0) {
                    throw ArithmeticException("Division by zero")
                } else {
                    left.divide(right, MathContext.DECIMAL128)
                }

                "^" -> BigDecimal(left.toDouble().pow(right.toDouble()))
                else -> BigDecimal.ZERO
            }
        )
    }
}
