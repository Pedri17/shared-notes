package com.pproject.sharednotes.presentation.screens.home

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.db.entity.NotificationWithNote
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.data.repository.NotificationRepository
import com.pproject.sharednotes.data.repository.PreferencesRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
    private val notificationRepository: NotificationRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    val activeUser: String = checkNotNull(savedStateHandle["user"])
    private var pinnedNotes = noteRepository.getPinnedNotes()
    var foldersWithNotes: LiveData<List<FolderWithNotes>> =
        folderRepository.getAllByUserWithNotes(activeUser)
            .combine(pinnedNotes) { list, pinnedNotes ->
                list.map { fwn ->
                    fwn.copy(
                        notes = fwn.notes.filter { note ->
                            pinnedNotes.contains(
                                FolderNoteCrossRef(note.noteId,fwn.folder.folderId,true)
                            )
                        }.plus(
                            fwn.notes.filter { note ->
                                pinnedNotes.contains(
                                    FolderNoteCrossRef(note.noteId,fwn.folder.folderId,false)
                                )
                            }
                        ).map { note ->
                            note.copy(
                                pinned = pinnedNotes.contains(
                                    FolderNoteCrossRef(
                                        note.noteId,
                                        fwn.folder.folderId,
                                        true
                                    )
                                )
                            )
                        }
                    )
                }
            }.asLiveData()
    private var notifications: Flow<List<NotificationWithNote>> = notificationRepository.getAllWithNoteToUser(activeUser)

    fun getNotificationPairs(context: Context): LiveData<List<Pair<Int, String>>> {
        return notifications.map { list ->
            list.map { notWithNote ->
                Pair(notWithNote.notification.notificationId, notWithNote.toMessage(context))
            }
        }.asLiveData()
    }

    fun interactNotification(notificationId: Int, accept: Boolean) = viewModelScope.launch {
        val notWithNote: NotificationWithNote? = notifications.map { list ->
            list.filter {
                it.notification.notificationId == notificationId
            }[0]
        }.firstOrNull()

        notWithNote?.let {
            // Use notification action
            if (accept) {
                when (notWithNote.notification.type) {
                    Notification.Type.PARTICIPATE ->
                        noteRepository.insertUserInNote(
                            notWithNote.notification.noteId,
                            notWithNote.notification.toUser
                        )

                    Notification.Type.DISPARTICIPATE ->
                        noteRepository.deleteUserInNote(
                            notWithNote.notification.noteId,
                            notWithNote.notification.toUser
                        )
                }
            }
            // Delete notification
            notificationRepository.delete(notWithNote.notification)
        }
    }

    fun createNewFolder(navController: NavController) = viewModelScope.launch {
        val newFolderId: Int = folderRepository.insert(Folder(userName = activeUser))
        navController.navigate(
            "${AppScreens.FolderScreen.route}/${activeUser}/true/${newFolderId}"
        )
    }

    fun createNewNoteOnFolder(navController: NavController, folderId: Int) = viewModelScope.launch {
        val newNoteId: Int = noteRepository.insert(Note())
        noteRepository.insertUserInNote(newNoteId, userName = activeUser)
        folderRepository.insertNoteInFolder(noteId = newNoteId, folderId = folderId)
        navController.navigate(
            "${AppScreens.NoteScreen.route}/${activeUser}/${newNoteId}"
        )
    }

    fun deleteFolder(folder: Folder) = viewModelScope.launch {
        folderRepository.delete(folder)
    }

    fun logOut(navController: NavController) = viewModelScope.launch {
        val activeSessionPreference = preferencesRepository.getActivePreference().firstOrNull()
        if (activeSessionPreference != null) {
            preferencesRepository.insert(activeSessionPreference.copy(activeSession = false))
        }
        navController.navigate(AppScreens.LoginScreen.route)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as SharedNotesApplication
                val savedStateHandle = extras.createSavedStateHandle()

                return HomeViewModel(
                    savedStateHandle,
                    folderRepository = application.container.folderRepository,
                    noteRepository = application.container.noteRepository,
                    notificationRepository = application.container.notificationRepository,
                    preferencesRepository = application.container.preferencesRepository,
                ) as T
            }
        }
    }
}