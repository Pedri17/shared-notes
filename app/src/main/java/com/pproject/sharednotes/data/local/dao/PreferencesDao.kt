package com.pproject.sharednotes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pproject.sharednotes.data.local.entity.Preferences
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {
    @Query("SELECT * FROM preferences WHERE username IN (:username)")
    fun get(username: String): Flow<Preferences>

    @Query("SELECT * FROM preferences WHERE activeSession IS TRUE")
    fun getActiveSession(): Flow<Preferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: Preferences)
}