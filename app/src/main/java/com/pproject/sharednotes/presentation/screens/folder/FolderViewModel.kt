package com.pproject.sharednotes.presentation.screens.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.local.entity.FolderWithNotes
import com.pproject.sharednotes.data.local.entity.Note
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.launch

data class FolderUiState(
    val title: TextFieldValue,
    val canEditTitle: Boolean = false,
) {
    fun isEmpty(): Boolean {
        return title.text == ""
    }
}

class FolderViewModel(
    savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    init {
        viewModelScope.launch {
            folderRepository.loadFromCloud()
            noteRepository.loadFromCloud()
        }
    }

    // Navigation arguments.
    val activeUser: String = checkNotNull(savedStateHandle["user"])
    var isNewFolder: Boolean = checkNotNull(savedStateHandle["newFolder"])
    private val openFolderId: Int = checkNotNull(savedStateHandle[AppScreens.FolderScreen.argument])

    // Data flows.
    val folderWithNotes: LiveData<FolderWithNotes> = noteRepository.getFolderWithOrderedNotesFlow(
        folderRepository.getWithNotesById(openFolderId),
        activeUser
    ).asLiveData()

    // Screen state.
    var uiState by mutableStateOf(
        FolderUiState(
            title = TextFieldValue("")
        )
    )

    fun loadFolder() = viewModelScope.launch {
        folderWithNotes.value?.let {
            uiState = uiState.copy(title = uiState.title.copy(text = it.folder.title))
        }
        setTitleCursorToEnd()
    }

    fun updateIsNewFolder(value: Boolean) {
        isNewFolder = value
    }

    // Title functions.
    fun updateTitle(newTitle: TextFieldValue) {
        uiState = uiState.copy(title = newTitle)
    }

    private fun setTitleCursorToEnd() {
        uiState = uiState.copy(
            title = uiState.title.copy(selection = TextRange(uiState.title.text.length))
        )
    }

    private fun saveTitle() = viewModelScope.launch {
        folderWithNotes.value?.let {
            folderRepository.update(it.folder.copy(title = uiState.title.text))
            folderRepository.saveOnCloud()
        }
    }

    fun updateCanEditTitle(canEdit: Boolean) {
        if (canEdit) {
            setTitleCursorToEnd()
        } else {
            saveTitle()
        }
        uiState = uiState.copy(canEditTitle = canEdit)
    }

    // Folder functions.
    fun createNoteInThisFolder(navController: NavController) = viewModelScope.launch {
        val newNoteId: Int = noteRepository.insert(Note())
        folderWithNotes.value?.let {
            folderRepository.insertNoteInFolder(newNoteId, it.folder.folderId)
        }
        navController.navigate(
            "${AppScreens.NoteScreen.route}/${activeUser}/${newNoteId}"
        )
        folderRepository.saveOnCloud()
        noteRepository.saveOnCloud()
    }

    fun deleteFolder() = viewModelScope.launch {
        folderWithNotes.value?.let {
            folderRepository.delete(it.folder)
            folderRepository.saveOnCloud()
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

                return FolderViewModel(
                    savedStateHandle,
                    folderRepository = application.container.folderRepository,
                    noteRepository = application.container.noteRepository,
                ) as T
            }
        }
    }
}