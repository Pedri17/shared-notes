package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.local.dao.UserDao
import com.pproject.sharednotes.data.local.entity.User
import com.pproject.sharednotes.data.cloud.CloudManager
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    private val allUsers = userDao.getAll()

    // Getters.
    fun getAll(): Flow<List<User>> {
        return allUsers
    }

    fun getUser(name: String): Flow<User> {
        return userDao.getByID(name)
    }

    // Manage entities.
    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    // Cloud.
    suspend fun saveOnCloud() {
        CloudManager.saveOnCloud(getAll(), "users")
    }

    suspend fun loadFromCloud() {
        val cloudData: List<User> = CloudManager.loadFromCloud("users")
        if (cloudData.isNotEmpty()) userDao.insert(cloudData)
    }
}