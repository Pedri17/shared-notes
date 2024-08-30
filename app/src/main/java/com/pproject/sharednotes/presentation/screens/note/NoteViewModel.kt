package com.pproject.sharednotes.presentation.screens.note

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.db.entity.Note
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.db.entity.FolderNoteCrossRef
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.db.entity.NoteWithFolders
import com.pproject.sharednotes.data.db.entity.Notification
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.data.repository.NotificationRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import com.pproject.sharednotes.presentation.screens.folder.FolderViewModel
import com.pproject.sharednotes.presentation.screens.note.components.Section
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

data class NoteUiState(
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val folder: Int = 0,
) {
    fun isEmpty(): Boolean {
        return title == "" && content == ""
    }
}

class NoteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
    private val notificationRepository: NotificationRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            noteRepository.loadFromCloud()
            folderRepository.loadFromCloud()
            notificationRepository.loadFromCloud()
        }
    }

    private val activeUser: String = checkNotNull(savedStateHandle["user"])
    private val openNoteId: Int = checkNotNull(savedStateHandle[AppScreens.NoteScreen.argument])
    val allPairNameFolders = folderRepository.getAllPairNamesByUser(activeUser).asLiveData()
    val note: LiveData<NoteWithFolders> = noteRepository.getByIdWithFolders(openNoteId).asLiveData()
    val users: LiveData<List<String>> = noteRepository.getUserIdsById(openNoteId).asLiveData()

    var uiState by mutableStateOf(
        NoteUiState(
            title = note.value?.note?.title ?: "",
            content = note.value?.note?.content ?: "",
        )
    )
        private set


    fun loadNoteData() {
        note.value?.let {
            uiState = uiState.copy(
                title = it.note.title,
                content = it.note.content,
            )
            val selectedFolder = getSelectedFolderId()
            if (selectedFolder != null) {
                viewModelScope.launch {
                    uiState = uiState.copy(
                        pinned = noteRepository.getPinnedNotes().map { pinneds ->
                            pinneds.contains(NoteUserCrossRef(openNoteId, activeUser, true))
                        }.firstOrNull() ?: false
                    )
                }
            }
        }
    }

    fun getSelectedFolderId(): Int? {
        note.value?.let {
            return it.getSelectedFolderId(activeUser)
        }
        return null
    }

    fun updateTitle(newTitle: String) {
        uiState = uiState.copy(title = newTitle)
        viewModelScope.launch {
            note.value?.let { noteRepository.update(it.note.copy(title = newTitle)) }
        }
    }

    fun updateContent(newText: String) {
        uiState = uiState.copy(content = newText)
        viewModelScope.launch {
            note.value?.let { noteRepository.update(it.note.copy(content = newText)) }
        }
    }

    fun deleteNote(navController: NavController) = viewModelScope.launch {
        note.value?.let { note ->
            noteRepository.deleteUserInNote(note.note.noteId, activeUser)
            users.value?.let {
                if (it.isEmpty()) {
                    noteRepository.delete(note.note)
                }
            }
        }
        backToLastScreen(navController)
    }

    fun updatePinned(newPinned: Boolean) = viewModelScope.launch {
        getSelectedFolderId()?.let {
            noteRepository.insertUserInNote(openNoteId, activeUser, newPinned)
        }
        uiState = uiState.copy(pinned = newPinned)
    }

    fun updateFolder(previousFolderId: Int?, newFolderId: Int?) = viewModelScope.launch {
        note.value?.let { note ->
            previousFolderId?.let { folderRepository.deleteNoteInFolder(note.note.noteId, it) }
            newFolderId?.let { folderRepository.insertNoteInFolder(note.note.noteId, it) }
            folderRepository.saveOnCloud()
        }
    }

    fun inviteCollaborator(collaboratorName: String) = viewModelScope.launch {
        note.value?.let {
            notificationRepository.insert(
                Notification(
                    fromUser = activeUser,
                    toUser = collaboratorName,
                    type = Notification.Type.PARTICIPATE,
                    noteId = it.note.noteId
                )
            )
            notificationRepository.saveOnCloud()
        }
    }

    fun deleteCollaborator(collaboratorName: String) = viewModelScope.launch {
        note.value?.let {
            notificationRepository.insert(
                Notification(
                    fromUser = activeUser,
                    toUser = collaboratorName,
                    type = Notification.Type.DISPARTICIPATE,
                    noteId = it.note.noteId
                )
            )
        }
    }

    fun backToLastScreen(navController: NavController) = viewModelScope.launch {
        navController.popBackStack()
        noteRepository.saveOnCloud()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY]) as SharedNotesApplication
                val savedStateHandle = extras.createSavedStateHandle()

                return NoteViewModel(
                    savedStateHandle,
                    folderRepository = application.container.folderRepository,
                    noteRepository = application.container.noteRepository,
                    notificationRepository = application.container.notificationRepository,
                ) as T
            }
        }
    }
}