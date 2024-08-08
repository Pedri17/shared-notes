package com.pproject.sharednotes.presentation.screens.folder

import android.util.Log
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
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.presentation.navigation.AppNavigation
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.launch

class FolderViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val openFolderId: Int = checkNotNull(savedStateHandle[AppScreens.FolderScreen.argument])

    var folderWithNotes: LiveData<FolderWithNotes> =
        folderRepository.getWithNotesById(openFolderId).asLiveData()

    var textFieldValueTitle by mutableStateOf(
        TextFieldValue(
            folderWithNotes.value?.folder?.title ?: ""
        )
    )
        private set

    var canEditTitle by mutableStateOf(false)
        private set

    fun updateTitle(newTitle: TextFieldValue) {
        textFieldValueTitle = newTitle
    }

    private fun saveTitle() = viewModelScope.launch {
        folderWithNotes.value?.folder?.let { folderRepository.update(it.copy(title = textFieldValueTitle.text)) }
    }

    private fun setTitleCursorToEnd() {
        textFieldValueTitle = textFieldValueTitle.copy(
            selection = TextRange(textFieldValueTitle.text.length)
        )
    }

    fun updateCanEditTitle(canEdit: Boolean) {
        if (canEdit) {
            folderWithNotes.value?.folder?.title?.let {
                textFieldValueTitle =
                    textFieldValueTitle.copy(text = it)
            }
            setTitleCursorToEnd()
        } else {
            saveTitle()
        }
        canEditTitle = canEdit
    }

    fun createNoteInThisFolder(navController: NavController) = viewModelScope.launch {
        val newNoteId: Int = noteRepository.insert(Note())
        folderWithNotes.value?.folder?.let {
            folderRepository.insertNoteInFolder(newNoteId, it.folderId)
        }
        navController.navigate(
            "${AppScreens.NoteScreen.route}/${newNoteId}"
        )
    }

    fun deleteFolder() = viewModelScope.launch {
        folderWithNotes.value?.let { folderRepository.delete(it.folder) }
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