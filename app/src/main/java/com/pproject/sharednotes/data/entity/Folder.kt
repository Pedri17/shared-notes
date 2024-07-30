package com.pproject.sharednotes.data.entity

import android.util.Log
import androidx.collection.mutableIntListOf
import androidx.compose.ui.text.input.TextFieldValue

data class Folder(
    var id: Int = 0,
    var title: TextFieldValue = TextFieldValue(""),
    var notes: List<Int> = emptyList(),
    var pinnedNotes: List<Int> = emptyList(),
) {
    fun getOrderedNotes(): List<Int> {
        val newList: MutableList<Int> = pinnedNotes.toMutableList()
        for (noteID in notes) {
            if (!newList.contains(noteID)) {
                newList.add(newList.size, noteID)
            }
        }
        return newList.toList()
    }
}