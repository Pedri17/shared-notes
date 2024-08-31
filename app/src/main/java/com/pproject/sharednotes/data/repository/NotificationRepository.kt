package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.local.dao.NotificationDao
import com.pproject.sharednotes.data.local.entity.Notification
import com.pproject.sharednotes.data.local.entity.NotificationWithNote
import com.pproject.sharednotes.data.cloud.CloudManager
import kotlinx.coroutines.flow.Flow

class NotificationRepository(private val notificationDao: NotificationDao) {
    private val allNotification = notificationDao.getAll()

    // Getters.
    fun getAll(): Flow<List<Notification>> {
        return allNotification
    }

    fun getAllWithNoteToUser(toUser: String): Flow<List<NotificationWithNote>> {
        return notificationDao.getAllWithNoteToUser(toUser)
    }

    // Manage entities.
    @WorkerThread
    suspend fun insert(notification: Notification) {
        notificationDao.insert(notification)
    }

    @WorkerThread
    suspend fun delete(notification: Notification) {
        notificationDao.delete(notification)
    }

    // Cloud.
    suspend fun saveOnCloud() {
        CloudManager.saveOnCloud(getAll(), "notifications")
    }

    suspend fun loadFromCloud() {
        val cloudData = CloudManager.loadFromCloud<Notification>("notifications")
        if (cloudData.isNotEmpty()) {
            notificationDao.insert(cloudData)
        }
    }
}