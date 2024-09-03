package com.pproject.sharednotes.data.repository

import androidx.annotation.WorkerThread
import com.pproject.sharednotes.data.local.dao.NoteDao
import com.pproject.sharednotes.data.local.entity.Folder
import com.pproject.sharednotes.data.local.entity.FolderWithNotes
import com.pproject.sharednotes.data.local.entity.Note
import com.pproject.sharednotes.data.local.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.local.entity.NoteWithFolders
import com.pproject.sharednotes.data.cloud.CloudManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class NoteRepository(private val noteDao: NoteDao) {
    private val allNotes = noteDao.getAll()
    private val pinnedNotes = noteDao.getPinnedNotes()
    private val folderNoteCrossRef = noteDao.getFolderNoteCrossRef()

    // Getters.
    fun getAll(): Flow<List<Note>> {
        return allNotes
    }

    fun getOrderedOutOfFolders(
        folders: Flow<List<Folder>>,
        username: String
    ): Flow<List<Note>> {
        val folderNoteCrossRefFromFolders =
            folderNoteCrossRef.combine(folders) { fncr, foldersUser ->
                fncr.filter {
                    foldersUser.any { folder ->
                        it.folderId == folder.folderId
                    }
                }
            }
        val notesOutOfFolder = getAll().combine(folderNoteCrossRefFromFolders) { notes, cross ->
            notes.filter { note ->
                !cross.any {
                    it.noteId == note.noteId
                }
            }
        }
        return orderNotesFlow(notesOutOfFolder, username)
    }

    private fun orderNotesFlow(
        noteList: Flow<List<Note>>,
        username: String
    ): Flow<List<Note>> {
        return noteList.combine(pinnedNotes) { notes, pinnedNotes ->
            orderNotes(notes, pinnedNotes, username)
        }
    }

    private fun orderNotes(
        notes: List<Note>,
        pinnedNotes: List<NoteUserCrossRef>,
        username: String,
    ): List<Note> {
        return notes.filter { note ->
            pinnedNotes.contains(
                NoteUserCrossRef(note.noteId, username, true)
            )
        }.plus(
            notes.filter { note ->
                pinnedNotes.contains(
                    NoteUserCrossRef(note.noteId, username, false)
                )
            }
        ).map { note ->
            note.copy(
                pinned = pinnedNotes.contains(
                    NoteUserCrossRef(note.noteId, username, true)
                )
            )
        }
    }

    fun getByIdWithFolders(id: Int): Flow<NoteWithFolders> {
        return noteDao.getByIdWithFolders(id)
    }

    fun getFolderWithOrderedNotesFlow(
        folderWithNotes: Flow<FolderWithNotes>,
        username: String,
    ): Flow<FolderWithNotes> {
        return folderWithNotes.combine(pinnedNotes) { fwn, pinnedNotes ->
            fwn.copy(
                notes = orderNotes(fwn.notes, pinnedNotes, username)
            )
        }
    }

    fun getFoldersWithOrderedNotesFlow(
        allFoldersWithNotesFromUser: Flow<List<FolderWithNotes>>,
        username: String,
    ): Flow<List<FolderWithNotes>> {
        return allFoldersWithNotesFromUser.combine(pinnedNotes) { list, pinnedNotes ->
            list.map { fwn ->
                fwn.copy(
                    notes = orderNotes(fwn.notes, pinnedNotes, username)
                )
            }
        }
    }

    fun getUserIdsById(id: Int): Flow<List<String>> {
        return noteDao.getUserNamesById(id)
    }

    fun getPinnedNotes(): Flow<List<NoteUserCrossRef>> {
        return pinnedNotes
    }

    // Manage entities.
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

    // Cloud.
    suspend fun saveOnCloud() {
        CloudManager.saveOnCloud(getAll(), "notes")
        CloudManager.saveOnCloud(noteDao.getAllNoteUserCrossRef(), "noteUserCrossRefs")
    }

    suspend fun loadFromCloud() {
        val cloudNotes = CloudManager.loadFromCloud<Note>("notes")
        val cloudRefs = CloudManager.loadFromCloud<NoteUserCrossRef>("noteUserCrossRefs")
        if (cloudRefs.isNotEmpty()) noteDao.insertUserInNote(cloudRefs)
        if (cloudNotes.isNotEmpty()) noteDao.insert(cloudNotes)
    }
}