package com.pproject.sharednotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.db.entity.NotificationWithNote
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notification")
    fun getAll(): Flow<List<Notification>>

    @Query("SELECT * FROM notification WHERE toUser IN (:username)")
    fun getAllToUser(username: String): Flow<List<Notification>>

    @Transaction
    @Query("SELECT * FROM notification WHERE toUser IN (:username)")
    fun getAllWithNoteToUser(username: String): Flow<List<NotificationWithNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)
}