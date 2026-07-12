package com.nexuralabs.calculator.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * The ONE Context.dataStore extension property in the whole app.
 *
 * The old single-module project declared this exact same property (same file name "settings")
 * in two different packages at once. The androidx DataStore delegate assumes a single owner per
 * file name - having two independent property delegates both backed by "settings" is a known
 * crash waiting to happen ("There are multiple DataStores active for the same file"). Now there
 * is only one, here, and every feature reaches it through PreferencesRepository below instead of
 * touching DataStore keys directly.
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
