package com.nexuralabs.calculator.core.domain

import java.math.BigDecimal
import org.junit.Assert.assertEquals
import org.junit.Test

/** How an answer is written, which is a separate decision from what it is. */
class ResultFormatterTest {

    @Test
    fun `a whole number is shown without a decimal tail`() {
        assertEquals("4", ResultFormatter.format(BigDecimal("4.000000"), 6))
        assertEquals("1024", ResultFormatter.format(BigDecimal("1024"), 6))
    }

    @Test
    fun `a fraction is shown to the precision the user chose`() {
        assertEquals("33.333333", ResultFormatter.format(BigDecimal("100").divide(BigDecimal("3"), java.math.MathContext.DECIMAL128), 6))
        assertEquals("33.33", ResultFormatter.format(BigDecimal("100").divide(BigDecimal("3"), java.math.MathContext.DECIMAL128), 2))
    }

    @Test
    fun `the last kept digit is rounded rather than dropped`() {
        assertEquals("2.5", ResultFormatter.format(BigDecimal("2.45"), 1))
        assertEquals("0.5", ResultFormatter.format(BigDecimal("0.45"), 1))
    }

    @Test
    fun `zero is zero, however it was arrived at`() {
        assertEquals("0", ResultFormatter.format(BigDecimal("0.000000"), 6))
    }
}
