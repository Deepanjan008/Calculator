package com.deepanjan.neocalculator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.deepanjan.neocalculator.data.AppDatabase
import com.deepanjan.neocalculator.data.HistoryDao
import com.deepanjan.neocalculator.repository.HistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()
    }

    @Singleton
    @Provides
    fun provideHistoryDao(db: AppDatabase): HistoryDao = db.historyDao()

    @Singleton
    @Provides
    fun provideHistoryRepository(dao: HistoryDao): HistoryRepository = HistoryRepository(dao)

    // Hilt-কে DataStore সাপ্লাই দেওয়ার জন্য এই নতুন মেথডটা যোগ করা হলো
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
