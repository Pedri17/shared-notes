package com.pproject.sharednotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pproject.sharednotes.data.local.entity.Notification
import com.pproject.sharednotes.data.local.entity.NotificationWithNote
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAll(): Flow<List<Notification>>

    @Transaction
    @Query("SELECT * FROM notification WHERE toUser IN (:username)")
    fun getAllWithNoteToUser(username: String): Flow<List<NotificationWithNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notifications: List<Notification>)

    @Delete
    suspend fun delete(notification: Notification)
}