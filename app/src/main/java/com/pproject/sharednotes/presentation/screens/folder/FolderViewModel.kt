package com.pproject.sharednotes.presentation.screens.folder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.entity.Folder
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.data.test.addNote
import com.pproject.sharednotes.data.test.getAllNotes
import com.pproject.sharednotes.data.test.getFolder
import com.pproject.sharednotes.data.test.getNote

class FolderViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var folder by mutableStateOf(
        getFolder(checkNotNull(savedStateHandle["folderID"])) ?: Folder()
    )
        private set

    var canEditTitle by mutableStateOf(false)
        private set

    fun updateTitle(newTitle: TextFieldValue) {
        folder = folder.copy(title = newTitle)
    }

    fun setTitleCursorToEnd() {
        folder = folder.copy(
            title = folder.title.copy(
                selection = TextRange(folder.title.text.length)
            )
        )
    }

    fun updateCanEditTitle(canEdit: Boolean) {
        canEditTitle = canEdit
    }

    fun getNoteFromId(noteID: Int): Note {
        val note = getNote(noteID)
        if (note != null) {
            return note
        }
        return Note()
    }

    fun getOrderedNotes(): List<Int> {
        val newList: MutableList<Int> = folder.pinnedNotes.toMutableList()
        for (noteID in folder.notes) {
            if (!newList.contains(noteID)) {
                newList.add(noteID)
            }
        }
        return newList.toList()
    }

    fun createNoteInThisFolder(): Int {
        val newNote = Note(
            id = getAllNotes().size,
            folder = folder.id,
        )
        addNote(newNote)
        return newNote.id
    }
}