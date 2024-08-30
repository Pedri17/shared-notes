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
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.db.entity.NoteUserCrossRef
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
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
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val activeUser: String = checkNotNull(savedStateHandle["user"])
    var isNewFolder: Boolean = checkNotNull(savedStateHandle["newFolder"])
    private val openFolderId: Int = checkNotNull(savedStateHandle[AppScreens.FolderScreen.argument])
    private val pinnedNotes = noteRepository.getPinnedNotes()
    val folderWithNotes: LiveData<FolderWithNotes> =
        folderRepository.getWithNotesById(openFolderId).combine(pinnedNotes) { fwn, pinnedNotes ->
            fwn.copy(
                notes = fwn.notes.filter {
                    pinnedNotes.contains(NoteUserCrossRef(it.noteId, activeUser, true))
                }.plus(
                    fwn.notes.filter {
                        pinnedNotes.contains(NoteUserCrossRef(it.noteId, activeUser, false))
                    }
                ).map { note ->
                    note.copy(
                        pinned = pinnedNotes.contains(
                            NoteUserCrossRef(note.noteId, activeUser, true)
                        )
                    )
                }
            )
        }.asLiveData()

    var uiState by mutableStateOf(
        FolderUiState(
            title = TextFieldValue("")
        )
    )

    fun updateIsNewFolder(value: Boolean) {
        isNewFolder = value
    }

    fun updateTitle(newTitle: TextFieldValue) {
        uiState = uiState.copy(title = newTitle)
    }

    private fun saveTitle() = viewModelScope.launch {
        folderWithNotes.value?.let {
            folderRepository.update(it.folder.copy(title = uiState.title.text))
        }
    }

    private fun setTitleCursorToEnd() {
        uiState = uiState.copy(
            title = uiState.title.copy(selection = TextRange(uiState.title.text.length))
        )
    }

    fun loadFolder() = viewModelScope.launch {
        folderWithNotes.value?.let {
            uiState = uiState.copy(title = uiState.title.copy(text = it.folder.title))
        }
        setTitleCursorToEnd()
    }

    fun updateCanEditTitle(canEdit: Boolean) {
        if (canEdit) {
            setTitleCursorToEnd()
        } else {
            saveTitle()
        }
        uiState = uiState.copy(canEditTitle = canEdit)
    }

    fun createNoteInThisFolder(navController: NavController) = viewModelScope.launch {
        val newNoteId: Int = noteRepository.insert(Note())
        folderWithNotes.value?.let {
            folderRepository.insertNoteInFolder(newNoteId, it.folder.folderId)
        }
        navController.navigate(
            "${AppScreens.NoteScreen.route}/${activeUser}/${newNoteId}"
        )
    }

    fun deleteFolder() = viewModelScope.launch {
        folderWithNotes.value?.let {
            folderRepository.delete(it.folder)
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