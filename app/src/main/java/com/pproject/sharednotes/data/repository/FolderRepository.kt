package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.db.dao.FolderDao
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderPinnedNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepository(private val folderDao: FolderDao) {
    private val allFolders = folderDao.getAll()
    private val allWithNotes = folderDao.getAllWithNotes()

    fun getAll(): Flow<List<Folder>> {
        return allFolders
    }

    fun getAllWithNotes(): Flow<List<FolderWithNotes>> {
        return allWithNotes
    }

    fun getAllPairNames(): Flow<List<Pair<Int, String>>> = allFolders.map { folders ->
        folders.map {
            Pair(it.folderId, it.title)
        }
    }

    fun getById(id: Int): Flow<Folder> {
        return folderDao.getById(id)
    }

    fun getWithNotesById(id: Int): Flow<FolderWithNotes> {
        return folderDao.getWithNotesById(id)
    }

    fun getNotesById(id: Int): Flow<List<Note>> {
        return folderDao.getNotesById(id)
    }

    fun getPinnedNotesById(id: Int): Flow<List<Int>> {
        return folderDao.getPinnedNoteIdsById(id)
    }

    @WorkerThread
    suspend fun insert(folder: Folder): Int {
        return folderDao.insert(folder).toInt()
    }

    @WorkerThread
    suspend fun insertNoteInFolder(noteId: Int, folderId: Int) {
        folderDao.insertNoteInFolder(FolderNoteCrossRef(noteId, folderId))
    }

    @WorkerThread
    suspend fun insertPinnedNoteInFolder(noteId: Int, folderId: Int) {
        folderDao.insertPinnedNoteInFolder(FolderPinnedNoteCrossRef(noteId, folderId))
    }

    @WorkerThread
    suspend fun update(folder: Folder) {
        folderDao.update(folder)
    }

    @WorkerThread
    suspend fun delete(folder: Folder) {
        folderDao.delete(folder)
    }

    @WorkerThread
    suspend fun deleteNoteInFolder(noteId: Int, folderId: Int) {
        folderDao.deleteNoteInFolder(FolderNoteCrossRef(noteId, folderId))
    }

    @WorkerThread
    suspend fun deletePinnedNoteInFolder(noteId: Int, folderId: Int) {
        folderDao.deletePinnedNoteInFolder(FolderPinnedNoteCrossRef(noteId, folderId))
    }
}