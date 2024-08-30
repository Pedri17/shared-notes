package com.pproject.sharednotes.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pproject.sharednotes.data.db.dao.NotificationDao
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.db.entity.NotificationWithNote
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import com.pproject.sharednotes.data.network.download
import com.pproject.sharednotes.data.network.upload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class NotificationRepository(private val notificationDao: NotificationDao) {
    private val allNotification = notificationDao.getAll()
    fun getAll(): Flow<List<Notification>> {
        return allNotification
    }

    suspend fun saveOnCloud() {
        val notifications = getAll().firstOrNull()
        val output = ByteArrayOutputStream()
        if (notifications != null) {
            ObjectMapper().writeValue(output, notifications)
            upload("notifications.json", output.toByteArray())
        }
    }

    suspend fun loadFromCloud() {
        val cloudData = download("/notifications.json")
        if (cloudData.isNotEmpty()) {
            val users: List<Notification> = ObjectMapper().readValue(cloudData)
            notificationDao.insert(users)
        }
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

    @WorkerThread
    suspend fun deleteAll() {
        notificationDao.deleteAll()
    }
}