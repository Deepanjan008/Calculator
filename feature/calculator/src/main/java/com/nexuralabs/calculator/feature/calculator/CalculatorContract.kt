package com.nexuralabs.calculator.feature.calculator

import com.nexuralabs.calculator.core.domain.ResultFormatter
import com.nexuralabs.calculator.core.presentation.ScreenEffect
import com.nexuralabs.calculator.core.presentation.ScreenEvent
import com.nexuralabs.calculator.core.presentation.ScreenState

/**
 * Everything the calculator draws, in one immutable snapshot.
 *
 * [expression] is what the person is typing and [preview] is the running answer
 * underneath it, kept apart because they answer different questions: one is the
 * sum so far, the other is what it comes to. A trailing operator leaves the
 * preview empty rather than stale, which is the difference between "not yet" and
 * "wrong".
 */
data class CalculatorUiState(
    val expression: String = "",
    val preview: String = "",
    val precision: Int = ResultFormatter.DEFAULT_PRECISION,
    val hapticEnabled: Boolean = true
) : ScreenState

/** Everything a person can do to the calculator. */
sealed interface CalculatorUiEvent : ScreenEvent {

    /** A key on the keypad was pressed, by the character it shows. */
    data class KeyPressed(val input: String) : CalculatorUiEvent

    /** An expression arrived from somewhere else - the history screen, today. */
    data class ExpressionLoaded(val expression: String) : CalculatorUiEvent
}

/**
 * Nothing yet, and that is a statement rather than an omission.
 *
 * The calculator has no message to show that the state cannot carry: a failure
 * belongs in the preview, where the answer would have been. The marker exists so
 * the holder has a type to be parameterised by, and the first screen that needs
 * a snackbar will add the first member to it.
 */
sealed interface CalculatorUiEffect : ScreenEffect
