package com.nexuralabs.calculator.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexuralabs.calculator.core.domain.repository.CalculationHistory
import com.nexuralabs.calculator.core.domain.repository.CalculatorPreferences
import com.nexuralabs.calculator.core.domain.usecase.ApplyKey
import com.nexuralabs.calculator.core.domain.usecase.EvaluateExpression
import com.nexuralabs.calculator.core.domain.usecase.FormatResult
import com.nexuralabs.calculator.core.domain.usecase.RecordCalculation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The host, not the brain.
 *
 * Everything this class does is lifetime: it builds the state holder once, keeps
 * it alive across a rotation because a ViewModel does, and forwards events into
 * it. There is no arithmetic here, no formatting, and no decision about what a
 * key does - which is what makes all of that testable in
 * `CalculatorStateHolderTest` without a ViewModel, a dispatcher rule or a device.
 *
 * The two contracts it is handed are the whole of its knowledge about storage:
 * it does not know that history lives in Room or that settings live in DataStore.
 */
@HiltViewModel
class CalculatorViewModel @Inject constructor(
    history: CalculationHistory,
    preferences: CalculatorPreferences
) : ViewModel() {

    private val holder = CalculatorStateHolder(
        applyKey = ApplyKey(),
        evaluateExpression = EvaluateExpression(),
        formatResult = FormatResult(),
        recordCalculation = RecordCalculation(history),
        preferences = preferences
    )

    val state: StateFlow<CalculatorUiState> = holder.state

    val effects: Flow<CalculatorUiEffect> = holder.effects

    init {
        viewModelScope.launch { holder.observePreferences() }
    }

    /** A key on the keypad was pressed. */
    fun pressKey(input: String) {
        viewModelScope.launch { holder.onEvent(CalculatorUiEvent.KeyPressed(input)) }
    }

    /** An expression arrived from the history screen. */
    fun loadExpression(expression: String) {
        viewModelScope.launch { holder.onEvent(CalculatorUiEvent.ExpressionLoaded(expression)) }
    }
}
