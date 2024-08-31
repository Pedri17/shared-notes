package com.pproject.sharednotes.data

import android.content.Context
import com.pproject.sharednotes.data.local.database.SharedNotesDatabase
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.data.repository.NotificationRepository
import com.pproject.sharednotes.data.repository.PreferencesRepository
import com.pproject.sharednotes.data.repository.UserRepository

class AppContainer(private val context: Context) {
    private val database by lazy { SharedNotesDatabase.getDatabase(context) }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val folderRepository by lazy { FolderRepository(database.folderDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val preferencesRepository by lazy { PreferencesRepository(database.preferencesDao()) }
    val notificationRepository by lazy { NotificationRepository(database.notificationDao()) }
}