package com.pproject.sharednotes.presentation.screens.folder

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.entity.Folder
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.data.test.addNote
import com.pproject.sharednotes.data.test.getAllNotes
import com.pproject.sharednotes.data.test.getFolder
import com.pproject.sharednotes.data.test.getNote
import com.pproject.sharednotes.presentation.screens.note.components.Section
import com.pproject.sharednotes.presentation.screens.note.decomposeInSections
import com.pproject.sharednotes.presentation.screens.note.testContent

data class TitleState(
    val title: TextFieldValue = TextFieldValue("New folder"),
    val canEdit: Boolean = false,
)

class FolderViewModel : ViewModel() {

    var folderID by mutableIntStateOf(0)
        private set

    fun setFolder(id: Int) {
        val folder: Folder = getFolder(id) ?: Folder()
        folderID = id
        updateTitle(folder.title)
        updateCanEditTitle(false)
        _notes = folder.notes.toMutableStateList()
        _pinnedNotes = folder.pinnedNotes.toMutableStateList()
    }

    private var _titleState: TitleState by mutableStateOf(TitleState())
    val titleState: TitleState
        get() = _titleState

    fun updateTitle(newTitle: TextFieldValue) {
        _titleState = _titleState.copy(title = newTitle)
    }

    fun setTitleCursorToEnd() {
        _titleState = _titleState.copy(
            title = _titleState.title.copy(
                selection = TextRange(_titleState.title.text.length)
            )
        )
    }

    fun updateCanEditTitle(canEdit: Boolean) {
        Log.d("PreUpdateCanEdit", "era " + _titleState.canEdit)
        _titleState = _titleState.copy(canEdit = canEdit)
        Log.d("UpdateCanEdit", canEdit.toString() + " quedando " + _titleState.canEdit)
    }

    var user: String by mutableStateOf("")
        private set

    private var _notes = mutableStateListOf<Int>()
    val notes: List<Int>
        get() = _notes

    private var _pinnedNotes = mutableStateListOf<Int>()

    val pinnedNotes: List<Int>
        get() = _pinnedNotes

    // Temporal: En caso de no haber nota devuelve una vacía
    fun getNoteFromId(noteID: Int): Note {
        val note = getNote(noteID)
        if (note != null) {
            return note
        }
        return Note()
    }

    fun getOrderedNotes(): List<Int> {
        val newList: MutableList<Int> = _pinnedNotes.toMutableList()
        for (noteID in _notes) {
            if (!newList.contains(noteID)) {
                newList.add(noteID)
            }
        }
        return newList.toList()
    }

    fun createNoteInThisFolder(): Int {
        val newNote: Note = Note(
            id = getAllNotes().size,
            folder = folderID,
        )
        addNote(newNote)
        return newNote.id
    }
}