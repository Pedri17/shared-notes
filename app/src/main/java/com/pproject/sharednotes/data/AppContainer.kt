package com.pproject.sharednotes.data

import android.content.Context
import com.pproject.sharednotes.data.db.database.SharedNotesDatabase
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository

class AppContainer(private val context: Context) {
    private val database by lazy { SharedNotesDatabase.getDatabase(context) }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val folderRepository by lazy { FolderRepository(database.folderDao()) }
}