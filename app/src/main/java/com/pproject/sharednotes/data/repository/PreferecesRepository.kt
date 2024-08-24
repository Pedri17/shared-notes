package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.db.dao.PreferencesDao
import com.pproject.sharednotes.data.db.entity.Preferences
import kotlinx.coroutines.flow.Flow

class PreferencesRepository(private val preferencesDao: PreferencesDao) {
    private val activePreferences = preferencesDao.getActiveSession()

    fun getActivePreference(): Flow<Preferences> {
        return activePreferences
    }

    @WorkerThread
    suspend fun insert(preferences: Preferences) {
        preferencesDao.insert(preferences)
    }
}