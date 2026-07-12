package com.nexuralabs.calculator.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.nexuralabs.calculator.core.data.datastore.dataStore
import com.nexuralabs.calculator.core.data.db.AppDatabase
import com.nexuralabs.calculator.core.data.db.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()
    }

    @Singleton
    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao = db.historyDao()

    // HistoryRepository and PreferencesRepository are constructor-injected (@Inject constructor),
    // so Hilt already knows how to build them from the bindings above - no @Provides needed here.

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
