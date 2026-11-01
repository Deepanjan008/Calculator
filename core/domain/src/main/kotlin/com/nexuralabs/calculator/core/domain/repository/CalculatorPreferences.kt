package com.nexuralabs.calculator.core.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * The two settings the calculator screen actually reads.
 *
 * Not the whole preferences store - the theme is a concern of the app shell, and
 * a screen that can change it is a screen that can fight the shell.
 */
interface CalculatorPreferences {

    /** How many decimal places a result is shown with. */
    val precision: Flow<Int>

    /** Whether a key press may vibrate. */
    val hapticEnabled: Flow<Boolean>
}
