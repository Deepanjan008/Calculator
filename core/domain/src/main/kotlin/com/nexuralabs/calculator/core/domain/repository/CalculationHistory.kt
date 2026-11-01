package com.nexuralabs.calculator.core.domain.repository

/**
 * The history, as far as the calculator screen is concerned: it can write a
 * finished sum down, and that is all.
 *
 * The screen used to hold the whole `HistoryRepository`, which also reads,
 * edits, deletes and clears. A screen that can clear the history by accident is
 * a screen with more reach than it needs, so it is handed this instead.
 */
interface CalculationHistory {

    /** Writes [expression] and its [result] down, most recent first. */
    suspend fun record(expression: String, result: String)
}
