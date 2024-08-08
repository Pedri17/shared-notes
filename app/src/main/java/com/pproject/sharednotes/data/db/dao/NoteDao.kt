package com.pproject.sharednotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE noteId in (:noteID)")
    fun getByID(noteID: Int): Flow<Note>

    @Transaction
    @Query("SELECT userName FROM noteusercrossref WHERE noteId IN (:noteId)")
    fun getUserNamesById(noteId: Int): Flow<List<String>>

    @Insert
    suspend fun insert(note: Note): Long

    @Insert
    suspend fun insertUserInNote(noteUserCrossRef: NoteUserCrossRef)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Delete
    suspend fun deleteUserInNote(noteUserCrossRef: NoteUserCrossRef)

    @Query("DELETE FROM note WHERE noteId = (:noteId)")
    suspend fun deleteById(noteId: Int);
}