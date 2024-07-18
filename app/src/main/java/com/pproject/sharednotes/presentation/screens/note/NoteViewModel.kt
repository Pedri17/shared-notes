package com.pproject.sharednotes.presentation.screens.note

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pproject.sharednotes.data.entity.Note
import com.pproject.sharednotes.data.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import com.pproject.sharednotes.presentation.screens.note.components.Section

var testContent: String = "PRUEBA de contenido bastante grande\n- para ver\n- que todo\nfuncione mínimamente correctamente, ahora checkbox:\n[X]cosa\n[v]otra cosa\n[V]la última\n"

class NoteViewModel : ViewModel() {
    var title: String by mutableStateOf("")
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    private val _content = decomposeInSections(testContent).toMutableStateList()
    val content: List<Section>
        get() = _content

    fun updateContent(section: Section, newText: String) {
        _content[_content.indexOf(section)] = section.copy(text = newText)
    }

    var pinned: Boolean by mutableStateOf(false)
        private set

    fun updatePinned(newPinned: Boolean) {
        pinned = newPinned
    }

    var situation: Note.Situation by mutableStateOf(Note.Situation.ON_USE)

    fun updateSituation(newSituation: Note.Situation) {
        situation = newSituation
    }

    var folderId: Int by mutableIntStateOf(-1)
        private set

    fun updateFolder(newFolderId: Int) {
        folderId = newFolderId
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