package com.nexuralabs.calculator.core.database.di

import android.content.Context
import androidx.room.Room
import com.nexuralabs.calculator.core.database.db.AppDatabase
import com.nexuralabs.calculator.core.database.db.HistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The database, and nothing above it.
 *
 * Room used to be provided from the data module, which meant the module that
 * holds repositories also held the schema. They change for different reasons -
 * a migration is a schema event, a new repository method is not - so they are
 * separate modules and separate modules mean separate bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()

    @Singleton
    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao = database.historyDao()
}
