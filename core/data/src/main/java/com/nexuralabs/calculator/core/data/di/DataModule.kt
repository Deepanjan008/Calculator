package com.nexuralabs.calculator.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.nexuralabs.calculator.core.data.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * What is left of this module's bindings: the preferences store.
 *
 * The database moved to `:core:database` (see `DatabaseModule` there), and the
 * two repositories are constructor-injected, so Hilt builds them from the
 * contracts they implement. That is the shape to keep: a module in here provides
 * storage, never a decision.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
