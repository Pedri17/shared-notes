package com.pproject.sharednotes.data.entity

import androidx.compose.ui.graphics.Color

data class Note(
    var id: Int = 0,
    var title: String = "",
    var content: String = "",
    var pinned: Boolean = false,
    var situation: Situation = Situation.ON_USE,
    var folder: Int? = null,
    var users: List<String> = emptyList<String>(),
    var color: Color? = null,
) {
    enum class Situation {
        ON_USE,
        ARCHIVED,
        DELETED,
    }
}