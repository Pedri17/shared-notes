package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.pproject.sharednotes.data.db.dao.NoteDao
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.NoteWithFolders
import com.pproject.sharednotes.data.db.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    private val allNotes = noteDao.getAll()
    private val pinnedNotes = noteDao.getPinnedNotes()

    fun getAll(): Flow<List<Note>> {
        return allNotes
    }

    fun getById(id: Int): Flow<Note> {
        return noteDao.getByID(id)
    }

    fun getByIdWithFolders(id: Int): Flow<NoteWithFolders> {
        return noteDao.getByIdWithFolders(id)
    }

    fun getUserIdsById(id: Int): Flow<List<String>> {
        return noteDao.getUserNamesById(id)
    }

    fun getPinnedNotes(): Flow<List<NoteUserCrossRef>> {
        return pinnedNotes
    }

    fun getFolderNoteCrossRef(): Flow<List<FolderNoteCrossRef>> {
        return noteDao.getFolderNoteCrossRef()
    }

    @WorkerThread
    suspend fun insert(note: Note): Int {
        return noteDao.insert(note).toInt()
    }

    @WorkerThread
    suspend fun insertUserInNote(noteId: Int, userName: String) {
        noteDao.insertUserInNote(NoteUserCrossRef(noteId, userName))
    }

    @WorkerThread
    suspend fun insertUserInNote(noteId: Int, userName: String, pinned: Boolean) {
        noteDao.insertUserInNote(NoteUserCrossRef(noteId, userName, pinned))
    }

    @WorkerThread
    suspend fun deleteUserInNote(noteId: Int, userName: String) {
        noteDao.deleteUserInNote(NoteUserCrossRef(noteId, userName))
    }

    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}