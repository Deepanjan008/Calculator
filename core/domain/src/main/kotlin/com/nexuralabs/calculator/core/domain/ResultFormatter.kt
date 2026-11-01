package com.nexuralabs.calculator.core.domain

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Turns a result into the string the screen shows.
 *
 * Two things happen here that the raw value cannot do for itself. The scale is
 * fixed to the user's precision, because `1/3` in decimal is an infinite
 * expansion and a calculator that prints thirty-four digits is telling you
 * something about its arithmetic rather than about your sum. And trailing zeros
 * are stripped, because `1.5 + 2.5` should read `4`, not `4.000000`.
 */
object ResultFormatter {

    /** The number of decimal places used when nothing else is known. */
    const val DEFAULT_PRECISION = 6

    fun format(value: BigDecimal, precision: Int): String =
        value.setScale(precision, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
}
