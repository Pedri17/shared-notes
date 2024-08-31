package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.local.dao.PreferencesDao
import com.pproject.sharednotes.data.local.entity.Preferences
import kotlinx.coroutines.flow.Flow

class PreferencesRepository(private val preferencesDao: PreferencesDao) {
    private val activePreferences = preferencesDao.getActiveSession()

    // Getters.
    fun getActivePreference(): Flow<Preferences> {
        return activePreferences
    }

    // Manage entities.
    @WorkerThread
    suspend fun insert(preferences: Preferences) {
        preferencesDao.insert(preferences)
    }
}