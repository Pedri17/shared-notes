package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.local.dao.FolderDao
import com.pproject.sharednotes.data.local.entity.Folder
import com.pproject.sharednotes.data.local.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.local.entity.FolderWithNotes
import com.pproject.sharednotes.data.cloud.CloudManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FolderRepository(private val folderDao: FolderDao) {
    private val allFolders = folderDao.getAll()

    // Getters.
    fun getAll(): Flow<List<Folder>> {
        return allFolders
    }

    fun getAllByUser(username: String): Flow<List<Folder>> {
        return folderDao.getAllByUser(username)
    }

    fun getAllByUserWithNotes(username: String): Flow<List<FolderWithNotes>> {
        return folderDao.getAllByUserWithNotes(username)
    }

    fun getAllPairNamesByUser(username: String): Flow<List<Pair<Int, String>>> {
        return getAllByUser(username).map { folders ->
            folders.map {
                Pair(it.folderId, it.title)
            }
        }
    }

    fun getWithNotesById(id: Int): Flow<FolderWithNotes> {
        return folderDao.getWithNotesById(id)
    }

    // Manage entities.
    @WorkerThread
    suspend fun insert(folder: Folder): Int {
        return folderDao.insert(folder).toInt()
    }

    @WorkerThread
    suspend fun insertNoteInFolder(noteId: Int, folderId: Int) {
        folderDao.insertNoteInFolder(FolderNoteCrossRef(noteId, folderId))
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

    // Cloud.
    suspend fun saveOnCloud() {
        CloudManager.saveOnCloud(getAll(), "folders")
        CloudManager.saveOnCloud(folderDao.getAllFolderNoteCrossRef(), "folderNoteCrossRefs")
    }

    suspend fun loadFromCloud() {
        val cloudFolders = CloudManager.loadFromCloud<Folder>("folders")
        val cloudRefs = CloudManager.loadFromCloud<FolderNoteCrossRef>("folderNoteCrossRefs")
        if (cloudFolders.isNotEmpty()) folderDao.insert(cloudFolders)
        if (cloudRefs.isNotEmpty()) folderDao.insertNoteInFolder(cloudRefs)
    }
}