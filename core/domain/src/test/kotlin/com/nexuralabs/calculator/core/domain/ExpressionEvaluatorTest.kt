package com.nexuralabs.calculator.core.domain

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * The arithmetic, pinned.
 *
 * Every case here is a rule the keypad relies on and that nothing else in the
 * app can check: precedence, right associativity, unary minus, the prefix root,
 * the postfix percent, and the one input that has no answer at all.
 */
class ExpressionEvaluatorTest {

    private fun evaluate(expression: String): String = ExpressionEvaluator.evaluate(expression).toPlainString()

    @Test
    fun `addition and subtraction read left to right`() {
        assertEquals("5", evaluate("2+3"))
        assertEquals("2", evaluate("5-3"))
        assertEquals("4", evaluate("7-2-1"))
    }

    @Test
    fun `multiplication binds tighter than addition`() {
        assertEquals("14", evaluate("2+3*4"))
        assertEquals("50", evaluate("2+3*4^2"))
    }

    @Test
    fun `division keeps its exact decimal value`() {
        assertEquals("2.5", evaluate("10/4"))
        assertEquals("0.3", evaluate("0.1+0.2"))
    }

    @Test
    fun `powers are right associative`() {
        assertEquals("1024", evaluate("2^10"))
    }

    @Test
    fun `a leading minus is a sign rather than a subtraction`() {
        assertEquals("-2", evaluate("-5+3"))
    }

    @Test
    fun `the root sign takes the value after it`() {
        assertEquals("3", evaluate("\u221a9"))
    }

    @Test
    fun `percent divides what precedes it by a hundred`() {
        assertEquals("0.5", evaluate("50%"))
        assertEquals("2.5", evaluate("2+50%"))
    }

    @Test
    fun `the symbols the keypad shows are the ones the engine understands`() {
        assertEquals("5", evaluate("2\u00d73-1"))
        assertEquals("5", evaluate("10\u00f74"))
        assertEquals("2", evaluate("5\u22123"))
    }

    @Test
    fun `dividing by zero has no answer`() {
        assertThrows(ArithmeticException::class.java) { ExpressionEvaluator.evaluate("1/0") }
    }

    @Test
    fun `a result keeps the decimal it was given`() {
        assertEquals(BigDecimal("2.5"), ExpressionEvaluator.evaluate("10/4"))
    }
}
