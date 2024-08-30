package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pproject.sharednotes.data.db.dao.FolderDao
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.network.download
import com.pproject.sharednotes.data.network.upload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.ByteArrayOutputStream

class FolderRepository(private val folderDao: FolderDao) {
    private val allFolders = folderDao.getAll()
    private val allWithNotes = folderDao.getAllWithNotes()

    fun getAll(): Flow<List<Folder>> {
        return allFolders
    }

    suspend fun saveOnCloud() {
        val folders = getAll().firstOrNull()
        val folderNoteCrossRefs = folderDao.getAllFolderNoteCrossRef().firstOrNull()
        val output = ByteArrayOutputStream()
        if (folders != null) {
            ObjectMapper().writeValue(output, folders)
            upload("folders.json", output.toByteArray())
            output.reset()
        }
        if (folderNoteCrossRefs != null) {
            ObjectMapper().writeValue(output, folderNoteCrossRefs)
            upload("folderNoteCrossRefs.json", output.toByteArray())
        }
    }

    suspend fun loadFromCloud() {
        val cloudFoldersData = download("/folders.json")
        val cloudFolderNoteCrossRefsData = download("/folderNoteCrossRefs.json")
        if (cloudFoldersData.isNotEmpty()) {
            val folders: List<Folder> = ObjectMapper().readValue(cloudFoldersData)
            folderDao.insert(folders)
        }
        if (cloudFolderNoteCrossRefsData.isNotEmpty()) {
            val noteUserCrossRefs: List<FolderNoteCrossRef> =
                ObjectMapper().readValue(cloudFolderNoteCrossRefsData)
            folderDao.insertNoteInFolder(noteUserCrossRefs)
        }
    }

    fun getAllByUser(username: String): Flow<List<Folder>> {
        return folderDao.getAllByUser(username)
    }


    fun getAllWithNotes(): Flow<List<FolderWithNotes>> {
        return allWithNotes
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

    fun getNotesById(id: Int): Flow<List<Note>> {
        return folderDao.getNotesById(id)
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
    suspend fun deleteAllFolders() {
        folderDao.deleteAllFolders()
    }

    @WorkerThread
    suspend fun deleteAllFolderNoteCrossRefs() {
        folderDao.deleteAllFolderNoteCrossRef()
    }
}