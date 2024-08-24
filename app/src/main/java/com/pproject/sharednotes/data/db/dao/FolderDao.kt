package com.pproject.sharednotes.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folder")
    fun getAll(): Flow<List<Folder>>

    @Query("SELECT * FROM folder")
    fun getAllWithNotes(): Flow<List<FolderWithNotes>>

    @Query("SELECT * FROM folder WHERE userName IN (:username)")
    fun getAllByUser(username: String): Flow<List<Folder>>

    @Query("SELECT * FROM folder WHERE userName IN (:username)")
    fun getAllByUserWithNotes(username: String): Flow<List<FolderWithNotes>>

    @Query("SELECT * FROM folder WHERE folderId in (:folderId)")
    fun getById(folderId: Int): Flow<Folder>

    @Query("SELECT * FROM folder WHERE folderId in (:folderId)")
    fun getWithNotesById(folderId: Int): Flow<FolderWithNotes>

    @Query("SELECT noteId FROM FolderNoteCrossRef WHERE folderId in (:folderId)")
    fun getNoteIdsById(folderId: Int): Flow<List<Int>>

    @Transaction
    @Query("SELECT * FROM FolderNoteCrossRef f JOIN note ON f.noteId == note.noteId WHERE folderId IN (:folderId)")
    fun getNotesById(folderId: Int): Flow<List<Note>>

    @Insert
    suspend fun insert(folder: Folder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteInFolder(folderNoteCrossRef: FolderNoteCrossRef)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Delete
    suspend fun deleteNoteInFolder(folderNoteCrossRef: FolderNoteCrossRef)
}