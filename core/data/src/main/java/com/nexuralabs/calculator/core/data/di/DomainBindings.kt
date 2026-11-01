package com.nexuralabs.calculator.core.data.di

import com.nexuralabs.calculator.core.data.repository.HistoryRepository
import com.nexuralabs.calculator.core.data.repository.PreferencesRepository
import com.nexuralabs.calculator.core.domain.repository.CalculationHistory
import com.nexuralabs.calculator.core.domain.repository.CalculatorPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Where the contracts meet the implementations.
 *
 * This is the only file in the app that knows both halves at once, which is the
 * whole point of the split: a screen depends on [CalculationHistory] and has no
 * idea that the answer is Room, and this module depends on both and has no idea
 * what a screen is.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DomainBindings {

    @Binds
    abstract fun bindCalculationHistory(implementation: HistoryRepository): CalculationHistory

    @Binds
    abstract fun bindCalculatorPreferences(implementation: PreferencesRepository): CalculatorPreferences
}
