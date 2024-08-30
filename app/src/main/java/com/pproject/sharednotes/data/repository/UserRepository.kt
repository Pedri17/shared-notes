package com.pproject.sharednotes.data.repository

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pproject.sharednotes.data.db.dao.UserDao
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import com.pproject.sharednotes.data.network.download
import com.pproject.sharednotes.data.network.upload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.Writer

class UserRepository(private val userDao: UserDao) {
    private val allUserWithFolders = userDao.getAllWithFolders()
    private val allUsers = userDao.getAll()

    fun getAll(): Flow<List<User>> {
        return allUsers
    }

    suspend fun saveUsersOnCloud() {
        val users = getAll().firstOrNull()
        val output = ByteArrayOutputStream()
        if (users != null) {
            val mapper = ObjectMapper()
            mapper.writeValue(output, users)
            upload("users.json", output.toByteArray())
        }
        loadUsersFromCloud()
    }

    suspend fun loadUsersFromCloud() {
        val cloudData = download("/users.json")
        if (cloudData.isNotEmpty()) {
            deleteAll()

            val mapper = ObjectMapper()
            val users: List<User> = mapper.readValue(cloudData)
            users.forEach {
                userDao.insert(it)
            }
            Log.d("Load Users", "Success")
        }
    }

    fun getAllWithNotes(): Flow<List<UserWithFolders>> {
        return allUserWithFolders
    }

    fun getUser(name: String): Flow<User> {
        return userDao.getByID(name)
    }

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun deleteAll() {
        userDao.deleteAll()
    }
}