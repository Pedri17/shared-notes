package com.pproject.sharednotes.presentation.screens.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.local.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.local.entity.NoteWithFolders
import com.pproject.sharednotes.data.local.entity.Notification
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.data.repository.NotificationRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class NoteUiState(
    val title: String = "",
    val content: String = "",
    val pinned: Boolean = false,
    val folder: Int = 0,
    val loading: Boolean = false,
) {
    fun isEmpty(): Boolean {
        return title == "" && content == ""
    }
}

class NoteViewModel(
    savedStateHandle: SavedStateHandle,
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

    // Navigation arguments.
    private val activeUser: String = checkNotNull(savedStateHandle["user"])
    private val openNoteId: Int = checkNotNull(savedStateHandle[AppScreens.NoteScreen.argument])

    // Data flows.
    val allPairNameFolders = folderRepository.getAllPairNamesByUser(activeUser).asLiveData()
    val note: LiveData<NoteWithFolders> = noteRepository.getByIdWithFolders(openNoteId).asLiveData()
    val users: LiveData<List<String>> = noteRepository.getUserIdsById(openNoteId).asLiveData()

    // Screen state.
    var uiState by mutableStateOf(NoteUiState())
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

    fun backToLastScreen(navController: NavController) = viewModelScope.launch {
        uiState = uiState.copy(loading = true)
        noteRepository.saveOnCloud()
        navController.popBackStack()
    }

    // Note functions.
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

    // Collaborator functions.
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