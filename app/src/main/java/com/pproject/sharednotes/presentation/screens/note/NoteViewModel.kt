package com.pproject.sharednotes.presentation.screens.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.entity.Note
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import com.pproject.sharednotes.data.test.getAllFolderPairNames
import com.pproject.sharednotes.data.test.getNote
import com.pproject.sharednotes.presentation.screens.note.components.Section

class NoteViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var note by mutableStateOf(
        getNote(checkNotNull(savedStateHandle["noteID"])) ?: Note()
    )
        private set

    private var _content = decomposeInSections(note.content).toMutableStateList()
    val content: List<Section>
        get() = _content

    fun updateTitle(newTitle: String) {
        note = note.copy(title = newTitle)
    }

    private fun updateContent(newContent: String) {
        _content = decomposeInSections(newContent).toMutableStateList()
    }

    fun updateContent(section: Section, newText: String) {
        _content[_content.indexOf(section)] = section.copy(text = newText)
    }

    fun updatePinned(newPinned: Boolean) {
        note = note.copy(pinned = newPinned)
    }

    fun updateSituation(newSituation: Note.Situation) {
        note = note.copy(situation = newSituation)
    }

    fun updateFolder(newFolderId: Int?) {
        note = note.copy(folder = newFolderId)
    }

    fun getFolderPairNames(): List<Pair<Int, String>> {
        return getAllFolderPairNames()
    }

    fun addCollaborator(collaboratorName: String): Boolean {
        if (!note.users.contains(collaboratorName)) {
            note.users = note.users.plus(collaboratorName)
        }
        return !note.users.contains(collaboratorName)
    }

    fun deleteCollaborator(collaboratorName: String): Boolean {
        if (note.users.contains(collaboratorName)) {
            note.users = note.users.minus(collaboratorName)
        }
        return note.users.contains(collaboratorName)
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