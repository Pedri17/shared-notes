package com.pproject.sharednotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.db.entity.UserWithFolders
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flow<List<User>>

    @Transaction
    @Query("SELECT * FROM user")
    fun getAllWithFolders(): Flow<List<UserWithFolders>>

    @Query("SELECT * FROM user WHERE userName in (:userName)")
    fun getByID(userName: String): Flow<User>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}