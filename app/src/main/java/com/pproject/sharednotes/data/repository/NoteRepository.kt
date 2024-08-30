package com.pproject.sharednotes.data.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pproject.sharednotes.data.db.dao.NoteDao
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.NoteWithFolders
import com.pproject.sharednotes.data.db.entity.User
import com.pproject.sharednotes.data.network.download
import com.pproject.sharednotes.data.network.upload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.io.ByteArrayOutputStream

class NoteRepository(private val noteDao: NoteDao) {
    private val allNotes = noteDao.getAll()
    private val pinnedNotes = noteDao.getPinnedNotes()

    fun getAll(): Flow<List<Note>> {
        return allNotes
    }

    suspend fun saveOnCloud() {
        val notes = getAll().firstOrNull()
        val noteUserCrossRefs = noteDao.getAllNoteUserCrossRef().firstOrNull()
        val output = ByteArrayOutputStream()
        if (notes != null) {
            ObjectMapper().writeValue(output, notes)
            upload("notes.json", output.toByteArray())
            output.reset()
        }
        if (noteUserCrossRefs != null) {
            ObjectMapper().writeValue(output, noteUserCrossRefs)
            upload("noteUserCrossRefs.json", output.toByteArray())
        }
    }

    suspend fun loadFromCloud() {
        val cloudNotesData = download("/notes.json")
        val cloudNoteUserCrossRefsData = download("/noteUserCrossRefs.json")
        if (cloudNotesData.isNotEmpty()) {
            val notes: List<Note> = ObjectMapper().readValue(cloudNotesData)
            noteDao.insert(notes)
        }
        if (cloudNoteUserCrossRefsData.isNotEmpty()) {
            val noteUserCrossRefs: List<NoteUserCrossRef> =
                ObjectMapper().readValue(cloudNoteUserCrossRefsData)
            noteDao.insertUserInNote(noteUserCrossRefs)
        }
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

    @WorkerThread
    suspend fun deleteAll() {
        noteDao.deleteAllNotes()
        //noteDao.deleteAllNoteUserCrossRefs()
    }

    @WorkerThread
    suspend fun deleteAllNoteUserCrossRefs() {
        noteDao.deleteAllNoteUserCrossRefs()
    }
}