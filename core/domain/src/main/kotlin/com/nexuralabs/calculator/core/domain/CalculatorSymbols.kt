package com.nexuralabs.calculator.core.domain

/**
 * The characters the keypad can put into an expression, in one place.
 *
 * They are here rather than scattered through the evaluator, the input rules and
 * the screen because all three have to agree on them: the evaluator decides what
 * an operator is worth, the input rules decide when one may be typed, and the
 * screen decides what the key says. Three copies of `"√"` is three places to
 * miss one.
 */
object CalculatorSymbols {

    /** Binary and prefix operators. */
    val OPERATORS = setOf("+", "-", "*", "/", "\u00d7", "\u00f7", "\u2212", "^")

    /** The square root, which is prefix rather than infix. */
    const val ROOT = "\u221a"

    /** How a subtraction that starts an expression is spelled internally. */
    const val UNARY_MINUS = "u-"

    /** Percent, which divides what precedes it by a hundred. */
    const val PERCENT = "%"

    /** Erase the last character. */
    const val BACKSPACE = "\u232b"

    /** Erase everything. */
    const val CLEAR = "C"

    /** Ask for the answer. */
    const val EQUALS = "="

    /** The character that separates a whole number from its fraction. */
    const val DECIMAL_POINT = "."
}
