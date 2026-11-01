package com.nexuralabs.calculator.core.domain

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * The keypad's awkward rules, each one asserted rather than trusted.
 *
 * These are the rules that used to live in a `when` inside a ViewModel's event
 * handler, where the only way to check one was to tap it.
 */
class ApplyCalculatorKeyTest {

    @Test
    fun `a digit is appended`() {
        assertEquals("12", ApplyCalculatorKey("1", "2"))
        assertEquals("1.", ApplyCalculatorKey("1", "."))
    }

    @Test
    fun `an expression cannot start with an operator`() {
        assertEquals("", ApplyCalculatorKey("", "+"))
        assertEquals("", ApplyCalculatorKey("", "^"))
    }

    @Test
    fun `a second operator replaces the first rather than stacking`() {
        assertEquals("2*", ApplyCalculatorKey("2+", "*"))
        assertEquals("2/", ApplyCalculatorKey("2+", "/"))
    }

    @Test
    fun `nothing may follow a root sign`() {
        assertEquals("\u221a", ApplyCalculatorKey("\u221a", "+"))
        assertEquals("\u221a", ApplyCalculatorKey("\u221a", "\u221a"))
        assertEquals("\u221a9", ApplyCalculatorKey("\u221a", "9"))
    }

    @Test
    fun `a number takes one decimal point`() {
        assertEquals("1.5", ApplyCalculatorKey("1.5", "."))
        assertEquals("1.5+2.", ApplyCalculatorKey("1.5+2", "."))
    }

    @Test
    fun `percent may only follow a digit`() {
        assertEquals("50%", ApplyCalculatorKey("50", "%"))
        assertEquals("50+", ApplyCalculatorKey("50+", "%"))
    }

    @Test
    fun `backspace removes one character and clear removes all of them`() {
        assertEquals("12", ApplyCalculatorKey("123", "\u232b"))
        assertEquals("", ApplyCalculatorKey("", "\u232b"))
        assertEquals("", ApplyCalculatorKey("123+4", "C"))
    }
}
