package com.pproject.sharednotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.NoteWithFolders
import com.pproject.sharednotes.data.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): Flow<List<Note>>

    @Query("SELECT * FROM noteusercrossref")
    fun getAllNoteUserCrossRef(): Flow<List<NoteUserCrossRef>>

    @Query("SELECT * FROM note WHERE noteId in (:noteId)")
    fun getByID(noteId: Int): Flow<Note>

    @Query("SELECT * FROM note WHERE noteId in (:noteId)")
    fun getByIdWithFolders(noteId: Int): Flow<NoteWithFolders>

    @Transaction
    @Query("SELECT userName FROM noteusercrossref WHERE noteId IN (:noteId)")
    fun getUserNamesById(noteId: Int): Flow<List<String>>

    @Query("SELECT * FROM noteusercrossref")
    fun getPinnedNotes(): Flow<List<NoteUserCrossRef>>

    @Query("SELECT * FROM foldernotecrossref")
    fun getFolderNoteCrossRef(): Flow<List<FolderNoteCrossRef>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notes: List<Note>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInNote(noteUserCrossRef: NoteUserCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInNote(noteUserCrossRefs: List<NoteUserCrossRef>)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM note")
    suspend fun deleteAllNotes()

    @Query("DELETE FROM noteusercrossref")
    suspend fun deleteAllNoteUserCrossRefs()

    @Delete
    suspend fun deleteUserInNote(noteUserCrossRef: NoteUserCrossRef)

    @Query("DELETE FROM note WHERE noteId = (:noteId)")
    suspend fun deleteById(noteId: Int);
}