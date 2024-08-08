package com.pproject.sharednotes.presentation.screens.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.db.entity.Folder
import com.pproject.sharednotes.data.db.entity.FolderWithNotes
import com.pproject.sharednotes.data.db.entity.Note
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import kotlinx.coroutines.launch

class HomeViewModel(
    private val folderRepository: FolderRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    val foldersWithNotes: LiveData<List<FolderWithNotes>> =
        folderRepository.getAllWithNotes().asLiveData()

    fun createNewFolder(navController: NavController) = viewModelScope.launch {
        val newFolderId: Int = folderRepository.insert(Folder())
        navController.navigate(
            "${AppScreens.FolderScreen.route}/${newFolderId}"
        )
    }

    fun createNewNoteOnFolder(navController: NavController, folderId: Int) = viewModelScope.launch {
        val newNoteId: Int = noteRepository.insert(Note()).toInt()
        folderRepository.insertNoteInFolder(noteId = newNoteId, folderId = folderId)
        navController.navigate(
            "${AppScreens.NoteScreen.route}/${newNoteId}"
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SharedNotesApplication
                HomeViewModel(
                    noteRepository = application.container.noteRepository,
                    folderRepository = application.container.folderRepository,
                )
            }
        }
    }
}