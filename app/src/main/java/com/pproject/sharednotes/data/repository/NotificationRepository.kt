package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.db.dao.NotificationDao
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.db.entity.NotificationWithNote
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class NotificationRepository(private val notificationDao: NotificationDao) {
    private val allNotification = notificationDao.getAll()
    fun getAll(): Flow<List<Notification>> {
        return allNotification
    }

    fun getAllToUser(toUser: String): Flow<List<Notification>> {
        return notificationDao.getAllToUser(toUser)
    }

    fun getAllWithNoteToUser(toUser: String): Flow<List<NotificationWithNote>> {
        return notificationDao.getAllWithNoteToUser(toUser)
    }

    @WorkerThread
    suspend fun insert(notification: Notification) {
        notificationDao.insert(notification)
    }

    @WorkerThread
    suspend fun delete(notification: Notification) {
        notificationDao.delete(notification)
    }
}