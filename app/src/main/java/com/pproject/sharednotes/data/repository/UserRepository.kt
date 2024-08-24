package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDao) {
    private val allUserWithFolders = userDao.getAllWithFolders()
    fun getAll(): Flow<List<User>> {
        return allUserWithFolders.map { users ->
            users.map {
                it.user
            }
        }
    }

    fun getAllWithNotes(): Flow<List<UserWithFolders>> {
        return allUserWithFolders
    }

    fun getUser(name: String): Flow<User> {
        return userDao.getByID(name)
    }

    /*
    fun getUser(name: String): Flow<User> {
        return allUserWithFolders.map { list ->
            list.filter {
                it.user.userName == name
            }[0].user
        }
    }
     */

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }
}