package com.pproject.sharednotes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pproject.sharednotes.data.local.entity.Folder
import com.pproject.sharednotes.data.local.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.local.entity.FolderWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folder")
    fun getAll(): Flow<List<Folder>>

    @Query("SELECT * FROM foldernotecrossref")
    fun getAllFolderNoteCrossRef(): Flow<List<FolderNoteCrossRef>>

    @Query("SELECT * FROM folder WHERE userName IN (:username)")
    fun getAllByUser(username: String): Flow<List<Folder>>

    @Query("SELECT * FROM folder WHERE userName IN (:username)")
    fun getAllByUserWithNotes(username: String): Flow<List<FolderWithNotes>>

    @Query("SELECT * FROM folder WHERE folderId in (:folderId)")
    fun getWithNotesById(folderId: Int): Flow<FolderWithNotes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: Folder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folders: List<Folder>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteInFolder(folderNoteCrossRef: FolderNoteCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteInFolder(folderNoteCrossRefs: List<FolderNoteCrossRef>)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Delete
    suspend fun deleteNoteInFolder(folderNoteCrossRef: FolderNoteCrossRef)
}