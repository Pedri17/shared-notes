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
import com.pproject.sharednotes.app.SharedNotesApplication
import com.pproject.sharednotes.data.repository.FolderRepository
import com.pproject.sharednotes.data.repository.NoteRepository
import com.pproject.sharednotes.presentation.navigation.AppScreens
import com.pproject.sharednotes.presentation.screens.folder.FolderViewModel
import com.pproject.sharednotes.presentation.screens.note.components.Section
import kotlinx.coroutines.launch

class NoteViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(),
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository,
) : ViewModel() {
    private val openNoteId: Int = checkNotNull(savedStateHandle[AppScreens.NoteScreen.argument])
    val allPairNameFolders = folderRepository.getAllPairNames().asLiveData()

    val note: LiveData<Note> =
        checkNotNull(noteRepository.getById(openNoteId).asLiveData())

    val users: LiveData<List<String>> =
        checkNotNull(noteRepository.getUserIdsById(openNoteId).asLiveData())

    private var _content =
        note.value?.let { decomposeInSections(it.content).toMutableStateList() }
            ?: emptyList<Section>().toMutableStateList()
    val content: List<Section>
        get() = _content.toList()

    fun updateTitle(newTitle: String) = viewModelScope.launch {
        note.value?.let { noteRepository.update(it.copy(title = newTitle)) }
    }

    private fun updateContent(newContent: String) = viewModelScope.launch {
        _content = decomposeInSections(newContent).toMutableStateList()
        note.value?.let { noteRepository.update(it.copy(content = newContent)) }
    }

    fun updateContent(section: Section, newText: String) = viewModelScope.launch {
        _content[_content.indexOf(section)] = section.copy(text = newText)
    }

    fun updatePinned(newPinned: Boolean) = viewModelScope.launch {
        //TODO: poner en pinned en sus carpetas
        note.value?.let { noteRepository.update(it.copy(pinned = newPinned)) }
    }

    fun updateSituation(newSituation: Note.Situation) = viewModelScope.launch {
        note.value?.let { noteRepository.update(it.copy(situation = newSituation)) }
    }

    fun updateFolder(newFolderId: Int?) = viewModelScope.launch {
        note.value?.let {
            if (newFolderId != null) {
                folderRepository.insertNoteInFolder(it.noteId, newFolderId)
            } else {
                // TODO: delete folder for this user
            }
        }
    }

    fun insertCollaborator(collaboratorName: String) = viewModelScope.launch {
        users.value?.let { noteRepository.insertUserInNote(openNoteId, collaboratorName) }
    }

    fun deleteCollaborator(collaboratorName: String) = viewModelScope.launch {
        users.value?.let { noteRepository.deleteUserInNote(openNoteId, collaboratorName) }
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
                ) as T
            }
        }
    }
}

fun decomposeInSections(
    contentText: String,
    position: Int = 0,
): List<Section> {
    val regexSections: Map<Section.Type, Regex> = mapOf(
        Section.Type.CHECKBOX to Regex("(?<=\\n)\\[[xXvV]].*?(?=\\n|\$)"),
        Section.Type.ENUMERATION to Regex("(?<=\\n)- .*?(?=\\n|\$)")
    )
    if (contentText != "" && contentText != "\n") {
        for (sectionRegex in regexSections) {
            val matchResult: MatchResult? = sectionRegex.value.find(contentText)
            if (matchResult != null) {
                val previousContent: String = contentText.split(matchResult.value)[0]
                val nextContent = contentText.removePrefix(previousContent + matchResult.value)

                return decomposeInSections(
                    previousContent,
                    position
                )
                    .plus(
                        Section(
                            position + previousContent.length,
                            matchResult.value,
                            sectionRegex.key
                        )
                    )
                    .plus(
                        decomposeInSections(
                            nextContent,
                            position + previousContent.length + matchResult.value.length,
                        )
                    )
            }
        }
        return listOf(Section(position, contentText, Section.Type.SIMPLE_TEXT))
    }
    return emptyList()
}